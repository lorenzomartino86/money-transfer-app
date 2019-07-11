package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.exception.UnknownAccountException;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Money;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetAccountUseCaseTest {
    private AccountRepository mockAccountRepository = createNiceMock(AccountRepository.class);
    private GetAccountUseCase getAccountUseCase = new GetAccountUseCase(mockAccountRepository);

    @Test
    public void canGetExistingAccountByAccountId(){
        final String name = "Foo";
        final BigDecimal balance = BigDecimal.valueOf(1542.35);
        Currency eur = Currency.of("EUR");
        Account mockAccount = Account.createNewAccount(name, Money.of(balance, eur));
        expect(mockAccountRepository.getAccountBy(EasyMock.anyString())).andReturn(Optional.of(mockAccount)).times(1);
        replay(mockAccountRepository);
        Account account = getAccountUseCase.compose("123");
        Assert.assertThat(account, is(notNullValue()));
        Assert.assertThat(account, is(mockAccount));
    }

    @Test(expected = UnknownAccountException.class)
    public void cannotGeAccountByMissingAccountId(){
        expect(mockAccountRepository.getAccountBy(EasyMock.anyString())).andReturn(Optional.empty()).times(1);
        replay(mockAccountRepository);
        getAccountUseCase.compose("123");
    }

    @Test
    public void canGetAllExistingAccounts(){
        final BigDecimal balance = BigDecimal.valueOf(1542.35);
        Currency eur = Currency.of("EUR");
        Account mockAccount1 = Account.createNewAccount("Foo", Money.of(balance, eur));
        Account mockAccount2 = Account.createNewAccount("Bar", Money.of(balance, eur));
        expect(mockAccountRepository.getAllAccounts()).andReturn(Arrays.asList(mockAccount1, mockAccount2)).times(1);
        replay(mockAccountRepository);
        List<Account> accounts = getAccountUseCase.compose();
        Assert.assertThat(accounts, is(notNullValue()));
        Assert.assertThat(accounts.size(), is(2));
        Assert.assertThat(accounts.get(0), is(mockAccount1));
        Assert.assertThat(accounts.get(1), is(mockAccount2));
    }

}