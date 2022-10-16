package io.github.ggreg1987.Library.service;

import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

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

        

    }
}
