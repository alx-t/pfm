package com.alxt.pfmservice.service;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.dto.AccountDTO;
import com.alxt.pfmservice.entity.payload.NewAccountPayload;
import com.alxt.pfmservice.entity.payload.UpdateAccountPayload;

import java.util.List;
import java.util.Optional;

// TODO перегнать в ДТО-шки
public interface AccountService {

    List<AccountDTO> findAllAccounts(String userId, String filter);

    AccountDTO createAccount(String userId, NewAccountPayload payload);

    Optional<AccountDTO> findAccount(String userId, Long accountId);

    void updateAccount(String userId, Long accountId, UpdateAccountPayload payload);

    void deleteAccount(String userId, Long accountId);
}
