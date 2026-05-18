package com.alxt.pfmservice.controller.v1;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.payload.NewAccountPayload;
import com.alxt.pfmservice.service.AccountService;
import com.alxt.pfmservice.utils.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/accounts")
@Tag(
        name = "Счета",
        description = "Ручки для работы со счетами"
)
public class AccountsRestController {

    private final AccountService accountService;

    // TODO: Вернуть DTO

    @GetMapping
    @Operation(summary = "Получить информацию о счетах")
    public Iterable<Account> findAccounts(
            @Parameter(description = "фильтр") @RequestParam(name = "filter", required = false) String filter,
            JwtAuthenticationToken auth
    ) {
        return accountService.findAllAccounts(UserUtils.getUserId(auth), filter);
    }

    @PostMapping
    @Operation(summary = "Создать новый счет")
    public ResponseEntity<?> createAccount(
            @Parameter(description = "Данные для создания нового счета") @Valid @RequestBody NewAccountPayload payload,
            BindingResult bindingResult,
            UriComponentsBuilder uriComponentsBuilder,
            JwtAuthenticationToken auth
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Account account = accountService.createAccount(UserUtils.getUserId(auth), payload);
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("api/v1/accounts/{accountId}")
                            .build(Map.of("accountId", account.getId())))
                    .body(account);
        }
    }
}