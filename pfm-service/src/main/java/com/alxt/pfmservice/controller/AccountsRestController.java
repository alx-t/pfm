package com.alxt.pfmservice.controller;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.payload.NewAccountPayload;
import com.alxt.pfmservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/accounts")
public class AccountsRestController {

    private final AccountService accountService;

    @GetMapping
    public Iterable<Account> findAccounts(
            @RequestParam(name = "filter", required = false) String filter
    ) {
        return accountService.findAllAccounts(filter);
    }

    @PostMapping
    public ResponseEntity<?> createAccount(
            @Valid @RequestBody NewAccountPayload payload,
            BindingResult bindingResult,
            UriComponentsBuilder uriComponentsBuilder
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Account account = accountService.createAccount(payload);
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("api/v1/accounts/{accountId}")
                            .build(Map.of("accountId", account.getId())))
                    .body(account);
        }
    }
}