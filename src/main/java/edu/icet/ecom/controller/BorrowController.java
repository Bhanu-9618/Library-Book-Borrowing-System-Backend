package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.model.dto.OverdueResponseDto;
import edu.icet.ecom.service.BorrowService;
import edu.icet.ecom.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    BorrowService borrowService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StandardResponse> save(@RequestBody BorrowDto borrowDto){
        String result = borrowService.saveDetails(borrowDto);
        return new ResponseEntity<>(
                new StandardResponse(201, result, null),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> update(@RequestBody BorrowDto borrowDto) {
        String result = borrowService.updateDetails(borrowDto);
        return new ResponseEntity<>(
                new StandardResponse(200, result, null),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> getAllHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> history = borrowService.getAllHistory(page, size);
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", history),
                HttpStatus.OK
        );
    }

    @GetMapping("/search/{userid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StandardResponse> getHistoryByUserId(
            @PathVariable Long userid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> history = borrowService.getHistoryByUserId(userid, page, size);
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", history),
                HttpStatus.OK
        );
    }

    @GetMapping("/count")
    public ResponseEntity<StandardResponse> getTotalBorrowCount() {
        long count = borrowService.getTotalBorrowCount();
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", count),
                HttpStatus.OK
        );
    }

    @GetMapping("/requested")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> getRequestedHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> history = borrowService.getRequestedHistory(page, size);
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", history),
                HttpStatus.OK
        );
    }

    @GetMapping("/requested/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> getRequestedCount() {
        long count = borrowService.getRequestedCount();
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", count),
                HttpStatus.OK
        );
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> getOverdueHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> history = borrowService.getOverdueHistory(page, size);
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", history),
                HttpStatus.OK
        );
    }

    @GetMapping("/overdue/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> getOverdueCount() {
        long count = borrowService.getOverdueCount();
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", count),
                HttpStatus.OK
        );
    }

    @GetMapping("/issued/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> getIssuedCount() {
        long count = borrowService.getIssuedCount();
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", count),
                HttpStatus.OK
        );
    }
}