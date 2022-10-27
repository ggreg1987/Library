package io.github.ggreg1987.Library.repository;

import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository repository;

    String isbn = "12345";

    public static Book createNewBook(String isbn) {
        return Book.builder()
                .title("My Book")
                .author("Gabriel")
                .isbn(isbn)
                .build();
    }

    @Test
    @DisplayName("Should return true when exists a book with isbn informed.")
    public void returnTrueWhenIsbExists() {
        String isbn = "53221";

        var book = createNewBook(isbn);
        testEntityManager.persist(book);

        boolean exists = repository.existsByIsbn(isbn);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should save a book.")
    public void saveBookTest() {

        var book = createNewBook(isbn);

        var bookSaved = repository.save(book);

        assertThat(bookSaved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should delete a book.")
    public void deleteBookTest() {

        var book = createNewBook(isbn);
        testEntityManager.persist(book);

        var foundBook = testEntityManager.find(Book.class, book.getId());

        repository.delete(foundBook);

        var deletedBook = testEntityManager.find(Book.class, book.getId());

        assertThat(deletedBook).isNull();
    }
}
