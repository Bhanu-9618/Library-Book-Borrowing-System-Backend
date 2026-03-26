package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.service.BookService;
import edu.icet.ecom.util.StandardResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import edu.icet.ecom.model.enums.BookCategory;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> save(@Valid @RequestBody BookDto bookDto) {
        bookService.add(bookDto);
        return new ResponseEntity<>(
                new StandardResponse(201, "Book saved successfully", null),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> update(@Valid @RequestBody BookDto bookDto) {
        bookService.update(bookDto);
        return new ResponseEntity<>(
                new StandardResponse(200, "Book updated successfully", null),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> delete(@PathVariable Long id) {
        bookService.delete(id);
        return new ResponseEntity<>(
                new StandardResponse(200, "Book deleted successfully", null),
                HttpStatus.OK
        );
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StandardResponse> searchById(@PathVariable Long id) {
        BookDto bookDto = bookService.searchById(id);
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", bookDto),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StandardResponse> getDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(required = false) BookCategory category) {

        Map<String, Object> paginatedData = bookService.getPaginatedBooks(page, size, category);

        return new ResponseEntity<>(
                new StandardResponse(200, "Success", paginatedData),
                HttpStatus.OK
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StandardResponse> searchBooks(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size) {

        Map<String, Object> paginatedData = bookService.searchPaginatedBooks(term, page, size);

        return new ResponseEntity<>(
                new StandardResponse(200, "Success", paginatedData),
                HttpStatus.OK
        );
    }

    @GetMapping("/count")
    public ResponseEntity<StandardResponse> getTotalBooksCount() {
        long count = bookService.getTotalBooksCount();
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", count),
                HttpStatus.OK
        );
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StandardResponse> getAllCategories() {
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", BookCategory.values()),
                HttpStatus.OK
        );
    }
}