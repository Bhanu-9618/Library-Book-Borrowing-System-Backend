package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService{

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
                bookDto.getAvailableCopies()
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
                            bookEntity.getAvailableCopies()
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
                bookDto.getAvailableCopies()
        );
        bookRepository.save(bookEntity);
    }

    public void delete(String id) {
        bookRepository.deleteById(Long.valueOf(id));
    }
}
