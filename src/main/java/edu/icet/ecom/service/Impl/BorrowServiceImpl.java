package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.BorrowService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
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
    @Transactional
    public String saveDetails(BorrowDto borrowDto) {
        if (!bookRepository.existsById(borrowDto.getBookid())) {
            return "Book Not Found!";
        }

        Optional<BookEntity> bookEntity = bookRepository.findById(borrowDto.getBookid());
        BookEntity bookEntity1 = bookEntity.orElseThrow();

        if(bookEntity1.getAvailableCopies() <= 0){
            bookEntity1.setAvailability("unavailable");
            bookRepository.save(bookEntity1);
            return "All the books borrowed!";
        }

        if (!userRepository.existsById(borrowDto.getUserid())) {
            return "User Not Found!";
        }

        bookEntity1.setAvailableCopies(bookEntity1.getAvailableCopies() - 1);
        bookRepository.save(bookEntity1);

        BorrowEntity entity = mapper.map(borrowDto, BorrowEntity.class);
        entity.setBookEntity(bookRepository.getReferenceById(borrowDto.getBookid()));
        entity.setUserEntity(userRepository.getReferenceById(String.valueOf(borrowDto.getUserid())));

        borrowRepository.save(entity);
        return "Borrow Successful!";
    }

    @Override
    @Transactional
    public String updateDetails(BorrowDto borrowDto) {

        Optional<BookEntity> bookEntity = bookRepository.findById(borrowDto.getBookid());
        BookEntity bookEntity1 = bookEntity.orElseThrow();

        BorrowEntity entity = mapper.map(borrowDto, BorrowEntity.class);
        entity.setBookEntity(bookRepository.getReferenceById(borrowDto.getBookid()));
        entity.setUserEntity(userRepository.getReferenceById(String.valueOf(borrowDto.getUserid())));
        bookEntity1.setAvailableCopies(bookEntity1.getAvailableCopies() + 1);

        borrowRepository.save(entity);
        return "Updated Successful!";
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
}