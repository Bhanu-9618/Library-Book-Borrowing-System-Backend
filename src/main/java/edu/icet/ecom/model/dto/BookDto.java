package edu.icet.ecom.model.dto;

import lombok.*;
import edu.icet.ecom.model.enums.BookCategory;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private BookCategory category;
    private int availableCopies;
    private boolean available = true;
}
