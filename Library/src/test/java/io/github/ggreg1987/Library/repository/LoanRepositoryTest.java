package io.github.ggreg1987.Library.repository;

import io.github.ggreg1987.Library.domain.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        var book = createNewBook("12345");
        entityManager.persist(book);
        entityManager.persist(loan);

        repository.existsByBookAndNotReturned(book);

    }
}
