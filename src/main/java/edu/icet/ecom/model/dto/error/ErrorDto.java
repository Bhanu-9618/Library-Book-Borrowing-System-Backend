package edu.icet.ecom.model.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDto {
    private String messege;
    private Integer code;
}
