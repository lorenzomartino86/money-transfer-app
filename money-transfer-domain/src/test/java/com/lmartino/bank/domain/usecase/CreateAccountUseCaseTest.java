package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.exception.AccountNameConflictException;
import com.lmartino.bank.domain.exception.UnknownCurrencyCodeException;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Money;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateAccountUseCaseTest {
    private Currency eur = Currency.of("EUR");
    private AccountRepository mockAccountRepository = createNiceMock(AccountRepository.class);
    private CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase(mockAccountRepository);

    @Test
    public void givenNameBalanceAndEUR_whenUserCreateNewAccount_thenAccountIsCreated(){
        final String name = "Foo";
        final BigDecimal balance = BigDecimal.valueOf(1542.35);
        final String currency = "EUR";

        expect(mockAccountRepository.getAccountByName(EasyMock.anyString())).andReturn(Optional.empty()).times(1);

        Account newAccount = Account.createNewAccount(name, Money.of(balance, eur));
        expect(mockAccountRepository.saveAccount(EasyMock.anyObject(Account.class))).andReturn(newAccount).times(1);
        replay(mockAccountRepository);

        Account account = createAccountUseCase.compose(name, balance, currency);
        Assert.assertThat(account, is(notNullValue()));
        Assert.assertThat(account, is(newAccount));

    }

    @Test(expected = UnknownCurrencyCodeException.class)
    public void givenNameBalanceAndAUD_whenUserCreateNewAccount_thenAccountCreationIsRejected(){
        final String name = "Foo";
        final BigDecimal balance = BigDecimal.valueOf(1542.35);
        final String currency = "AUD";

        expect(mockAccountRepository.getAccountByName(EasyMock.anyString())).andReturn(Optional.empty()).times(1);
        replay(mockAccountRepository);

        createAccountUseCase.compose(name, balance, currency);
    }

    @Test(expected = AccountNameConflictException.class)
    public void givenAlreadyExistingAccountName_whenUserCreateNewAccount_thenAccountCreationIsRejected(){
        final String name = "Foo";
        final BigDecimal balance = BigDecimal.valueOf(1542.35);
        final String currency = "EUR";

        Account alreadyExistingAccount = Account.createNewAccount(name, Money.of(balance, eur));
        expect(mockAccountRepository.getAccountByName(EasyMock.anyString())).andReturn(Optional.of(alreadyExistingAccount)).times(1);
        replay(mockAccountRepository);

        createAccountUseCase.compose(name, balance, currency);
    }

}