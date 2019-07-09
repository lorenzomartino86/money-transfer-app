package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.exception.UnknownCurrencyCodeException;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.AllowedCurrencies;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.Currency;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateAccountUseCaseTest {

    private AccountRepository mockAccountRepository = createNiceMock(AccountRepository.class);
    private AllowedCurrencies mockAllowedCurrency = createNiceMock(AllowedCurrencies.class);
    private CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase(mockAccountRepository, mockAllowedCurrency);

    @Test
    public void givenNameBalanceAndEUR_whenUserCreateNewAccount_thenAccountIsCreated(){
        final String name = "Foo";
        final BigDecimal balance = BigDecimal.valueOf(1542.35);
        final String currency = "EUR";

        Account newAccount = Account.createNewAccount(name, Amount.of(balance), Currency.of(currency));
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
        createAccountUseCase.compose(name, balance, currency);
    }

}