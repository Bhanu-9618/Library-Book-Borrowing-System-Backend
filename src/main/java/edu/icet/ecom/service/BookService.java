package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.BookDto;

import java.util.List;

public interface BookService {

    void add(BookDto bookDto);
    List<BookDto> getAllDetails() throws Exception;
    void update(BookDto bookDto);
    void delete(Long id);
    BookDto searchById(Long bookId);
}
