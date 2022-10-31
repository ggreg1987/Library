package io.github.ggreg1987.Library.domain.rest.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

    private Long id;
    private String isbn;
    private String customer;
    private BookDTO book;
}
