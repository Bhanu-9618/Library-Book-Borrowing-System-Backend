package edu.icet.ecom.model.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDate membershipdate;
}
