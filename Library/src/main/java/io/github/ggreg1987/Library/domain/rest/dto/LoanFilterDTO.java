package io.github.ggreg1987.Library.domain.rest.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanFilterDTO {

    private String isbn;
    private String customer;
}
