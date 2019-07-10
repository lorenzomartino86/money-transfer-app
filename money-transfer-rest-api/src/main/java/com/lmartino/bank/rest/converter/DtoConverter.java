package com.lmartino.bank.rest.converter;

import com.lmartino.bank.domain.model.Transfer;
import com.lmartino.bank.rest.dto.TransferDto;

public final class DtoConverter {

    public static TransferDto transferDto(final Transfer transfer){
        return TransferDto.of(transfer.getId().getValue(),
                transfer.getFromAccount().getId().getValue(),
                transfer.getToAccount().getId().getValue(),
                transfer.getDescription(),
                transfer.getAmount().getMoney(),
                transfer.getCreatedAt());
    }
}
