package com.lmartino.bank.domain.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;

public class AccountTransferTest {

    @Test
    public void canCreateAccountTransfer(){
        Id id = Id.of("123");
        TransferType type = TransferType.WITHDRAW;
        Money money = Money.of(BigDecimal.TEN, Currency.of("EUR"));
        String description = "description";
        LocalDateTime createdAt = LocalDateTime.now();
        AccountTransfer accountTransfer = AccountTransfer.of(id, type, money, description, createdAt);
        Assert.assertThat(accountTransfer.getId(), is(id));
        Assert.assertThat(accountTransfer.getType(), is(type));
        Assert.assertThat(accountTransfer.getDescription(), is(description));
        Assert.assertThat(accountTransfer.getMoney(), is(money));
        Assert.assertThat(accountTransfer.getCreatedAt(), is(createdAt));
    }

}