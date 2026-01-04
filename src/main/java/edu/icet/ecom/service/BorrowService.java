package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.BorrowDto;
import java.util.List;

public interface BorrowService {
    String saveDetails(BorrowDto borrowDto);
    String updateDetails(BorrowDto borrowDto); // Added Update
    List<BorrowDto> getAllHistory(); // For History view
    List<BorrowDto> getHistoryByUserId(Long userid);
}