package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @PutMapping("/update")
    public String update(@RequestBody BorrowDto borrowDto) {
        return borrowService.updateDetails(borrowDto);
    }

    @GetMapping("/all")
    public List<BorrowDto> getAllHistory() {
        return borrowService.getAllHistory();
    }
}