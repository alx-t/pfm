package com.alxt.pfmservice.service;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.payload.NewAccountPayload;
import com.alxt.pfmservice.entity.payload.UpdateAccountPayload;
import com.alxt.pfmservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Iterable<Account> findAllAccounts(String filter) {
        if (filter != null && !filter.isBlank()) {
            return accountRepository.findAllByTitleLikeIgnoreCase("%"+filter+"%");
        } else {
            return accountRepository.findAll();
        }
    }

    @Override
    @Transactional
    public Account createAccount(NewAccountPayload payload) {
        return accountRepository.save(payload.toAccount());
    }

    @Override
    public Optional<Account> findAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }

    // TODO: определиться что обновляем
    @Transactional
    @Override
    public void updateAccount(Long accountId, UpdateAccountPayload payload) {
        accountRepository.findById(accountId)
                .ifPresentOrElse(account -> {
                    account.setTitle(payload.title());
                    account.setAccountType(payload.accountType());
//                    account.setCurrency(payload.currency());
//                    account.setAmount(payload.amountCurrency());
//                    account.setAmountCurrency(payload.amountCurrency());
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    // TODO: подумать о проверке остатков и пр
    @Override
    @Transactional
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }
}
