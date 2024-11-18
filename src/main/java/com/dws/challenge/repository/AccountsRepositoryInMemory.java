package com.dws.challenge.repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AmountValidationException;
import com.dws.challenge.exception.DuplicateAccountIdException;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts;

    public AccountsRepositoryInMemory() {
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }

    @Override
    public void balanaceTransfer(String accountFrom, String accountTo, double amount) {
        double remainingBalanceFromAccount = accounts.get(accountFrom).getBalance().doubleValue() - amount;
        Double result = Math.signum(remainingBalanceFromAccount);

        if (result.compareTo(1.0d) != 0) {
            throw new AmountValidationException("we do not support overdrafts!");
        } else {
            double v = accounts.get(accountTo).getBalance().doubleValue() + amount;

        }
    }


}
