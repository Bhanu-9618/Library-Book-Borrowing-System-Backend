package edu.icet.ecom.service.Impl;

import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.model.entity.FineEntity;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.FineRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.BorrowService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import edu.icet.ecom.model.enums.BorrowStatus;
import edu.icet.ecom.model.enums.PaymentStatus;
import edu.icet.ecom.model.dto.OverdueResponseDto;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    ModelMapper mapper;

    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FineRepository fineRepository;

    @Override
    @Transactional
    public String saveDetails(BorrowDto borrowDto) {
        BookEntity book = bookRepository.findById(borrowDto.getBookid())
                .orElseThrow(() -> new ResourceNotFoundException("Book Not Found!"));

        if (book.getAvailableCopies() <= 0) {
            return "Book is currently unavailable!";
        }

        if (!userRepository.existsById(borrowDto.getUserid())) {
            throw new ResourceNotFoundException("User Not Found!");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        if (book.getAvailableCopies() == 0) {
            book.setAvailable(false);
        }
        bookRepository.save(book);

        BorrowEntity entity = new BorrowEntity();
        entity.setBookEntity(book);
        entity.setUserEntity(userRepository.getReferenceById(borrowDto.getUserid()));
        entity.setStatus(BorrowStatus.REQUESTED);
        entity.setBorrowdate(null);
        entity.setDueDate(null);
        entity.setReturnDate(null);

        borrowRepository.save(entity);
        return "Book requested successfully. Waiting for admin approval.";
    }

    @Override
    @Transactional
    public String updateDetails(BorrowDto borrowDto) {
        BorrowEntity existingBorrow = borrowRepository.findById(borrowDto.getBorrowid())
                .orElseThrow(() -> new ResourceNotFoundException("Borrow Record Not Found!"));

        BookEntity book = existingBorrow.getBookEntity();

        if (BorrowStatus.ISSUED == borrowDto.getStatus() && BorrowStatus.REQUESTED == existingBorrow.getStatus()) {
            existingBorrow.setStatus(BorrowStatus.ISSUED);
            existingBorrow.setBorrowdate(LocalDate.now());
            existingBorrow.setDueDate(LocalDate.now().plusDays(14));
            borrowRepository.save(existingBorrow);
            return "Book issued successfully. Due date set to 14 days from today.";
        }

        if (BorrowStatus.RETURNED == borrowDto.getStatus() && (BorrowStatus.ISSUED == existingBorrow.getStatus() || BorrowStatus.OVERDUE == existingBorrow.getStatus())) {
            existingBorrow.setStatus(BorrowStatus.RETURNED);
            LocalDate returnDate = LocalDate.now();
            existingBorrow.setReturnDate(returnDate);

            book.setAvailableCopies(book.getAvailableCopies() + 1);
            book.setAvailable(true);
            bookRepository.save(book);

            borrowRepository.save(existingBorrow);

            if (returnDate.isAfter(existingBorrow.getDueDate())) {
                long daysLate = ChronoUnit.DAYS.between(existingBorrow.getDueDate(), returnDate);
                double finalFineAmount = daysLate * 50.0;

                FineEntity fine = fineRepository.findByBorrowEntity_Borrowid(existingBorrow.getBorrowid())
                        .orElse(new FineEntity());

                fine.setBorrowEntity(existingBorrow);
                fine.setUserEntity(existingBorrow.getUserEntity());
                fine.setFineAmount(finalFineAmount);

                if (fine.getPaymentStatus() == null) {
                    fine.setPaymentStatus(PaymentStatus.UNPAID);
                }

                fineRepository.save(fine);

                return "Book returned successfully. Late fine of Rs " + finalFineAmount + " applied.";
            }

            return "Book returned successfully. Inventory updated.";
        }

        return "Invalid status update. Check current status.";
    }

    @Override
    public Map<String, Object> getAllHistory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowEntity> borrowPage = borrowRepository.findAll(pageable);
        return createPaginatedBorrowResponse(borrowPage);
    }

    @Override
    public Map<String, Object> getHistoryByUserId(Long userid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowEntity> borrowPage = borrowRepository.findByUserEntity_Id(userid, pageable);
        return createPaginatedBorrowResponse(borrowPage);
    }

    @Override
    public long getTotalBorrowCount() {
        return borrowRepository.count();
    }

    @Override
    public Map<String, Object> getRequestedHistory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowEntity> borrowPage = borrowRepository.findByStatus(BorrowStatus.REQUESTED, pageable);
        return createPaginatedBorrowResponse(borrowPage);
    }

    @Override
    public long getRequestedCount() {
        return borrowRepository.countByStatus(BorrowStatus.REQUESTED);
    }

    @Override
    public Map<String, Object> getOverdueHistory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowEntity> borrowPage = borrowRepository.findByStatus(BorrowStatus.OVERDUE, pageable);

        List<OverdueResponseDto> list = new ArrayList<>();
        for (BorrowEntity entity : borrowPage.getContent()) {
            Optional<FineEntity> fineOpt = fineRepository.findByBorrowEntity_Borrowid(entity.getBorrowid());
            OverdueResponseDto dto = new OverdueResponseDto();
            dto.setUserid(entity.getUserEntity().getId());
            dto.setBorrowid(entity.getBorrowid());
            if (fineOpt.isPresent()) {
                dto.setFineAmount(fineOpt.get().getFineAmount());
                dto.setPaymentStatus(fineOpt.get().getPaymentStatus());
            } else {
                dto.setFineAmount(0.0);
                dto.setPaymentStatus(PaymentStatus.UNPAID);
            }
            list.add(dto);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("history", list);
        response.put("currentPage", borrowPage.getNumber());
        response.put("totalItems", borrowPage.getTotalElements());
        response.put("totalPages", borrowPage.getTotalPages());
        return response;
    }

    private Map<String, Object> createPaginatedBorrowResponse(Page<BorrowEntity> borrowPage) {
        List<BorrowDto> historyList = new ArrayList<>();
        for (BorrowEntity entity : borrowPage.getContent()) {
            BorrowDto dto = mapper.map(entity, BorrowDto.class);
            if (entity.getBookEntity() != null) dto.setBookid(entity.getBookEntity().getId());
            if (entity.getUserEntity() != null) dto.setUserid(entity.getUserEntity().getId());
            historyList.add(dto);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("history", historyList);
        response.put("currentPage", borrowPage.getNumber());
        response.put("totalItems", borrowPage.getTotalElements());
        response.put("totalPages", borrowPage.getTotalPages());
        return response;
    }

    @Override
    public long getOverdueCount() {
        return borrowRepository.countByStatus(BorrowStatus.OVERDUE);
    }

    @Override
    public long getIssuedCount() {
        return borrowRepository.countByStatus(BorrowStatus.ISSUED);
    }
}