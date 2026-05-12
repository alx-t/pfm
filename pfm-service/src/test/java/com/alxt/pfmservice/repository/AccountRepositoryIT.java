package com.alxt.pfmservice.repository;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql("/sql/accounts.sql")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryIT {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findAllByTitleLikeIgnoreCase_ReturnsFilteredProductsList() {
        // given
        var filter = "%wallet%";

        // when
        var accounts = accountRepository.findAllByTitleLikeIgnoreCase(filter);

        // then
        assertEquals(List.of(new Account(
                1L, "Wallet", AccountType.WALLET,
                Currency.RUB, new BigDecimal("1000.000000"), new BigDecimal("1000.000000"))), accounts);
    }
}
