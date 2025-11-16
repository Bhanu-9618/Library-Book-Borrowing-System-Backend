package edu.icet.ecom.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class BorrowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowid;

    private LocalDate borrowdate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private BookEntity bookEntity;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private UserEntity userEntity;

    public BorrowEntity( LocalDate borrowdate, LocalDate dueDate, LocalDate returnDate, String status, Long id, String userid) {

        this.borrowdate = LocalDate.now();
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;

        BookEntity book = new BookEntity();
        book.setId(id);
        this.bookEntity = book;

        UserEntity user = new UserEntity();
        user.setUserid(userid);
        this.userEntity = user;
    }
}

