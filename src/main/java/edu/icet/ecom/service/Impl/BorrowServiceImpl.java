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
import java.util.stream.Collectors;

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
        entity.setStatus("REQUESTED");
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

        if ("ISSUED".equalsIgnoreCase(borrowDto.getStatus()) && "REQUESTED".equalsIgnoreCase(existingBorrow.getStatus())) {
            existingBorrow.setStatus("ISSUED");
            existingBorrow.setBorrowdate(LocalDate.now());
            existingBorrow.setDueDate(LocalDate.now().plusDays(14));
            borrowRepository.save(existingBorrow);
            return "Book issued successfully. Due date set to 14 days from today.";
        }

        if ("RETURNED".equalsIgnoreCase(borrowDto.getStatus()) && "ISSUED".equalsIgnoreCase(existingBorrow.getStatus())) {
            existingBorrow.setStatus("RETURNED");
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
                    fine.setPaymentStatus("UNPAID");
                }

                fineRepository.save(fine);

                return "Book returned successfully. Late fine of Rs " + finalFineAmount + " applied.";
            }

            return "Book returned successfully. Inventory updated.";
        }

        return "Invalid status update. Check current status.";
    }

    @Override
    public List<BorrowDto> getAllHistory() {
        List<BorrowEntity> entities = borrowRepository.findAll();
        List<BorrowDto> historyList = new ArrayList<>();

        for (BorrowEntity entity : entities) {
            BorrowDto dto = mapper.map(entity, BorrowDto.class);
            dto.setBookid(entity.getBookEntity().getId());
            dto.setUserid(entity.getUserEntity().getId());
            historyList.add(dto);
        }
        return historyList;
    }

    @Override
    public List<BorrowDto> getHistoryByUserId(Long userid) {
        List<BorrowEntity> entities = borrowRepository.findByUserEntity_Id(userid);

        return entities.stream().map(entity -> {
            BorrowDto dto = mapper.map(entity, BorrowDto.class);
            if (entity.getUserEntity() != null) {
                dto.setUserid(entity.getUserEntity().getId());
            }
            if (entity.getBookEntity() != null) {
                dto.setBookid(entity.getBookEntity().getId());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public long getTotalBorrowCount() {
        return borrowRepository.count();
    }
}