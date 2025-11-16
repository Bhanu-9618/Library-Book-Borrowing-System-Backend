package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public String saveDetails(BorrowDto borrowDto) {

        if (!bookRepository.existsById(Long.valueOf(borrowDto.getId()))) {
            return "Book Not Found!";
        }

        Optional<BookEntity> bookEntity = bookRepository.findById(borrowDto.getId());
        BookEntity bookEntity1 = bookEntity.orElseThrow();

        if(bookEntity1.getAvailability().equals("unavailable")){
            return "Book already borrowed!";
        }

        if (!userRepository.existsById(borrowDto.getUserid())) {
            return "User Not Found!";
        }

        bookEntity1.setAvailability("unavailable");
        BorrowEntity borrowEntity = new BorrowEntity(
                borrowDto.getBorrowdate(),
                borrowDto.getDueDate(),
                borrowDto.getReturnDate(),
                borrowDto.getStatus(),
                borrowDto.getId(),
                borrowDto.getUserid()
        );
        borrowRepository.save(borrowEntity);
        return "Borrow Successfull!";
    }
}
