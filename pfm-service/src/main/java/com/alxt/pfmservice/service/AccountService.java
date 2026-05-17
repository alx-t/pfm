package com.alxt.pfmservice.service;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.payload.NewAccountPayload;
import com.alxt.pfmservice.entity.payload.UpdateAccountPayload;

import java.util.Optional;

// TODO перегнать в ДТО-шки
public interface AccountService {

    Iterable<Account> findAllAccounts(String userId, String filter);

    Account createAccount(String userId, NewAccountPayload payload);

    Optional<Account> findAccount(String userId, Long accountId);

    void updateAccount(String userId, Long accountId, UpdateAccountPayload payload);

    void deleteAccount(String userId, Long accountId);
}
