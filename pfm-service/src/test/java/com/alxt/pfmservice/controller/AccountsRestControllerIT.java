package com.alxt.pfmservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class AccountsRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql("/sql/accounts.sql")
    void findAccounts_ReturnsAccountsList() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts")
                .param("filter", "account");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {"id": 2, "title": "Account 2", "accountType": "WALLET", "currency": "RUB",
                                      "amount": 2000.0, "amountCurrency": 2000.0},
                                    {"id": 4, "title": "Account 4", "accountType": "DEPOSIT", "currency": "RUB",
                                      "amount": 4000.0, "amountCurrency": 4000.0}
                                ]""")
                );
    }

//    @Test
//    @Sql("/sql/accounts.sql")
//    void findAccounts_UserIsNotAuthorized_ReturnsForbidden() throws Exception {}

    @Test
    void createAccount_RequestIsValid_ReturnsNewAccount() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "Account 1", "accountType": 0, "currency": "RUB", "amountCurrency": 1000}""");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "http://localhost/api/v1/accounts/1"),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "id": 1,
                                    "title": "Account 1",
                                    "accountType": "WALLET",
                                    "currency": "RUB",
                                     "amount": 1000.0,
                                     "amountCurrency": 1000.0
                                }"""));
    }

    // TODO: посмотреть ошибки
    @Test
    void createAccount_RequestIsInvalid_ReturnsProblemDetail() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "  ", "accountType": 0, "currency": "RUB", "amountCurrency": 1000}""");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        content().json("""
                                {
                                    "errors": [
                                        "Account title should be min=3 and max=50"
                                    ]
                                }"""));
    }

//    @Test
//    void createAccount_UserIsNotAuthorized_ReturnsForbidden() throws Exception {}
}
