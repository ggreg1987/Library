package io.github.ggreg1987.Library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Test
    @DisplayName("Should verifying if there is an unreturned loan")
    public void existsByBookAndNotReturnedTest() {

        entitiyManager.persist(book);
        entitiyManager.persist(loan);

        repository.existsByBookAndNotReturned(book);

    }
}
