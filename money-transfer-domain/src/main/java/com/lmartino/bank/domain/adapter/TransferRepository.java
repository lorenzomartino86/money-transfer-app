package com.lmartino.bank.domain.adapter;

import com.lmartino.bank.domain.model.AccountTransfer;
import com.lmartino.bank.domain.model.Transfer;

import java.util.List;

public interface TransferRepository {
    Transfer saveTransfer(final Transfer transfer);

    List<AccountTransfer> getTransfersBy(final String accountId);

}
