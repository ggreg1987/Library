package io.github.ggreg1987.Library.domain.rest.controller;

import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.entities.Loan;
import io.github.ggreg1987.Library.domain.rest.dto.BookDTO;
import io.github.ggreg1987.Library.domain.rest.dto.LoanDTO;
import io.github.ggreg1987.Library.domain.rest.dto.LoanFilterDTO;
import io.github.ggreg1987.Library.domain.rest.dto.ReturnedLoanDTO;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import io.github.ggreg1987.Library.domain.rest.service.LoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Api
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;

    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Create a Loan")
    public Long create(@RequestBody LoanDTO dto) {
       var book = bookService.getBookByIsbn(dto.getIsbn())
               .orElseThrow(() ->
                       new ResponseStatusException(BAD_REQUEST,
                               "Book nor found for passed isbn."));
       Loan entity = Loan.builder()
               .book(book)
               .customer(dto.getCustomer())
               .loanDate(LocalDate.now()).build();

        entity= loanService.save(entity);
        return entity.getId();
    }

    @PatchMapping("{id}")
    @ApiOperation("Obtains a Loans details by id")
    public void returnBook(@PathVariable Long id,
                           @RequestBody ReturnedLoanDTO dto) {
        var loan = loanService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        loan.setReturned(dto.getReturned());
        loanService.update(loan);
    }

    @GetMapping
    @ApiOperation("Obtain all Loans.")
    public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageable) {
        Page<Loan> result = loanService.find(dto, pageable);
        List<LoanDTO> loansCollect = result
                .getContent()
                .stream()
                .map(entity -> {
                    var book = entity.getBook();
                    BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
                }).collect(Collectors.toList());
        return new PageImpl<>(loansCollect,pageable,result.getTotalElements());
    }

}
