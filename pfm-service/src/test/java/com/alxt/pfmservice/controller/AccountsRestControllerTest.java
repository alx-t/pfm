package com.alxt.pfmservice.controller;

import com.alxt.pfmservice.controller.v1.AccountsRestController;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

    String userId = "User_1";
    JwtAuthenticationToken jwt = new JwtAuthenticationToken(Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("sub", userId)
            .build());

    @Test
    void findAccounts_ReturnsAccountsList() {
        // given
        var filter = "account";

        doReturn(
                List.of(
                        new Account(1L, "Account 1", AccountType.WALLET,
                            Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), userId),
                        new Account(2L, "Account 2", AccountType.WALLET,
                            Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), userId))
        ).when(accountService).findAllAccounts(userId, "account");

        // when
        var result = controller.findAccounts(filter, jwt);

        // then
        assertEquals(
                List.of(
                        new Account(1L, "Account 1", AccountType.WALLET,
                        Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), userId),
                        new Account(2L, "Account 2", AccountType.WALLET,
                        Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), userId)
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

        doReturn(new Account(1L, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), userId))
                .when(accountService).createAccount(userId, new NewAccountPayload(
                        "Account 1", AccountType.WALLET,
                        Currency.RUB, BigDecimal.valueOf(1000)
                ));

        // when
        var result = controller.createAccount(payload, bindingResult, uriComponentsBuilder, jwt);

        // then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(URI.create("http://localhost/api/v1/accounts/1"), result.getHeaders().getLocation());
        assertEquals(new Account(1L, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), userId
        ), result.getBody());

        verify(accountService).createAccount(userId, payload);
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
                () -> controller.createAccount(payload, bindingResult, uriComponentsBuilder, jwt));

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
                () -> controller.createAccount(payload, bindingResult, uriComponentsBuilder, jwt));

        // then
        assertEquals(List.of(new FieldError("payload", "title", "error")),
                exception.getAllErrors());
        verifyNoInteractions(accountService);
    }
}
