package io.github.ggreg1987.Library.domain.rest.service;

import io.github.ggreg1987.Library.domain.entities.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);
    Optional<Book> getById(Long id);
    void delete(Book book);
    Book update(Book book);
}
