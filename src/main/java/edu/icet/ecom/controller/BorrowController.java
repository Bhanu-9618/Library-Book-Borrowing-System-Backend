package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.service.BorrowService;
import edu.icet.ecom.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/borrow")
@CrossOrigin(origins = "*")
public class BorrowController {

    @Autowired
    BorrowService borrowService;

    @PostMapping("/save")
    public ResponseEntity<StandardResponse> save(@RequestBody BorrowDto borrowDto){
        String result = borrowService.saveDetails(borrowDto);
        return new ResponseEntity<>(
                new StandardResponse(201, result, null),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update")
    public ResponseEntity<StandardResponse> update(@RequestBody BorrowDto borrowDto) {
        String result = borrowService.updateDetails(borrowDto);
        return new ResponseEntity<>(
                new StandardResponse(200, result, null),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    public ResponseEntity<StandardResponse> getAllHistory() {
        List<BorrowDto> history = borrowService.getAllHistory();
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", history),
                HttpStatus.OK
        );
    }

    @GetMapping("/search/{userid}")
    public ResponseEntity<StandardResponse> getHistoryByUserId(@PathVariable Long userid) {
        List<BorrowDto> history = borrowService.getHistoryByUserId(userid);
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", history),
                HttpStatus.OK
        );
    }
}