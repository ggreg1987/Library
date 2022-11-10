package io.github.ggreg1987.Library.service;

import io.github.ggreg1987.Library.businessRule.BusinessException;
import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.entities.Loan;
import io.github.ggreg1987.Library.domain.repository.LoanRepository;
import io.github.ggreg1987.Library.domain.rest.dto.LoanFilterDTO;
import io.github.ggreg1987.Library.domain.rest.service.LoanService;
import io.github.ggreg1987.Library.domain.rest.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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

    public static Loan createNewLoan() {
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

        verify(loanRepository, times(1)).findById(id);

    }

    @Test
    @DisplayName("Should update a loan.")
    public void updateLoanTest() {

        Long id = 1L;

        var loan = createNewLoan();
        loan.setId(id);
        loan.setReturned(true);

        when(loanRepository.save(loan)).thenReturn(loan);

        var updatedLoan = service.update(loan);

        assertThat(updatedLoan.getReturned()).isTrue();
        verify(loanRepository,times(1)).save(loan);

    }

    @Test
    @DisplayName("Should filter a loan  by properties")
    public void findLoanTest() {
        var loan = createNewLoan();
        loan.setId(1L);

        LoanFilterDTO loanDTO = LoanFilterDTO
                .builder().customer("Gabriel")
                .isbn("54321").build();

        PageRequest pageRequest = PageRequest.of(0,10);
        List<Loan> list = Arrays.asList(loan);

        Page<Loan> page = new PageImpl<Loan>(list, pageRequest,list.size());
        when(loanRepository.findByBookIsbnOrCustomer(Mockito.anyString(),
                Mockito.anyString(),Mockito.any(Pageable.class)))
                .thenReturn(page);

        Page<Loan> result = service.find(loanDTO, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
}
