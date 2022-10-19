package io.github.ggreg1987.Library.domain.repository;

import io.github.ggreg1987.Library.domain.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {

    boolean existsByIsbn(String isbn);
}
