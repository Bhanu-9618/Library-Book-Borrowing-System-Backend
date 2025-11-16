package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.service.BookService;
import edu.icet.ecom.service.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
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
}
