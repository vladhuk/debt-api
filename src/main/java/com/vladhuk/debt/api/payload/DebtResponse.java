package com.vladhuk.debt.api.payload;

import com.vladhuk.debt.api.model.Debt;
import com.vladhuk.debt.api.model.User;
import lombok.Getter;

import java.util.Objects;

@Getter
public class DebtResponse {

    private User partner;
    private Float balance;

    private DebtResponse() {}

    public static DebtResponse create(Debt debt, Long currentUserId) {
        final DebtResponse debtResponse = new DebtResponse();

        if (Objects.equals(debt.getCreditor().getId(), currentUserId)) {
            debtResponse.partner = debt.getBorrower();
            debtResponse.balance = debt.getBalance();
        } else {
            debtResponse.partner = debt.getCreditor();
            debtResponse.balance = -debt.getBalance();
        }

        return debtResponse;
    }

}
