package io.github.ggreg1987.Library.domain.rest.controller;

import io.github.ggreg1987.Library.domain.rest.dto.BookDTO;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;
    public final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    public BookDTO create(@RequestBody BookDTO dto) {

        return dto;
    }
}
