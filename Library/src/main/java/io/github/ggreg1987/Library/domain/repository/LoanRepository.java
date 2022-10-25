package io.github.ggreg1987.Library.domain.repository;

import io.github.ggreg1987.Library.domain.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long> {
}
