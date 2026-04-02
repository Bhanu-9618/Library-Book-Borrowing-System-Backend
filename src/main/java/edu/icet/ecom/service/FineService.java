package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.FineDto;

public interface FineService {
    FineDto getFineByBorrowId(Long borrowId);
    void updateFineStatus(Long borrowId, String status);
}
