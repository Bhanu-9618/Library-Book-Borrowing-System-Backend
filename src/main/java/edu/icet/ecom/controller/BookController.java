package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PreAuthorize("hasRole('ADMIN')") // Admin Only
    public void saveBook(@RequestBody BookDto bookDto){
        bookService.add(bookDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Both can view
    public List<BookDto> getDetails() throws Exception {
        return bookService.getAllDetails();
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')") // Admin Only
    public void update(@RequestBody BookDto bookDto){
        bookService.update(bookDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Admin Only
    public void delete(@PathVariable Long id){
        bookService.delete(id);
    }

    @GetMapping("/id/{bookId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Both can search
    public BookDto searchById(@PathVariable Long bookId){
        return bookService.searchById(bookId);
    }
}