package io.github.ggreg1987.Library.domain.rest.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

    private Long id;
    @NotEmpty
    private String isbn;
    @NotEmpty
    private String customer;
    private BookDTO book;
    @NotEmpty
    private String email;
}
