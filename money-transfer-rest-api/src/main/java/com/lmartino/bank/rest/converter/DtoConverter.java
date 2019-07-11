package com.lmartino.bank.rest.converter;

import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.AccountTransfer;
import com.lmartino.bank.domain.model.Transfer;
import com.lmartino.bank.rest.dto.AccountDto;
import com.lmartino.bank.rest.dto.AccountTransferDto;
import com.lmartino.bank.rest.dto.TransferDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DtoConverter {

    public static TransferDto transferDto(final Transfer transfer){
        return TransferDto.of(transfer.getId().getValue(),
                transfer.getFromAccount().getId().getValue(),
                transfer.getToAccount().getId().getValue(),
                transfer.getDescription(),
                transfer.getWithdrawAmount().getCurrency().getValue(),
                transfer.getWithdrawAmount().getValue(),
                transfer.getCreatedAt());
    }

    public static AccountTransferDto accountTransferDto(final AccountTransfer accountTransfer){
        return AccountTransferDto.of(
                accountTransfer.getId().getValue(),
                accountTransfer.getType().name(),
                accountTransfer.getMoney().getValue(),
                accountTransfer.getMoney().getCurrency().getValue(),
                accountTransfer.getDescription(),
                accountTransfer.getCreatedAt()
        );
    }

    public static AccountDto accountDto(final Account account){
        return AccountDto.of(
                account.getId().getValue(),
                account.getName(),
                account.getBalance().getValue(),
                account.getCreatedAt(),
                account.getCurrency().getValue()
        );
    }

}
