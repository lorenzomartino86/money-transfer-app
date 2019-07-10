package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.AccountTransfer;

import java.util.List;
import java.util.Optional;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.unkownAccountException;

public class GetAccountTransfersUseCase {
    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    public GetAccountTransfersUseCase(final AccountRepository accountRepository,
                                      final TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    public List<AccountTransfer> compose(final String accountId){
        Optional<Account> optionalAccount = accountRepository.getAccountBy(accountId);
        if (optionalAccount.isEmpty()) unkownAccountException(accountId);
        return transferRepository.getLastTransfersFor(accountId, 0L, 10L);
    }

}
