package com.alxt.pfmservice.repository;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testSaveAccount() {
        Account account = new Account();
        account.setTitle("Test account");
        account.setAccountType(AccountType.WALLET);
        account.setCurrency(Currency.RUB);
        account.setAmount(BigDecimal.valueOf(1000));
        account.setAmountCurrency(BigDecimal.valueOf(1000));
        account.setUserId("TestUser");

        Account savedAccount = accountRepository.save(account);

        assertNotNull(savedAccount.getId());
        assertEquals("Test account", savedAccount.getTitle());
    }
}