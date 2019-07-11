package com.lmartino.bank.rest.converter;

import com.lmartino.bank.domain.model.*;
import com.lmartino.bank.rest.dto.AccountDto;
import com.lmartino.bank.rest.dto.AccountTransferDto;
import com.lmartino.bank.rest.dto.TransferDto;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class DtoConverterTest {

    @Test
    public void canCreateTransferDto(){
        Account foo = Account.createNewAccount("foo", Money.of(BigDecimal.valueOf(12.99), Currency.of("EUR")));
        Account bar = Account.createNewAccount("bar", Money.of(BigDecimal.valueOf(1.99), Currency.of("EUR")));
        Transfer transfer = Transfer.makeTransfer(foo, bar, Money.of(BigDecimal.valueOf(1.99), Currency.of("EUR")),
                "Notes", BigDecimal.ONE);

        TransferDto transferDto = DtoConverter.transferDto(transfer);
        Assert.assertThat(transferDto, is(CoreMatchers.notNullValue()));
        Assert.assertThat(transferDto.getAmount(), is(transfer.getAmount().getValue()));
        Assert.assertThat(transferDto.getCurrency(), is(transfer.getAmount().getCurrency().getValue()));
        Assert.assertThat(transferDto.getDescription(), is(transfer.getDescription()));
        Assert.assertThat(transferDto.getId(), is(transfer.getId().getValue()));
        Assert.assertThat(transferDto.getFromAccountId(), is(transfer.getFromAccount().getId().getValue()));
        Assert.assertThat(transferDto.getToAccountId(), is(transfer.getToAccount().getId().getValue()));
        Assert.assertThat(transferDto.getCreatedAt(), is(transfer.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

    }

    @Test
    public void canCreateAccountDto(){
        Account foo = Account.createNewAccount("foo", Money.of(BigDecimal.valueOf(12.99), Currency.of("EUR")));

        AccountDto accountDto = DtoConverter.accountDto(foo);
        Assert.assertThat(accountDto, is(notNullValue()));
        Assert.assertThat(accountDto.getId(), is(foo.getId().getValue()));
        Assert.assertThat(accountDto.getName(), is(foo.getName()));
        Assert.assertThat(accountDto.getBalance(), is(foo.getBalance().getValue()));
        Assert.assertThat(accountDto.getCurrency(), is(foo.getBalance().getCurrency().getValue()));
        Assert.assertThat(accountDto.getCreatedAt(), is(foo.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

    }

    @Test
    public void canCreateAccountTransferDto(){
        AccountTransfer accountTransfer = AccountTransfer.of(Id.of("123"),
                TransferType.WITHDRAW,
                Money.of(BigDecimal.valueOf(12.99),
                        Currency.of("EUR")), "notes", LocalDateTime.now());

        AccountTransferDto accountTransferDto = DtoConverter.accountTransferDto(accountTransfer);
        Assert.assertThat(accountTransferDto, is(notNullValue()));
        Assert.assertThat(accountTransferDto.getId(), is(accountTransfer.getId().getValue()));
        Assert.assertThat(accountTransferDto.getType(), is(accountTransfer.getType().name()));
        Assert.assertThat(accountTransferDto.getAmount(), is(accountTransfer.getMoney().getValue()));
        Assert.assertThat(accountTransferDto.getCurrency(), is(accountTransfer.getMoney().getCurrency().getValue()));
        Assert.assertThat(accountTransferDto.getCreatedAt(), is(accountTransfer.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

    }
}