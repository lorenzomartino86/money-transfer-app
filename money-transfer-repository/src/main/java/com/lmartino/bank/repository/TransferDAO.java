package com.lmartino.bank.repository;

import com.google.inject.Inject;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.*;
import com.lmartino.bank.repository.converter.TableConverter;
import com.lmartino.bank.repository.entity.TransferTable;
import lombok.extern.java.Log;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                        transferTable.setCurrency(transfer.getAmount().getCurrency().getValue());
                        transferTable.setAmount(transfer.getAmount().getValue());
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
    public List<AccountTransfer> getTransfersBy(final String accountId) {
        try {

            List<TransferTable> withdrawTransfers = super.queryForEq("from_account_id", accountId);
            List<AccountTransfer> accountWithdraws = withdrawTransfers.stream()
                    .map(w -> toAccountTransfer(w, TransferType.WITHDRAW)).collect(Collectors.toList());

            List<TransferTable> depositTransfers = super.queryForEq("to_account_id", accountId);
            List<AccountTransfer> accountDeposits = depositTransfers.stream()
                    .map(w -> toAccountTransfer(w, TransferType.DEPOSIT)).collect(Collectors.toList());

            return Stream.concat(accountWithdraws.stream(), accountDeposits.stream())
                    .sorted(Comparator.comparing(AccountTransfer::getCreatedAt))
                    .collect(Collectors.toList());

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
                Money.of(t.getAmount(), Currency.of(t.getCurrency())),
                t.getDescription(),
                toLocalDateTime(t.getCreatedAt()));
    }

    private AccountTransfer toAccountTransfer(TransferTable t, TransferType type) {
        return AccountTransfer.of(
                Id.of(t.getId()),
                type,
                Money.of(t.getAmount(), Currency.of(t.getCurrency())),
                t.getDescription(),
                toLocalDateTime(t.getCreatedAt()));
    }

}
