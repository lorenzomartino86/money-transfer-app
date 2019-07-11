package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.ExchangeRateRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.*;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class MakeTransferUseCaseTest {
    private Currency eur = Currency.of("EUR");
    private Currency usd = Currency.of("USD");

    private TransferRepository mockTransferRepository = createNiceMock(TransferRepository.class);
    private AccountRepository mockAccountRepository = createNiceMock(AccountRepository.class);
    private ExchangeRateRepository mockExchangeRateRepository = createNiceMock(ExchangeRateRepository.class);
    private MakeTransferUseCase makeTransferUseCase = new MakeTransferUseCase(mockAccountRepository,
            mockTransferRepository, mockExchangeRateRepository);

    @Test
    public void canMakeTransferFromEurAccountIntoEurAccount(){
        Money transferMoney = Money.of(BigDecimal.valueOf(123.12), eur);
        String transferDescription = "Transfer Description";

        // Account mock and stubs
        Account fooAccount = Account.createNewAccount("Foo", Money.of(BigDecimal.valueOf(1542.35), eur));
        Account barAccount = Account.createNewAccount("Bar", Money.of(BigDecimal.valueOf(765.91), eur));
        expect(mockAccountRepository.getAccountBy(EasyMock.anyString())).andReturn(Optional.of(barAccount)).times(1);
        expect(mockAccountRepository.getAccountBy(EasyMock.anyString())).andReturn(Optional.of(fooAccount)).times(1);
        replay(mockAccountRepository);

        // Transfer mock and stubs
        Transfer mockedTransfer = Transfer.makeTransfer(fooAccount, barAccount, transferMoney, transferDescription, BigDecimal.ONE);
        expect(mockTransferRepository.saveTransfer(EasyMock.anyObject(Transfer.class))).andReturn(mockedTransfer).times(1);
        replay(mockTransferRepository);

        Transfer transfer = makeTransferUseCase.compose("foo-account-id",
                "bar-account-id", transferMoney, transferDescription);

        Assert.assertThat(transfer, is(notNullValue()));
        Assert.assertThat(transfer, is(mockedTransfer));

    }


    @Test
    public void canMakeTransferFromEurAccountIntoUsdAccount(){
        Money transferMoney = Money.of(BigDecimal.valueOf(123.12), eur);
        String transferDescription = "Transfer Description";

        // Account mock and stubs
        Account fooAccount = Account.createNewAccount("Foo", Money.of(BigDecimal.valueOf(1542.35), eur));
        Account barAccount = Account.createNewAccount("Bar", Money.of(BigDecimal.valueOf(765.91), usd));
        expect(mockAccountRepository.getAccountBy(fooAccount.getId().getValue())).andReturn(Optional.of(fooAccount)).times(1);
        expect(mockAccountRepository.getAccountBy(barAccount.getId().getValue())).andReturn(Optional.of(barAccount)).times(1);
        replay(mockAccountRepository);

        // Transfer mock and stubs
        Transfer mockedTransfer = Transfer.makeTransfer(fooAccount, barAccount, transferMoney, transferDescription, BigDecimal.valueOf(1.12));
        expect(mockTransferRepository.saveTransfer(EasyMock.anyObject(Transfer.class))).andReturn(mockedTransfer).times(1);
        replay(mockTransferRepository);

        // exchange rate mock and stubs
        ExchangeRate rate = ExchangeRate.of("123", eur, usd, BigDecimal.valueOf(1.12));
        expect(mockExchangeRateRepository.getRate(EasyMock.anyString(), EasyMock.anyString()))
                .andReturn(Optional.of(rate))
                .times(1);
        replay(mockExchangeRateRepository);

        Transfer transfer = makeTransferUseCase.compose(fooAccount.getId().getValue(),
                barAccount.getId().getValue(), transferMoney, transferDescription);

        Assert.assertThat(transfer, is(notNullValue()));
        Assert.assertThat(transfer, is(mockedTransfer));

    }

}
