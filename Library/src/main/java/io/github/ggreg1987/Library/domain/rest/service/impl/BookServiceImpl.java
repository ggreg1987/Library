package io.github.ggreg1987.Library.domain.rest.service.impl;

import io.github.ggreg1987.Library.businessRule.BusinessException;
import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.repository.BookRepository;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {


    private final BookRepository repository;

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Duplicated Isbn");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Book book) {

    }
}
