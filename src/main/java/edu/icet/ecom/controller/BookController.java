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
import java.util.List;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> saveBook(@Valid @RequestBody BookDto bookDto){
        bookService.add(bookDto);
        return new ResponseEntity<>(
                new StandardResponse(201, "Book Saved Successfully", null),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StandardResponse> getDetails() throws Exception {
        List<BookDto> allBooks = bookService.getAllDetails();
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", allBooks),
                HttpStatus.OK
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> update(@Valid @RequestBody BookDto bookDto){
        bookService.update(bookDto);
        return new ResponseEntity<>(
                new StandardResponse(200, "Book Updated Successfully", null),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> delete(@PathVariable Long id){
        bookService.delete(id);
        return new ResponseEntity<>(
                new StandardResponse(200, "Book Deleted Successfully", null),
                HttpStatus.OK
        );
    }

    @GetMapping("/id/{bookId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StandardResponse> searchById(@PathVariable Long bookId){
        BookDto bookDto = bookService.searchById(bookId);
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", bookDto),
                HttpStatus.OK
        );
    }
}