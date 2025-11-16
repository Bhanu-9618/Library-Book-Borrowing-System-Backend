package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.BookDto;

import java.util.List;

public interface BookService {

    void add(BookDto bookDto);
    List<BookDto> getAllDetails();
    void update(BookDto bookDto);
}
