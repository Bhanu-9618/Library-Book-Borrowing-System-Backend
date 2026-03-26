package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.BookDto;

import java.util.List;
import java.util.Map;

public interface BookService {

    void add(BookDto bookDto);

    List<BookDto> getAllDetails() throws Exception;

    void update(BookDto bookDto);

    void delete(Long id);

    BookDto searchById(Long bookId);

    Map<String, Object> getPaginatedBooks(int page, int size);

    Map<String, Object> searchPaginatedBooks(String term, int page, int size);

    long getTotalBooksCount();
}