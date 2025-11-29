package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    ModelMapper mapper = new ModelMapper();

    @Autowired
    BookRepository bookRepository;

    public void add(BookDto bookDto){
        bookRepository.save(mapper.map(bookDto,BookEntity.class));
    }

    public List<BookDto> getAllDetails(){
        List<BookEntity> bookList =  bookRepository.findAll();
        List<BookDto> bookDtos = new ArrayList<>();

        for (BookEntity bookEntity : bookList){
            bookDtos.add(mapper.map(bookEntity, BookDto.class));
        }
        return bookDtos;
    }

    public void update(BookDto bookDto) {
        bookRepository.save(mapper.map(bookDto,BookEntity.class));
    }

    public void delete(String id) {
        bookRepository.deleteById(Long.valueOf(id));
    }

    public BookDto searchById(String bookId){
        Optional<BookEntity> byId = bookRepository.findById(Long.valueOf(bookId));
        BookEntity bookEntity = byId.orElseThrow();
        return (mapper.map(bookEntity , BookDto.class));
    }
}
