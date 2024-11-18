package com.dws.challenge.web;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AmountValidationException;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

    private final AccountsService accountsService;

    private final NotificationService notificationService;

    @Autowired
    public AccountsController(AccountsService accountsService
            , NotificationService notificationService) {
        this.accountsService = accountsService;
        this.notificationService = notificationService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
        log.info("Creating account {}", account);

        try {
            this.accountsService.createAccount(account);
        } catch (DuplicateAccountIdException daie) {
            return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        log.info("Retrieving account for id {}", accountId);
        return this.accountsService.getAccount(accountId);
    }

    @PostMapping("/moneyTransfer")
    public ResponseEntity<Object> moneyTransfer(@RequestParam("accountFrom") String accountFrom, @RequestParam("accountTo") String accountTo,
                                                @RequestParam("amount") Double amount) {
        try {
            accountsService.amountValidation(amount);
            log.info("transferring money amount {} from {} to {} ", amount, accountFrom, accountTo);
            accountsService.transferMoney(accountFrom, accountTo, amount);
        } catch (AmountValidationException amountValidationMessage) {
            return new ResponseEntity<>(amountValidationMessage.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
