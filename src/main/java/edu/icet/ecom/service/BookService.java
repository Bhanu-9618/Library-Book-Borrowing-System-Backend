package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.BookDto;

import java.util.List;

public interface BookService {

    void add(BookDto bookDto);
    List<BookDto> getAllDetails() throws Exception;
    void update(BookDto bookDto);
    void delete(String id);
    BookDto searchById(String bookId);
}
