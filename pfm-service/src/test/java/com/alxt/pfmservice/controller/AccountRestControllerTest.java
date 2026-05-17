package com.alxt.pfmservice.controller;

import com.alxt.pfmservice.controller.v1.AccountRestController;
import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;
import com.alxt.pfmservice.entity.payload.UpdateAccountPayload;
import com.alxt.pfmservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountRestControllerTest {

    @Mock
    AccountService accountService;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    AccountRestController controller;

    String userId = "User_1";
    JwtAuthenticationToken jwt = new JwtAuthenticationToken(Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("sub", userId)
            .build());

    @Test
    void getProduct_ProductExists_ReturnsProduct() {
        // given
        var account = new Account(1L, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), userId);

        doReturn(Optional.of(account)).when(accountService).findAccount(userId, 1L);

        // when
        var result = this.controller.getAccount(1L, jwt);

        // then
        assertEquals(account, result);
    }

    @Test
    void getAccount_AccountDoesNotExist_ThrowsNoSuchElementException() {
        // given

        // when
        var exception = assertThrows(NoSuchElementException.class, () -> this.controller.getAccount(1L, jwt));

        // then
        assertEquals("errors.account.not_found", exception.getMessage());
    }

    @Test
    void findAccount_ReturnsAccount() {
        // given
        var account = new Account(1L, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000), userId);

        // when
        var result = this.controller.findAccount(account);

        // then
        assertEquals(account, result);
    }

    @Test
    void updateAccount_RequestIsValid_ReturnsNoContent() throws BindException {
        // given
        var payload = new UpdateAccountPayload(
                "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000)
        );
        var bindingResult = new MapBindingResult(Map.of(), "payload");

        // when
        var result = this.controller.updateAccount(1L, payload, bindingResult, jwt);

        // then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(accountService).updateAccount(userId, 1L, new UpdateAccountPayload(
                "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000)
        ));
    }

    @Test
    void updateAccount_RequestIsInvalid_ReturnsBadRequest() {
        // given
        var payload = new UpdateAccountPayload(
                "  ", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000)
        );
        var bindingResult = new MapBindingResult(Map.of(), "payload");
        bindingResult.addError(new FieldError("payload", "title", "error"));

        // when
        var exception = assertThrows(BindException.class, () -> controller.updateAccount(1L, payload, bindingResult, jwt));

        // then
        assertEquals(List.of(new FieldError("payload", "title", "error")), exception.getAllErrors());
        verifyNoInteractions(accountService);
    }

    @Test
    void deleteAccount_ReturnsNoContent() {
        // given

        // when
        var result = controller.deleteAccount(1L, jwt);

        // then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(accountService).deleteAccount(userId, 1L);
    }

    @Test
    void handleNoSuchElementException_ReturnsNotFound() {
        // given
        var exception = new NoSuchElementException("error_code");
        var locale = Locale.of("ru");

        doReturn("error details").when(this.messageSource)
                .getMessage("error_code", new Object[0], "error_code", Locale.of("ru"));

        // when
        var result = this.controller.handleNoSuchElementException(exception, locale);

        // then
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertInstanceOf(ProblemDetail.class, result.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getBody().getStatus());
        assertEquals("error details", result.getBody().getDetail());

        verifyNoInteractions(accountService);
    }
}