package com.alxt.pfmservice.controller;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;
import com.alxt.pfmservice.entity.payload.NewAccountPayload;
import com.alxt.pfmservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountsRestControllerTest {

    @Mock
    AccountService accountService;

    @InjectMocks
    AccountsRestController controller;

    @Test
    void findAccounts_ReturnsAccountsList() {
        // given
        var filter = "account";
        doReturn(
                List.of(
                        new Account(1, "Account 1", AccountType.WALLET,
                            Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)),
                        new Account(2, "Account 2", AccountType.WALLET,
                            Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)))
        ).when(accountService).findAllAccounts("account");

        // when
        var result = controller.findAccounts(filter);

        // then
        assertEquals(
                List.of(
                        new Account(1, "Account 1", AccountType.WALLET,
                        Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)),
                        new Account(2, "Account 2", AccountType.WALLET,
                        Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000))
                ), result);
    }

    @Test
    void createAccount_RequestIsValid_ReturnsNoContent() throws BindException {
        // given
        var payload = new NewAccountPayload(
                "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000)
        );
        var bindingResult = new MapBindingResult(Map.of(), "payload");
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        doReturn(new Account(1, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)))
                .when(accountService).createAccount(new NewAccountPayload(
                        "Account 1", AccountType.WALLET,
                        Currency.RUB, BigDecimal.valueOf(1000)
                ));

        // when
        var result = controller.createAccount(payload, bindingResult, uriComponentsBuilder);

        // then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(URI.create("http://localhost/api/v1/accounts/1"), result.getHeaders().getLocation());
        assertEquals(new Account(1, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)
        ), result.getBody());

        verify(accountService).createAccount(payload);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    void createAccount_RequestIsInvalid_ReturnsBadRequest() {
        // given
        var payload = new NewAccountPayload("   ", null,null, BigDecimal.valueOf(1000));
        var bindingResult = new MapBindingResult(Map.of(), "payload");
        bindingResult.addError(new FieldError("payload", "title", "error"));
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        // when
        var exception = assertThrows(BindException.class,
                () -> controller.createAccount(payload, bindingResult, uriComponentsBuilder));

        // then
        assertEquals(List.of(new FieldError("payload", "title", "error")),
                exception.getAllErrors());
        verifyNoInteractions(accountService);
    }

    @Test
    void createAccount_RequestIsInvalidAndBindResultIsBindException_ReturnsBadRequest() {
        // given
        var payload = new NewAccountPayload("   ", null,null, BigDecimal.valueOf(1000));
        var bindingResult = new BindException(new MapBindingResult(Map.of(), "payload"));
        bindingResult.addError(new FieldError("payload", "title", "error"));
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        // when
        var exception = assertThrows(BindException.class,
                () -> controller.createAccount(payload, bindingResult, uriComponentsBuilder));

        // then
        assertEquals(List.of(new FieldError("payload", "title", "error")),
                exception.getAllErrors());
        verifyNoInteractions(accountService);
    }
}
