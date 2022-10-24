package io.github.ggreg1987.Library.domain.rest.controller;

import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.entities.Loan;
import io.github.ggreg1987.Library.domain.rest.dto.LoanDTO;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import io.github.ggreg1987.Library.domain.rest.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(CREATED)
    public Long create(@RequestBody LoanDTO dto) {
       var book = bookService.getBookByIsbn(dto.getIsbn()).get();
       Loan entity = Loan.builder()
               .book(book)
               .customer(dto.getCustomer())
               .loanDate(LocalDate.now()).build();

        entity= loanService.save(entity);
        return entity.getId();
    }

}
