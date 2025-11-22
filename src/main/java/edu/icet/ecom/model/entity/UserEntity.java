package edu.icet.ecom.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDate membershipdate;

    public UserEntity(String name, String email, String phone, String address, LocalDate membershipdate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membershipdate = membershipdate;
    }
}
