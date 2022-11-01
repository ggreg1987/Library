package io.github.ggreg1987.Library.domain.rest.service.impl;

import io.github.ggreg1987.Library.businessRule.BusinessException;
import io.github.ggreg1987.Library.domain.entities.Loan;
import io.github.ggreg1987.Library.domain.repository.LoanRepository;
import io.github.ggreg1987.Library.domain.rest.dto.LoanFilterDTO;
import io.github.ggreg1987.Library.domain.rest.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository repository;
    @Override
    public Loan save(Loan loan) {
        if(repository.existsByBookAndNotReturned(loan.getBook())) {
            throw new BusinessException("Book already loaned.");
        }
        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable) {
        return repository.findByBookIsbnOrCustomer(filterDTO.getIsbn(),filterDTO.getCustomer(),pageable);
    }
}
