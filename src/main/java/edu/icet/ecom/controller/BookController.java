package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/save")
    public void getAllBookDetails(@RequestBody BookDto bookDto){
        bookService.add(bookDto);
        System.out.println(bookDto);
    }

    @GetMapping("/all")
    public List<BookDto> getDetails(){
        List<BookDto> bookDtoList = bookService.getAllDetails();
        return bookDtoList;
    }

    @PutMapping("/update")
    public void update(@RequestBody BookDto bookDto){
        bookService.update(bookDto);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable String id){
        bookService.delete(id);
    }

    @GetMapping("id/{bookId}")
    public BookDto searchById(@PathVariable String bookId){
        return bookService.searchById(bookId);
    }
}
