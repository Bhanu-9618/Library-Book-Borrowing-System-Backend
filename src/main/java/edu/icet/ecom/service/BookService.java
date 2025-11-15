package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

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
}
