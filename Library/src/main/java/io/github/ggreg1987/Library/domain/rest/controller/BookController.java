package io.github.ggreg1987.Library.domain.rest.controller;

import io.github.ggreg1987.Library.businessRule.BusinessException;
import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.entities.Loan;
import io.github.ggreg1987.Library.domain.rest.dto.BookDTO;
import io.github.ggreg1987.Library.domain.rest.dto.LoanDTO;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import io.github.ggreg1987.Library.domain.rest.service.LoanService;
import io.github.ggreg1987.Library.exceptions.ApiErrors;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Api("Book API")
public class BookController {

    private final BookService service;
    public final ModelMapper modelMapper;

    public final LoanService loanService;

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Create a New Book")
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        var savedBook = service.save(entity);
        return modelMapper.map(savedBook,BookDTO.class);
    }
    @GetMapping("{id}")
    @ResponseStatus(OK)
    @ApiOperation("Obtains a Book details by id")
    public BookDTO getById(@PathVariable Long id) {

        return  service.getById(id)
                .map(book -> modelMapper.map(book,BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Deletes a Book by id")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Book Successfully deleted")
    })
    public void delete(@PathVariable Long id) {
        var book = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        service.delete(book);
    }
    @PutMapping("{id}")
    @ResponseStatus(OK)
    @ApiOperation("Updates a book")
    public BookDTO update(@PathVariable Long id,@RequestBody BookDTO dto ) {
        return  service.getById(id)
                .map(book -> {
                    book.setId(dto.getId());
                    service.update(book);
                    return modelMapper.map(book,BookDTO.class);
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping
    @ApiOperation("Find books by params")
    public Page<BookDTO> find(BookDTO dto , Pageable pageRequest) {
        var filter = modelMapper.map(dto,Book.class);
        Page<Book> result = service.find(filter, pageRequest);
        List<BookDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>(list,pageRequest,result.getTotalElements());

    }

    @GetMapping("{id}/loans")
    @ApiOperation("Get loans by id")
    public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable) {
        Book book = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        Page<Loan> result = loanService.getLoansByBook(book,pageable);

        List<LoanDTO> list = result.getContent()
                .stream()
                .map(loan -> {
                    Book loanBook = loan.getBook();
                    BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
                }).collect(Collectors.toList());
        return new PageImpl<LoanDTO>(list,pageable,result.getTotalElements());
    }
}
