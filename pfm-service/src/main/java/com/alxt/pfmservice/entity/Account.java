package com.alxt.pfmservice.entity;

import com.alxt.pfmservice.entity.dto.AccountDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @Column(name = "acc_type")
    @NotNull
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    @NotNull
    private Currency currency;

    @Column(name = "amount", precision = 15, scale = 6)
    @NotNull
    private BigDecimal amount;

    @Column(name = "amount_curr", precision = 15, scale = 6)
    @NotNull
    private BigDecimal amountCurrency;

    public AccountDTO toDTO() {
        return new AccountDTO(getId(), getTitle(), getAccountType(), getCurrency(), getAmount(), getAmountCurrency());
    }
}
