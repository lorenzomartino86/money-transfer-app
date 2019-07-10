package com.lmartino.bank.repository;

import com.google.inject.Inject;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.*;
import com.lmartino.bank.repository.converter.TableConverter;
import com.lmartino.bank.repository.entity.TransferTable;
import lombok.extern.java.Log;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.unprocessableTransferException;
import static com.lmartino.bank.repository.converter.DateTimeConverter.toDate;
import static com.lmartino.bank.repository.converter.DateTimeConverter.toLocalDateTime;
import static com.lmartino.bank.repository.converter.TableConverter.toAccountTable;

@Log
public class TransferDAO extends BaseDaoImpl<TransferTable, String> implements TransferRepository {
    private AccountRepository accountRepository;

    @Inject
    public TransferDAO(AccountRepository accountRepository, ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, TransferTable.class);
        this.accountRepository = accountRepository;
    }

    @Override
    public Transfer saveTransfer(Transfer transfer) {
        try {
            // Open a database transaction
            TransactionManager.callInTransaction(connectionSource,
                    (Callable<Void>) () -> {

                        Account fromAccount = accountRepository.updateAccount(transfer.getFromAccount());
                        Account toAccount = accountRepository.updateAccount(transfer.getToAccount());

                        TransferTable transferTable = new TransferTable();
                        transferTable.setId(transfer.getId().getValue());
                        transferTable.setAmount(transfer.getAmount().getMoney());
                        transferTable.setFromAccount(toAccountTable(fromAccount));
                        transferTable.setToAccount(toAccountTable(toAccount));
                        transferTable.setDescription(transfer.getDescription());
                        transferTable.setCreatedAt(toDate(transfer.getCreatedAt()));
                        create(transferTable);

                        return null;

                    });
            return transfer;

        } catch (SQLException e) {
            log.info(e.getMessage());
            unprocessableTransferException(transfer);
            return null;
        }
    }

    @Override
    public List<AccountTransfer> getLastTransfersFor(final String accountId, final Long offset, final Long limit) {
        try {
            QueryBuilder<TransferTable, String> queryBuilder = super.queryBuilder();
            queryBuilder.where()
                    .eq("from_account_id", accountId)
                    .or()
                    .eq("to_account_id", accountId);
            List<TransferTable> transferTableRows = queryBuilder.offset(offset).limit(limit).query();
            return transferTableRows.stream().map(this::toAccountTransfer).collect(Collectors.toList());
        } catch (SQLException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Transfer toDomainModel(TransferTable t) {
        return Transfer.of(
                Id.of(t.getId()),
                TableConverter.toDomainModel(t.getFromAccount()),
                TableConverter.toDomainModel(t.getToAccount()),
                Amount.of(t.getAmount()),
                t.getDescription(),
                toLocalDateTime(t.getCreatedAt()));
    }

    private AccountTransfer toAccountTransfer(TransferTable t) {
        return AccountTransfer.of(
                Id.of(t.getId()),
                Id.of(t.getFromAccount().getId()),
                Amount.of(t.getAmount()),
                t.getDescription(),
                toLocalDateTime(t.getCreatedAt()));
    }

}
