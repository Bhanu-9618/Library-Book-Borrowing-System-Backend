package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.FineDto;
import edu.icet.ecom.model.entity.FineEntity;
import edu.icet.ecom.repository.FineRepository;
import edu.icet.ecom.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FineServiceImpl implements FineService {

    @Autowired
    private FineRepository fineRepository;

    @Override
    public FineDto getFineByBorrowId(Long borrowId) {
        Optional<FineEntity> fineEntityOptional = fineRepository.findByBorrowEntity_Borrowid(borrowId);
        
        if (fineEntityOptional.isPresent()) {
            FineEntity fineEntity = fineEntityOptional.get();
            FineDto fineDto = new FineDto();
            fineDto.setFineAmount(fineEntity.getFineAmount());
            fineDto.setUserId(fineEntity.getUserEntity().getId());
            fineDto.setUserName(fineEntity.getUserEntity().getName());
            fineDto.setPaymentStatus(fineEntity.getPaymentStatus());
            return fineDto;
        }
        return null;
    }
}
