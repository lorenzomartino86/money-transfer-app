package com.lmartino.bank.repository;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.lmartino.bank.domain.adapter.ExchangeRateRepository;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.ExchangeRate;
import com.lmartino.bank.repository.entity.ExchangeRateTable;
import com.lmartino.bank.repository.exception.RepositoryException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAO extends BaseDaoImpl<ExchangeRateTable, String> implements ExchangeRateRepository {

    public ExchangeRateDAO(final ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, ExchangeRateTable.class);
    }

    @Override
    public Optional<ExchangeRate> getRate(String fromCurrency, String toCurrency) {
        try {
            QueryBuilder<ExchangeRateTable, String> queryBuilder = super.queryBuilder();
            queryBuilder.where()
                    .eq("fromCurrency", fromCurrency)
                    .and()
                    .eq("toCurrency", toCurrency);
            List<ExchangeRateTable> rates = queryBuilder.query();
            if (rates.isEmpty()){
                return Optional.empty();
            }
            ExchangeRateTable exchangeRateTable = rates.get(0);
            return Optional.of(toDomainModel(exchangeRateTable));
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void saveRates(ExchangeRate... rates) {
        List<ExchangeRateTable> collectionOfRates = new ArrayList<>();
        for (ExchangeRate rate : rates) collectionOfRates.add(toExchangeRateTable(rate));
        try {
            super.create(collectionOfRates);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private ExchangeRate toDomainModel(ExchangeRateTable table){
        return ExchangeRate.of(
                table.getId(),
                Currency.of(table.getFromCurrency()),
                Currency.of(table.getToCurrency()),
                table.getRate()
        );
    }

    private ExchangeRateTable toExchangeRateTable(ExchangeRate rate) {
        ExchangeRateTable exchangeRateTable = new ExchangeRateTable();
        exchangeRateTable.setId(rate.getId().getValue());
        exchangeRateTable.setFromCurrency(rate.getFromCurrency().getValue());
        exchangeRateTable.setToCurrency(rate.getToCurrency().getValue());
        exchangeRateTable.setRate(rate.getRate());
        return exchangeRateTable;
    }

}
