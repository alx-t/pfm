package com.alxt.pfmservice.service;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.dto.AccountDTO;
import com.alxt.pfmservice.entity.mapper.AccountMapper;
import com.alxt.pfmservice.entity.payload.NewAccountPayload;
import com.alxt.pfmservice.entity.payload.UpdateAccountPayload;
import com.alxt.pfmservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public List<AccountDTO> findAllAccounts(String userId, String filter) {
        List<Account> result;

        if (filter != null && !filter.isBlank()) {
            result = accountRepository.findAllByUserIdAndTitleLikeIgnoreCase(userId, "%"+filter+"%");
        } else {
            result = accountRepository.findAllByUserId(userId);
        }

        return result
                .stream()
                .map(accountMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public AccountDTO createAccount(String userId, NewAccountPayload payload) {
        return accountMapper.toDTO(
                accountRepository.save(
                    new Account(
                        null,
                        payload.title(),
                        payload.accountType(),
                        payload.currency(),
                        payload.amountCurrency(),
                        payload.amountCurrency(), // TODO: сделать пересчет в валюте
                        userId
                    )
                )
        );
    }

    @Override
    public Optional<AccountDTO> findAccount(String userId, Long accountId) {
        return Optional.ofNullable(accountMapper.toDTO(
                accountRepository.findByUserIdAndId(userId, accountId).orElse(null)
        ));
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
