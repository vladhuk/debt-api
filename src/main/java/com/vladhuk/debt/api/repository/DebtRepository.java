package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.Debt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface DebtRepository extends PagingAndSortingRepository<Debt, Long> {

    List<Debt> findAllByCreditorIdOrBorrowerId(Long creditorId, Long borrowerId);

    List<Debt> findAllByCreditorIdOrBorrowerId(Long creditorId, Long borrowerId, Pageable pageable);

    Optional<Debt> findByCreditorIdOrBorrowerId(Long creditorId, Long borrowerId);

    Boolean existsByCreditorIdAndBorrowerId(Long creditorId, Long borrowerId);

    @Modifying
    @Query("DELETE FROM Debt WHERE id = ?1 and (creditor.id = ?2 or borrower.id = ?2)")
    void deleteById_And_CreditorOrBorrowerId(Long id, Long creditorOrBorrowerId);

}
