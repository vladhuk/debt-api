package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.Debt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DebtRepository extends PagingAndSortingRepository<Debt, Long> {

    List<Debt> findAllByCreditorIdOrBorrowerId(Long creditorId, Long borrowerId);

    List<Debt> findAllByCreditorIdOrBorrowerId(Long creditorId, Long borrowerId, Pageable pageable);

}
