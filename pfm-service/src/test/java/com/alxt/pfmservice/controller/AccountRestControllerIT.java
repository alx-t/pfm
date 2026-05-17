package com.alxt.pfmservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class AccountRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql("/sql/accounts.sql")
    void findAccount_AccountExists_ReturnsAccountsList() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts/1")
                .with(jwt()
                        .jwt(jwt -> jwt.claim("sub", "User_1"))
                );

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "id": 1,
                                    "title": "Wallet",
                                    "accountType": "WALLET",
                                    "currency": "RUB",
                                    "amount": 1000.0,
                                    "amountCurrency": 1000.0
                                }""")
                );
    }

    @Test
    void findAccount_AccountDoesNotExist_ReturnsNotFound() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts/1")
                .with(jwt()
                        .jwt(jwt -> jwt.claim("sub", "User_1"))
                );

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    @Sql("/sql/accounts.sql")
    void findAccount_UserIsNotAuthorized_ReturnsUnauthorized() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts/1");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isUnauthorized()
                );
    }

    @Test
    @Sql("/sql/accounts.sql")
    void updateAccount_RequestIsValid_ReturnsNoContent() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.patch("/api/v1/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "New Account",
                            "accountType": 0,
                            "currency": "RUB",
                            "amountCurrency": 1000
                        }""")
                .with(jwt()
                        .jwt(jwt -> jwt.claim("sub", "User_1"))
                );

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @Test
    @Sql("/sql/accounts.sql")
    void updateAccount_RequestIsInvalid_ReturnsBadRequest() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.patch("/api/v1/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": " ",
                            "accountType": 0,
                            "currency": "RUB",
                            "amountCurrency": 1000
                        }""")
                .with(jwt()
                        .jwt(jwt -> jwt.claim("sub", "User_1"))
                );

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        content().json("""
                                {
                                    "errors": ["Account title should be min=3 and max=50"]
                                }""")
                );
    }

    @Test
    void updateAccount_AccountDoesNotExist_ReturnsNotFound() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.patch("/api/v1/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "New Account",
                            "accountType": 0,
                            "currency": "RUB",
                            "amountCurrency": 1000
                        }""")
                .with(jwt()
                        .jwt(jwt -> jwt.claim("sub", "User_1"))
                );

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    @Sql("/sql/accounts.sql")
    void deleteAccount_AccountExists_ReturnsNoContent() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.delete("/api/v1/accounts/1")
                .with(jwt()
                        .jwt(jwt -> jwt.claim("sub", "User_1"))
                );

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @Test
    void deleteAccount_AccountDoesNotExist_ReturnsNotFound() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.delete("/api/v1/accounts/1")
                .with(jwt()
                        .jwt(jwt -> jwt.claim("sub", "User_1"))
                );

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNotFound()
                );
    }
}
