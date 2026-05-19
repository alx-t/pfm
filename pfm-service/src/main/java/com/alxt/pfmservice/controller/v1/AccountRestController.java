package com.alxt.pfmservice.controller.v1;

import com.alxt.pfmservice.entity.dto.AccountDTO;
import com.alxt.pfmservice.entity.payload.UpdateAccountPayload;
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

import java.util.NoSuchElementException;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/accounts/{accountId}")
@Tag(
        name = "Счет",
        description = "Ручки для работы со счетом"
)
public class AccountRestController {

    private final AccountService accountService;

    @ModelAttribute("account")
    public AccountDTO getAccount(
            @Parameter(description = "Номер счета") @PathVariable Long accountId,
            JwtAuthenticationToken auth
    ) {
        return accountService
                .findAccount(UserUtils.getUserId(auth), accountId)
                .orElseThrow(() -> new NoSuchElementException("errors.account.not_found"));
    }

    @GetMapping
    @Operation(summary = "Получить информацию о счете")
    public AccountDTO findAccount(@ModelAttribute("account") AccountDTO accountDto) {
        return accountDto;
    }

    @PatchMapping
    @Operation(summary = "Изменить счет")
    public ResponseEntity<?> updateAccount(
            @Parameter(description = "Номер счета") @PathVariable Long accountId,
            @Parameter(description = "Данные обновленного счета") @Valid @RequestBody UpdateAccountPayload payload,
            BindingResult bindingResult,
            JwtAuthenticationToken auth
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            accountService.updateAccount(UserUtils.getUserId(auth), accountId, payload);
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    @Operation(summary = "Удалить счет")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId, JwtAuthenticationToken auth) {
        accountService.deleteAccount(UserUtils.getUserId(auth), accountId);
        return ResponseEntity.noContent().build();
    }
}
