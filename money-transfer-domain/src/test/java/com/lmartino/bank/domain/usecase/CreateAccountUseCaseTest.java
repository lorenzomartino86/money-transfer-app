package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Amount;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateAccountUseCaseTest {

    private AccountRepository mockAccountRepository = createNiceMock(AccountRepository.class);
    private CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase(mockAccountRepository);

    @Test
    public void canCreateNewAccount(){
        final String name = "Foo";
        final BigDecimal balance = BigDecimal.valueOf(1542.35);

        Account newAccount = Account.createNewAccount(name, Amount.of(balance), null);
        expect(mockAccountRepository.saveAccount(EasyMock.anyObject(Account.class)))
                .andReturn(newAccount)
                .times(1);
        replay(mockAccountRepository);

        Account account = createAccountUseCase.compose(name, balance, null);

        Assert.assertThat(account, is(notNullValue()));
        Assert.assertThat(account, is(newAccount));

    }

}