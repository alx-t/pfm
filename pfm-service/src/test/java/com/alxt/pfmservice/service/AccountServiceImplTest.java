package com.alxt.pfmservice.service;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;
import com.alxt.pfmservice.entity.payload.NewAccountPayload;
import com.alxt.pfmservice.entity.payload.UpdateAccountPayload;
import com.alxt.pfmservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountServiceImpl accountService;

    @Test
    void findAllAccounts_FilterIsNotSet_ReturnsAccountsList() {
        // given
        var accounts = LongStream.range(1, 4)
                .mapToObj(i -> new Account(
                        i, "Account №%d".formatted(i), AccountType.WALLET,
                        Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)))
                .toList();

        doReturn(accounts).when(accountRepository).findAll();

        // when
        var result = accountService.findAllAccounts(null);

        // then
        assertEquals(accounts, result);

        verify(accountRepository).findAll();
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void findAllAccounts_FilterIsSet_ReturnsFilteredAccountsList() {
        // given
        var accounts = LongStream.range(1, 4)
                .mapToObj(i -> new Account(
                        i, "Account №%d".formatted(i), AccountType.WALLET,
                        Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)))
                .toList();

        doReturn(accounts).when(accountRepository).findAllByTitleLikeIgnoreCase("%account%");

        // when
        var result = accountService.findAllAccounts("account");

        // then
        assertEquals(accounts, result);

        verify(accountRepository).findAllByTitleLikeIgnoreCase("%account%");
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void findAccount_AccountExists_ReturnsNotEmptyOptional() {
        // given
        var account = new Account(
                1L, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)
        );

        doReturn(Optional.of(account)).when(accountRepository).findById(1L);

        // when
        var result = accountService.findAccount(1L);

        // then
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(account, result.orElseThrow());

        verify(accountRepository).findById(1L);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void findAccount_AccountDoesNotExist_ReturnsEmptyOptional() {
        // given
        var account = new Account(
                1L, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)
        );

        // when
        var result = accountService.findAccount(1L);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(accountRepository).findById(1L);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void createAccount_ReturnsCreatedAccount() {
        // given
        NewAccountPayload payload = new NewAccountPayload(
                "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000)
        );

        doReturn(new Account(
                1L, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000))
        ).when(accountRepository).save(payload.toAccount());

        // when
        var result = accountService.createAccount(payload);

        // then
        assertEquals(new Account(1L, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)), result);

        verify(accountRepository).save(new Account(null, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void updateAccount_AccountExists_UpdatesAccount() {
        // given
        var accountId = 1L;
        var account = new Account(
                1L, "Account 1", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000)
        );
        UpdateAccountPayload payload = new UpdateAccountPayload(
                "Account 11", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(2000)
        );

        doReturn(Optional.of(account))
                .when(accountRepository).findById(1L);

        // when
        accountService.updateAccount(accountId, payload);

        // then
        verify(accountRepository).findById(accountId);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void updateAccount_AccountDoesNotExist_ThrowsNoSuchElementException() {
        // given
        var accountId = 1L;
        UpdateAccountPayload payload = new UpdateAccountPayload(
                "Account 11", AccountType.WALLET,
                Currency.RUB, BigDecimal.valueOf(2000)
        );

        // when
        assertThrows(NoSuchElementException.class, () -> accountService
                .updateAccount(accountId, payload));

        // then
        verify(accountRepository).findById(accountId);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void deleteAccount_DeletesAccount() {
        // given
        var accountId = 1L;

        // when
        accountService.deleteAccount(accountId);

        // then
        verify(accountRepository).deleteById(accountId);
        verifyNoMoreInteractions(accountRepository);
    }
}