package com.lmartino.bank.rest.converter;

import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Money;
import com.lmartino.bank.domain.model.Transfer;
import com.lmartino.bank.rest.dto.TransferDto;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class DtoConverterTest {

    @Test
    public void canCreateTransferDto(){
        Account foo = Account.createNewAccount("foo", Money.of(BigDecimal.valueOf(12.99), Currency.of("EUR")));
        Account bar = Account.createNewAccount("bar", Money.of(BigDecimal.valueOf(1.99), Currency.of("EUR")));
        Transfer transfer = Transfer.makeTransfer(foo, bar, Money.of(BigDecimal.valueOf(1.99), Currency.of("EUR")), "Notes", BigDecimal.ONE);

        TransferDto transferDto = DtoConverter.transferDto(transfer);
        Assert.assertThat(transferDto, CoreMatchers.is(CoreMatchers.notNullValue()));
        Assert.assertThat(transferDto.getAmount(), CoreMatchers.is(transfer.getAmount().getValue()));
        Assert.assertThat(transferDto.getCurrency(), CoreMatchers.is(transfer.getAmount().getCurrency().getValue()));
        Assert.assertThat(transferDto.getDescription(), CoreMatchers.is(transfer.getDescription()));
        Assert.assertThat(transferDto.getId(), CoreMatchers.is(transfer.getId().getValue()));
        Assert.assertThat(transferDto.getFromAccountId(), CoreMatchers.is(transfer.getFromAccount().getId().getValue()));
        Assert.assertThat(transferDto.getToAccountId(), CoreMatchers.is(transfer.getToAccount().getId().getValue()));
        Assert.assertThat(transfer.getCreatedAt(), CoreMatchers.is(transfer.getCreatedAt()));

    }
}