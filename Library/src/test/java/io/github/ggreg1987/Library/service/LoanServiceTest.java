package io.github.ggreg1987.Library.service;

import io.github.ggreg1987.Library.businessRule.BusinessException;
import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.entities.Loan;
import io.github.ggreg1987.Library.domain.repository.LoanRepository;
import io.github.ggreg1987.Library.domain.rest.service.LoanService;
import io.github.ggreg1987.Library.domain.rest.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService service;

    @MockBean
    LoanRepository loanRepository;

    @BeforeEach
    public void setUp() {
        this.service = new LoanServiceImpl(loanRepository);
    }

    private Loan createNewLoan() {
        var book = Book.builder().id(1L)
                .author("Gregorio")
                .title("The Avengers").isbn("12345").build();

        return Loan.builder()
                .customer("Gabriel")
                .book(book).loanDate(LocalDate.now()).build();

    }

    @Test
    @DisplayName("Should save a loan.")
    public void saveLoanTest() {

        var loan = createNewLoan();
        var book = createNewLoan().getBook();
        var loanReturn = Loan.builder()
                .id(1L).customer("Gabriel")
                .book(book).loanDate(LocalDate.now()).build();
        when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(false);
        when(loanRepository.save(loan)).thenReturn(loanReturn);

        var loanSaved = service.save(loan);

        assertThat(loanSaved.getId()).isEqualTo(loanReturn.getId());
        assertThat(loanSaved.getBook().getId()).isEqualTo(loanReturn.getBook().getId());
        assertThat(loanSaved.getBook().getIsbn()).isEqualTo(loanReturn.getBook().getIsbn());
    }

    @Test
    @DisplayName("Should show an error when try to save a book loaned.")
    public void errorToSaveLoanTest() {

        var book = Book.builder().id(1L)
                .author("Gregorio")
                .title("The Avengers").isbn("12345").build();

        var loan = Loan.builder()
                .customer("Gabriel")
                .book(book).loanDate(LocalDate.now()).build();

        when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(true);
        Throwable throwable = catchThrowable(() -> service.save(loan));

        assertThat(throwable)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned.");

        verify(loanRepository,never()).save(loan);
    }

    @Test
    @DisplayName("Should show details of the loan by id")
    public void getLoanDetailsTest() {

        Long id = 1L;

        var loan = createNewLoan();
        loan.setId(id);

        Mockito.when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

        Optional<Loan> result = service.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

    }
}
