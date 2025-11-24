package edu.icet.ecom.model.dto;

import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.entity.UserEntity;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BorrowDto {

    private LocalDate borrowdate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private Long bookid;
    private Long userid;
}
