package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.*;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetAccountTransfersUseCaseTest {
    private AccountRepository mockAccountRepository = createNiceMock(AccountRepository.class);
    private TransferRepository mockTransferRepository = createNiceMock(TransferRepository.class);
    private GetAccountTransfersUseCase getAccountTransfersUseCase = new GetAccountTransfersUseCase(mockAccountRepository, mockTransferRepository);

    @Test
    public void canGetAccountTransfersByAccountInAccountCurrency(){
        final String name = "Foo";
        final BigDecimal balance = BigDecimal.valueOf(1542.35);

        Currency eur = Currency.of("EUR");
        Account account = Account.createNewAccount(name, Money.of(balance, eur));
        expect(mockAccountRepository.getAccountBy(EasyMock.anyString())).andReturn(Optional.of(account)).times(1);
        replay(mockAccountRepository);

        AccountTransfer withdraw1 = AccountTransfer.of(Id.of("1"), TransferType.WITHDRAW,
                Money.of(BigDecimal.TEN, eur), "description 1", LocalDateTime.now());
        AccountTransfer deposit1 = AccountTransfer.of(Id.of("2"), TransferType.DEPOSIT,
                Money.of(BigDecimal.TEN, eur), "description 2", LocalDateTime.now());
        AccountTransfer withdraw2 = AccountTransfer.of(Id.of("3"), TransferType.WITHDRAW,
                Money.of(BigDecimal.ONE, eur), "description 3", LocalDateTime.now());
        AccountTransfer deposit2 = AccountTransfer.of(Id.of("4"), TransferType.DEPOSIT,
                Money.of(BigDecimal.ONE, eur), "description 4", LocalDateTime.now());
        List<AccountTransfer> mockTransfers = Arrays.asList(withdraw1, deposit1, withdraw2, deposit2);
        expect(mockTransferRepository.getTransfersBy(EasyMock.anyString()))
                .andReturn(mockTransfers)
                .times(1);
        replay(mockTransferRepository);

        List<AccountTransfer> transfers = getAccountTransfersUseCase.compose("123123");
        Assert.assertThat(transfers, is(notNullValue()));
        Assert.assertThat(transfers.size(), is(mockTransfers.size()));
        Assert.assertThat(transfers.get(0), is(mockTransfers.get(0)));
        Assert.assertThat(transfers.get(1), is(mockTransfers.get(1)));
        Assert.assertThat(transfers.get(2), is(mockTransfers.get(2)));
        Assert.assertThat(transfers.get(3), is(mockTransfers.get(3)));

    }

}