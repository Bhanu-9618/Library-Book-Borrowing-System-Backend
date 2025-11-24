package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.BorrowService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BorrowServiceImpl implements BorrowService {

    ModelMapper mapper = new ModelMapper();

    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public String saveDetails(BorrowDto borrowDto) {

        System.out.println(borrowDto.getBookid());
        System.out.println(borrowDto.getUserid());
        if (!bookRepository.existsById(borrowDto.getBookid())) {
            return "Book Not Found!";
        }

        Optional<BookEntity> bookEntity = bookRepository.findById(borrowDto.getBookid());
        BookEntity bookEntity1 = bookEntity.orElseThrow();

        if(bookEntity1.getAvailability().equals("unavailable")){
            return "Book already borrowed!";
        }

        if (!userRepository.existsById(borrowDto.getUserid())) {
            return "User Not Found!";
        }
        bookEntity1.setAvailability("unavailable");

        BorrowEntity entity = mapper.map(borrowDto, BorrowEntity.class);

        entity.setBookEntity(bookRepository.getReferenceById(borrowDto.getBookid()));
        entity.setUserEntity(userRepository.getReferenceById(String.valueOf(borrowDto.getUserid())));

        borrowRepository.save(entity);
        return "Borrow Successfull!";
    }
}
