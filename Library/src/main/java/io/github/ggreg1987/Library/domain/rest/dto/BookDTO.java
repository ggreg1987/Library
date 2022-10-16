package io.github.ggreg1987.Library.domain.rest.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
}
