package edu.icet.ecom.model.dto;

import edu.icet.ecom.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FineDto {
    private Double fineAmount;
    private Long userId;
    private String userName;
    private PaymentStatus paymentStatus;
}
