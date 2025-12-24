package edu.icet.ecom.model.dto;

import lombok.*;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BorrowDto {
    private Long borrowid; // Added this to show in history
    private LocalDate borrowdate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private Long bookid;
    private Long userid;
}