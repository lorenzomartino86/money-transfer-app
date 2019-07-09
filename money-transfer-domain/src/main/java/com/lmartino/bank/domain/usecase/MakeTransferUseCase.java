package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.ExchangeRateRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.ExchangeRate;
import com.lmartino.bank.domain.model.Transfer;

import java.math.BigDecimal;
import java.util.Optional;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.unkownAccountException;

public class MakeTransferUseCase {
    private AccountRepository accountRepository;
    private TransferRepository transferRepository;
    private ExchangeRateRepository exchangeRateRepository;

    public MakeTransferUseCase(final AccountRepository accountRepository,
                               final TransferRepository transferRepository,
                               final ExchangeRateRepository exchangeRateRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public Transfer compose(final String fromAccountId, final String toAccountId, final Amount amount, final String description) {

        Optional<Account> fromAccount = accountRepository.getAccountBy(fromAccountId);
        if (fromAccount.isEmpty()) unkownAccountException(fromAccountId);

        Optional<Account> toAccount = accountRepository.getAccountBy(toAccountId);
        if (toAccount.isEmpty()) unkownAccountException(toAccountId);


        Account sourceAccount = fromAccount.get();
        Account targetAccount = toAccount.get();

        BigDecimal rate;
        if (sourceAccount.hasSameCurrency(targetAccount)) rate = BigDecimal.ONE;
        else {
            Optional<ExchangeRate> exchangeRate = exchangeRateRepository.getRate(sourceAccount.getCurrency().getValue(),
                    targetAccount.getCurrency().getValue());
            rate = exchangeRate.get().getRate().getMoney();
        }
        Transfer transfer = Transfer.makeTransfer(sourceAccount, targetAccount, amount, description, rate);

        return transferRepository.saveTransfer(transfer);
    }
}
