package io.github.ggreg1987.Library.domain.entities;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    private Long id;
    private String author;
    private String title;
    private String isbn;
}
