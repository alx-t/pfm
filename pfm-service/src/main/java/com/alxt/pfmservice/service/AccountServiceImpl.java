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
    public Iterable<Account> findAllAccounts(String userId, String filter) {
        if (filter != null && !filter.isBlank()) {
            return accountRepository.findAllByUserIdAndTitleLikeIgnoreCase(userId, "%"+filter+"%");
        } else {
            return accountRepository.findAllByUserId(userId);
        }
    }

    @Override
    @Transactional
    public Account createAccount(String userId, NewAccountPayload payload) {

        return accountRepository.save(
                new Account(
                    null,
                    payload.title(),
                    payload.accountType(),
                    payload.currency(),
                    payload.amountCurrency(),
                    payload.amountCurrency(), // TODO: сделать пересчет в валюте
                    userId
                )
        );
    }

    @Override
    public Optional<Account> findAccount(String userId, Long accountId) {
        return accountRepository.findByUserIdAndId(userId, accountId);
    }

    // TODO: определиться что обновляем
    @Transactional
    @Override
    public void updateAccount(String userId, Long accountId, UpdateAccountPayload payload) {
        accountRepository.findByUserIdAndId(userId, accountId)
                .ifPresentOrElse(account -> {
                    account.setTitle(payload.title());
//                    account.setAccountType(payload.accountType());
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
    public void deleteAccount(String userId, Long accountId) {
        accountRepository.deleteByUserIdAndId(userId, accountId);
    }
}
