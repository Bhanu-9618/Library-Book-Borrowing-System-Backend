package edu.icet.ecom.model.dto;

import edu.icet.ecom.model.enums.PaymentStatus;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OverdueResponseDto {
    private Long userid;
    private Long borrowid;
    private Double fineAmount;
    private PaymentStatus paymentStatus;
}
