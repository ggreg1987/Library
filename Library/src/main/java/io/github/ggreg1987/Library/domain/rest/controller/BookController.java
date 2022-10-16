package io.github.ggreg1987.Library.domain.rest.controller;

import io.github.ggreg1987.Library.domain.rest.dto.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @PostMapping
    @ResponseStatus(CREATED)
    public BookDTO create(@RequestBody BookDTO dto) {

        return null;
    }
}
