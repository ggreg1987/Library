package io.github.ggreg1987.Library.domain.rest.service;

import io.github.ggreg1987.Library.domain.entities.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    Loan save(Loan any);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);
}
