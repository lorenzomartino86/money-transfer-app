package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.Transfer;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class MakeTransferUseCaseTest {

    private TransferRepository mockTransferRepository = createNiceMock(TransferRepository.class);
    private AccountRepository mockAccountRepository = createNiceMock(AccountRepository.class);
    private MakeTransferUseCase makeTransferUseCase = new MakeTransferUseCase(mockAccountRepository, mockTransferRepository);

    @Test
    public void canMakeTransferFromFooAccountIntoBarAccount(){
        Amount transferAmount = Amount.of(123.12);
        String transferDescription = "Transfer Description";

        // Account mock and stubs
        Account fooAccount = Account.createNewAccount("Foo", Amount.of(1542.35));
        Account barAccount = Account.createNewAccount("Bar", Amount.of(765.91));
        expect(mockAccountRepository.getAccountBy(EasyMock.anyString())).andReturn(Optional.of(barAccount)).times(1);
        expect(mockAccountRepository.getAccountBy(EasyMock.anyString())).andReturn(Optional.of(fooAccount)).times(1);
        replay(mockAccountRepository);

        // Transfer mock and stubs
        Transfer mockedTransfer = Transfer.makeTransfer(fooAccount, barAccount, transferAmount, transferDescription);
        expect(mockTransferRepository.saveTransfer(EasyMock.anyObject(Transfer.class))).andReturn(mockedTransfer).times(1);
        replay(mockTransferRepository);

        Transfer transfer = makeTransferUseCase.compose("foo-account-id",
                "bar-account-id", transferAmount, transferDescription);

        Assert.assertThat(transfer, is(notNullValue()));

    }

}
