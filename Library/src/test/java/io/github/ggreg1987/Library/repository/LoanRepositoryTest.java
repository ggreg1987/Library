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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import static io.github.ggreg1987.Library.repository.BookRepositoryTest.createNewBook;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    LoanRepository repository;

    @Autowired
    TestEntityManager entityManager;


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

    }

    public Loan createAndPersistLoan() {
        var book = createNewBook("12345");
        entityManager.persist(book);

        var loan = Loan.builder()
                .customer("Iron Man")
                .book(book)
                .loanDate(LocalDate.now())
                .build();
        entityManager.persist(loan);
        return loan;
    }
}
