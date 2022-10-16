package io.github.ggreg1987.Library.service;

import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.repository.BookRepository;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import io.github.ggreg1987.Library.domain.rest.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;
    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save a book.")
    public void saveBookTest() {

        Book book = Book
                .builder()
                .author("Gregorio")
                .title("Somethings")
                .isbn("54321")
                .build();
        var savedBook = service.save(book);

        assertThat(savedBook.getId()).isEqualTo(1L);

    }
}
