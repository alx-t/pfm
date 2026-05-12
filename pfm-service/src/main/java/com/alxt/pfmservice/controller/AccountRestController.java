package com.alxt.pfmservice.controller;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.payload.UpdateAccountPayload;
import com.alxt.pfmservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/accounts/{accountId}")
public class AccountRestController {

    private final AccountService accountService;
    private final MessageSource messageSource;

    @ModelAttribute("account")
    public Account getAccount(@PathVariable Integer accountId) {
        return accountService
                .findAccount(accountId)
                .orElseThrow(() -> new NoSuchElementException("errors.account.not_found"));
    }

    @GetMapping
    public Account findAccount(@ModelAttribute("account") Account account) {
        return account;
    }

    @PatchMapping
    public ResponseEntity<?> updateAccount(
            @PathVariable Integer accountId,
            @Valid @RequestBody UpdateAccountPayload payload,
            BindingResult bindingResult
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            accountService.updateAccount(accountId, payload);
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(
            NoSuchElementException exception, Locale locale
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        messageSource.getMessage(
                                exception.getMessage(),
                                new Object[0],
                                exception.getMessage(),
                                locale
                        )
                ));
    }
}
