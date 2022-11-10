package io.github.ggreg1987.Library.domain.repository;

import io.github.ggreg1987.Library.domain.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {

    boolean existsByIsbn(String isbn);

    Optional<Book> findByIsbn(String isbn);
}
