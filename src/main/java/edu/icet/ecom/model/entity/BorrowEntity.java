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

    @Column(nullable = false)
    private LocalDate borrowdate;
    @Column(nullable = false)
    private LocalDate dueDate;
    @Column(nullable = false)
    private LocalDate returnDate;
    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private BookEntity bookEntity;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private UserEntity userEntity;

}

