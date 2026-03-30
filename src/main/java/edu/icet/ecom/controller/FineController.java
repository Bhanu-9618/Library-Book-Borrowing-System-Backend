package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.FineDto;
import edu.icet.ecom.service.FineService;
import edu.icet.ecom.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fine")
@CrossOrigin(origins = "*")
public class FineController {

    @Autowired
    private FineService fineService;

    @GetMapping("/search/{borrowId}")
    public ResponseEntity<StandardResponse> getFineByBorrowId(@PathVariable Long borrowId) {
        FineDto fineDto = fineService.getFineByBorrowId(borrowId);
        
        if (fineDto != null) {
            return new ResponseEntity<>(
                    new StandardResponse(200, "Fine Details Found", fineDto),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new StandardResponse(404, "No Fine Record Found for This Borrow ID", null),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}
