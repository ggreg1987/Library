package io.github.ggreg1987.Library.repository;
import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.entities.Loan;
import io.github.ggreg1987.Library.domain.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import static io.github.ggreg1987.Library.repository.BookRepositoryTest.createNewBook;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    LoanRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public Loan createAndPersistLoan(LocalDate loanDate) {
        var book = createNewBook("12345");
        entityManager.persist(book);

        var loan = Loan.builder()
                .customer("Iron Man")
                .book(book)
                .loanDate(loanDate)
                .build();
        entityManager.persist(loan);
        return loan;
    }


    @Test
    @DisplayName("Should verifying if there is an unreturned loan")
    public void existsByBookAndNotReturnedTest() {
        var loan = createAndPersistLoan();
        Book book = loan.getBook();

        var  exists = repository.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Should search a loan by isbn Book or customer")
    public void findByBookIsbnOrCustomerTest() {

        Loan loan = createAndPersistLoan();
        Page<Loan> result = repository
                .findByBookIsbnOrCustomer(
                        "12345",
                        "Greg",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(loan);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);

    }

    @Test
    @DisplayName("Should obtain loans whose date is less than or " +
            "equals to three days ago and not returned")
    public void findByLoanDateLessThanAndNotReturned() {
        Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));

        List<Loan> result = repository
                .findByLoanDateLessThanAndNotReturned(LocalDate.now()
                        .minusDays(4));
        assertThat(result).hasSize(1).contains(loan);
    }




}
