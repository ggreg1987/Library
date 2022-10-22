package io.github.ggreg1987.Library.service;

import io.github.ggreg1987.Library.businessRule.BusinessException;
import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.repository.BookRepository;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import io.github.ggreg1987.Library.domain.rest.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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

    private Book createValidBook() {
        return Book
                .builder()
                .author("Gregorio")
                .title("Somethings")
                .isbn("54321")
                .build();
    }

    @Test
    @DisplayName("Should save a book.")
    public void saveBookTest() {
        var book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

        Mockito.when(repository.save(book)).thenReturn(
                Book.builder()
                        .id(1L)
                        .author("Gregorio")
                        .title("Somethings")
                        .isbn("54321")
                        .build()
        );
        var savedBook = service.save(book);

        assertThat(savedBook.getId()).isEqualTo(1L);

    }
    @Test
    @DisplayName("Should show an exception when to try save a duplicated ISBN.")
    public void cantSaveBook() {
        var book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Duplicated Isbn");

        Mockito.verify(repository,Mockito.never()).save(book);
    }

    @Test
    @DisplayName("should show a book.")
    public void getByIdTest() {
        Long id = 1L;
        var book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }
    @Test
    @DisplayName("Should show an exception when try find an id")
    public void bookNotFoundByIdTest() {
        Long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> book = service.getById(id);

        assertThat(book.isPresent()).isFalse();
    }
    @Test
    @DisplayName("Should delete a book.")
    public void deleteBookTest() {
        var book = Book
                .builder().id(1L).build();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book));

        Mockito.verify(repository,Mockito.times(1)).delete(book);
    }
    @Test
    @DisplayName("Cant delete a null book or without id.")
    public void deleteInvalidBookTest() {
        var book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.delete(book));

        Mockito.verify(repository,Mockito.never()).delete(book);
    }
}
