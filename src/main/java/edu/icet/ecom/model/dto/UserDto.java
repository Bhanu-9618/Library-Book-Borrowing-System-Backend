package edu.icet.ecom.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.icet.ecom.model.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    private LocalDate membershipdate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

    private Role role;

    private Boolean isActive;
}