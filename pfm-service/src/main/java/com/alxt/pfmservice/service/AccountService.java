package com.alxt.pfmservice.service;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.payload.NewAccountPayload;
import com.alxt.pfmservice.entity.payload.UpdateAccountPayload;

import java.util.Optional;

// TODO перегнать в ДТО-шки
public interface AccountService {

    Iterable<Account> findAllAccounts(String filter);

    Account createAccount(NewAccountPayload payload);

    Optional<Account> findAccount(Integer accountId);

    void updateAccount(Integer accountId, UpdateAccountPayload payload);

    void deleteAccount(Integer accountId);
}
