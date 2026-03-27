package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.BorrowDto;
import java.util.List;
import edu.icet.ecom.model.dto.OverdueResponseDto;

public interface BorrowService {
    String saveDetails(BorrowDto borrowDto);
    String updateDetails(BorrowDto borrowDto); // Added Update
    List<BorrowDto> getAllHistory(); // For History view
    List<BorrowDto> getHistoryByUserId(Long userid);
    long getTotalBorrowCount();
    List<BorrowDto> getRequestedHistory();
    long getRequestedCount();
    List<OverdueResponseDto> getOverdueHistory();
    long getOverdueCount();
    long getIssuedCount();
}