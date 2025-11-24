package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
@CrossOrigin(origins = "*")
public class BorrowController {

    @Autowired
    BorrowService borrowService;

    @PostMapping("/save")
    public String save(@RequestBody BorrowDto borrowDto){
        return borrowService.saveDetails(borrowDto);
    }
}
