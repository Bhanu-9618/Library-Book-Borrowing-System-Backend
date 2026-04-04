package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.BorrowDto;
import java.util.List;
import java.util.Map;
import edu.icet.ecom.model.dto.OverdueResponseDto;

public interface BorrowService {
    String saveDetails(BorrowDto borrowDto);
    String updateDetails(BorrowDto borrowDto); // Added Update
    Map<String, Object> getAllHistory(int page, int size); // For History view
    Map<String, Object> getHistoryByUserId(Long userid, int page, int size);
    long getTotalBorrowCount();
    Map<String, Object> getRequestedHistory(int page, int size);
    long getRequestedCount();
    Map<String, Object> getOverdueHistory(int page, int size);
    long getOverdueCount();
    long getIssuedCount();
}