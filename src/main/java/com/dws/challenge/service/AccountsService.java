package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AmountValidationException;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class AccountsService {


  private final AccountsRepository accountsRepository;

  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
  
  public void transferMoney(String accountFrom, String accountTo , Double amount ) {
      this.accountsRepository.balanaceTransfer(accountFrom, accountTo, amount);
  }
  
  public void amountValidation(Double amount) throws AmountValidationException
  {
	  Double result = Math.signum(amount);
	   
	    if (result.compareTo(1.0d) != 0) {
	      
	   throw new AmountValidationException("amount is negative number");
  }
  }
}
