package com.lmartino.bank.domain.adapter;

import com.lmartino.bank.domain.model.Transfer;

public interface TransferRepository {
    Transfer saveTransfer(final Transfer transfer);
}
