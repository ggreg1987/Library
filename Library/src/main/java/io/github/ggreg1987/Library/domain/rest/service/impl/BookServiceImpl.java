package io.github.ggreg1987.Library.domain.rest.service.impl;

import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {


    private BookReporitory repository;

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }
}
