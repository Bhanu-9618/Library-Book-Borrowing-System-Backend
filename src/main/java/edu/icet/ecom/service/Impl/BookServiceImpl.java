package edu.icet.ecom.service.Impl;

import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.icet.ecom.model.enums.BookCategory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public void add(BookDto bookDto) {
        log.info("Adding new book record");
        BookEntity entity = mapper.map(bookDto, BookEntity.class);
        bookRepository.save(entity);
    }

    @Override
    public List<BookDto> getAllDetails() throws Exception {
        return bookRepository.findAll().stream()
                .map(entity -> mapper.map(entity, BookDto.class))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void update(BookDto bookDto) {
        log.info("Updating book record for ID: {}", bookDto.getId());
        if (!bookRepository.existsById(bookDto.getId())) {
            throw new ResourceNotFoundException("Book Not Found!");
        }
        BookEntity entity = mapper.map(bookDto, BookEntity.class);
        bookRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting book record for ID: {}", id);
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book Not Found!");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto searchById(Long bookId) {
        BookEntity entity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book Not Found!"));
        return mapper.map(entity, BookDto.class);
    }

    @Override
    public Map<String, Object> getPaginatedBooks(int page, int size, BookCategory category) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookPage;
        if (category != null) {
            bookPage = bookRepository.findByCategory(category, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }
        return createPaginatedResponse(bookPage);
    }

    @Override
    public Map<String, Object> searchPaginatedBooks(String term, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookEntity> bookPage = bookRepository.searchBooksByTerm(term, pageable);
        return createPaginatedResponse(bookPage);
    }

    private Map<String, Object> createPaginatedResponse(Page<BookEntity> bookPage) {
        List<BookDto> bookDtos = bookPage.getContent().stream()
                .map(entity -> mapper.map(entity, BookDto.class))
                .collect(java.util.stream.Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("books", bookDtos);
        response.put("currentPage", bookPage.getNumber());
        response.put("totalItems", bookPage.getTotalElements());
        response.put("totalPages", bookPage.getTotalPages());

        return response;
    }

    @Override
    public long getTotalBooksCount() {
        return bookRepository.count();
    }
}