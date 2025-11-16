package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    public void add(BookDto bookDto){
        BookEntity bookEntity = new BookEntity(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getPublisher(),
                bookDto.getIsbn(),
                bookDto.getCategory(),
                bookDto.getAvailableCopies(),
                bookDto.getAvailability().toLowerCase()
        );
        bookRepository.save(bookEntity);
    }

    public List<BookDto> getAllDetails(){
        List<BookEntity> bookList =  bookRepository.findAll();
        List<BookDto> bookDtos = new ArrayList<>();

        for (BookEntity bookEntity : bookList){
            bookDtos.add( new BookDto(
                            bookEntity.getId(),
                            bookEntity.getTitle(),
                            bookEntity.getAuthor(),
                            bookEntity.getPublisher(),
                            bookEntity.getIsbn(),
                            bookEntity.getCategory(),
                            bookEntity.getAvailableCopies(),
                            bookEntity.getAvailability()
                    )
            );
        }
        return bookDtos;
    }

    public void update(BookDto bookDto) {
        BookEntity bookEntity = new BookEntity(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getPublisher(),
                bookDto.getIsbn(),
                bookDto.getCategory(),
                bookDto.getAvailableCopies(),
                bookDto.getAvailability()
        );
        bookRepository.save(bookEntity);
    }

    public void delete(String id) {
        bookRepository.deleteById(Long.valueOf(id));
    }

    public BookDto searchById(String bookId){
        Optional<BookEntity> byId = bookRepository.findById(Long.valueOf(bookId));

        BookEntity bookEntity = byId.orElseThrow();
        return new BookDto(
                bookEntity.getId(),
                bookEntity.getAuthor(),
                bookEntity.getTitle(),
                bookEntity.getPublisher(),
                bookEntity.getIsbn(),
                bookEntity.getPublisher(),
                bookEntity.getAvailableCopies(),
                bookEntity.getAvailability()
        );
    }
}
