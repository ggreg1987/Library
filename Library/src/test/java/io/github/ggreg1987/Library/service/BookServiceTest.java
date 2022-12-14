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
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

        when(repository.save(book)).thenReturn(
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
        when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

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
        when(repository.findById(id)).thenReturn(Optional.of(book));

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
        when(repository.findById(id)).thenReturn(Optional.empty());

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

    @Test
    @DisplayName("Should update a book.")
    public void updateBookTest() {
        Long id = 1L;

        var oldBook = Book.builder().id(id).build();
        var update = createValidBook();
        update.setId(id);

        when(repository.save(oldBook)).thenReturn(update);
        var book = service.update(oldBook);

        assertThat(book.getId()).isEqualTo(update.getId());
        assertThat(book.getAuthor()).isEqualTo(update.getAuthor());
        assertThat(book.getTitle()).isEqualTo(update.getTitle());

    }

    @Test
    @DisplayName("Cant update a null book or without id.")
    public void updateInvalidBookTest() {
        var book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.update(book));

        Mockito.verify(repository,Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Should filter a book by properties")
    public void findBookTest() {
        var book = createValidBook();

        PageRequest pageRequest = PageRequest.of(0,10);
        List<Book> list = Arrays.asList(book);

        Page<Book> page = new PageImpl<Book>(list, pageRequest,1);
        when(repository.findAll(Mockito.any(Example.class), Mockito.any(Pageable.class)))
                .thenReturn(page);

        Page<Book> result = service.find(book, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should find a book by isbn.")
    public void getBookByIsbnTest() {

        String isbn = "1234";
        var book = Book.builder().id(1L).isbn(isbn).build();
        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        Optional<Book> bookFound = service.getBookByIsbn(isbn);

        assertThat(bookFound.isPresent()).isTrue();
        assertThat(bookFound.get().getId()).isEqualTo(book.getId());
        assertThat(bookFound.get().getIsbn()).isEqualTo(book.getIsbn());

        verify(repository,times(1)).findByIsbn(isbn);
    }
}
