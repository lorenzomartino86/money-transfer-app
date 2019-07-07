package com.lmartino.bank.repository;

import com.google.inject.Inject;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.Transfer;
import com.lmartino.bank.repository.entity.AccountTable;
import com.lmartino.bank.repository.entity.TransferTable;
import lombok.extern.java.Log;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.unprocessableTransferException;
import static com.lmartino.bank.repository.DateTimeConverter.toDate;

@Log
public class TransferDAO extends BaseDaoImpl<TransferTable, String> implements TransferRepository {
    private AccountDAO accountDAO;

    @Inject
    public TransferDAO(AccountDAO accountDAO, ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, TransferTable.class);
        this.accountDAO = accountDAO;
    }

    @Override
    public Transfer saveTransfer(Transfer transfer) {
        try {
            // Open a database transaction
            TransactionManager.callInTransaction(connectionSource,
                    (Callable<Void>) () -> {

                        AccountTable fromAccount = accountDAO.updateAccountTable(transfer.getFromAccount());
                        AccountTable toAccount = accountDAO.updateAccountTable(transfer.getToAccount());

                        TransferTable transferTable = new TransferTable();
                        transferTable.setId(transfer.getId().getValue());
                        transferTable.setAmount(transfer.getAmount().getMoney());
                        transferTable.setFromAccount(fromAccount);
                        transferTable.setToAccount(toAccount);
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

}
