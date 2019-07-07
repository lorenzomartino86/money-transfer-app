package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.Transfer;

import java.util.Optional;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.unkownAccountException;

public class MakeTransferUseCase {
    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    public MakeTransferUseCase(final AccountRepository accountRepository, final TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    public Transfer compose(final String fromAccountId, final String toAccountId, final Amount amount, final String description) {

        Optional<Account> fromAccount = accountRepository.getAccountBy(fromAccountId);
        if (fromAccount.isEmpty()) unkownAccountException(fromAccountId);

        Optional<Account> toAccount = accountRepository.getAccountBy(toAccountId);
        if (toAccount.isEmpty()) unkownAccountException(toAccountId);

        Transfer transfer = Transfer.makeTransfer(fromAccount.get(), toAccount.get(), amount, description);

        return transferRepository.saveTransfer(transfer);
    }
}
