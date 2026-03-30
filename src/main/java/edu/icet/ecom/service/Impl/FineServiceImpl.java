package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.FineDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.model.entity.FineEntity;
import edu.icet.ecom.model.enums.BorrowStatus;
import edu.icet.ecom.model.enums.PaymentStatus;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.FineRepository;
import edu.icet.ecom.service.FineService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FineServiceImpl implements FineService {

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public FineDto getFineByBorrowId(Long borrowId) {
        Optional<FineEntity> fineEntityOptional = fineRepository.findByBorrowEntity_Borrowid(borrowId);
        
        if (fineEntityOptional.isPresent()) {
            FineEntity fineEntity = fineEntityOptional.get();
            FineDto fineDto = new FineDto();
            fineDto.setFineAmount(fineEntity.getFineAmount());
            fineDto.setUserId(fineEntity.getUserEntity().getId());
            fineDto.setUserName(fineEntity.getUserEntity().getName());
            fineDto.setPaymentStatus(fineEntity.getPaymentStatus());
            return fineDto;
        }
        return null;
    }

    @Override
    @Transactional
    public void updateFineStatus(Long borrowId, String status) {
        Optional<FineEntity> fineEntityOptional = fineRepository.findByBorrowEntity_Borrowid(borrowId);

        if (fineEntityOptional.isPresent()) {
            FineEntity fineEntity = fineEntityOptional.get();

            if ("PAID".equalsIgnoreCase(status)) {
                fineEntity.setPaymentStatus(PaymentStatus.PAID);
                fineRepository.save(fineEntity);

                BorrowEntity borrowEntity = fineEntity.getBorrowEntity();
                borrowEntity.setStatus(BorrowStatus.RETURNED);
                borrowEntity.setReturnDate(LocalDate.now());
                borrowRepository.save(borrowEntity);

                BookEntity book = borrowEntity.getBookEntity();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                book.setAvailable(true);
                bookRepository.save(book);
            }
        }
    }
}
