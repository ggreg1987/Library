package io.github.ggreg1987.Library.domain.rest.service.impl;

import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.repository.BookRepository;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {


    private final BookRepository repository;

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }
}
