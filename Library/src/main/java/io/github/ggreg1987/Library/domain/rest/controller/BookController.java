package io.github.ggreg1987.Library.domain.rest.controller;

import io.github.ggreg1987.Library.businessRule.BusinessException;
import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.rest.dto.BookDTO;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import io.github.ggreg1987.Library.exceptions.ApiErrors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;
    public final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        var savedBook = service.save(entity);
        return modelMapper.map(savedBook,BookDTO.class);
    }
    @GetMapping("{id}")
    @ResponseStatus(OK)
    public BookDTO getById(@PathVariable Long id) {
        var book = service.getById(id).get();
        return modelMapper.map(book,BookDTO.class);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleBusinessExceptions(BusinessException ex) {
        return new ApiErrors(ex);
    }
}
