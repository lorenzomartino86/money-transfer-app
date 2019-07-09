package com.lmartino.bank.repository;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.ExchangeRate;
import com.lmartino.bank.repository.entity.ExchangeRateTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;

public class ExchangeRateDAOTest {
    private JdbcPooledConnectionSource connectionSource;
    private ExchangeRateDAO exchangeRateDAO;

    @Before
    public void setUp() throws SQLException {
        connectionSource = new JdbcPooledConnectionSource("jdbc:h2:mem:bank;DB_CLOSE_DELAY=-1");
        TableUtils.createTableIfNotExists(connectionSource, ExchangeRateTable.class);
        exchangeRateDAO = new ExchangeRateDAO(connectionSource);
    }

    @Test
    public void canSaveMultipleExchangeRates() {
        ExchangeRate eurusd = ExchangeRate.create(Currency.of("EUR"), Currency.of("USD"), Amount.of(BigDecimal.valueOf(1.12077)));
        ExchangeRate usdeur = ExchangeRate.create(Currency.of("USD"), Currency.of("EUR"), Amount.of(BigDecimal.valueOf(0.89224)));
        ExchangeRate gbpeur = ExchangeRate.create(Currency.of("GBP"), Currency.of("EUR"), Amount.of(BigDecimal.valueOf(1.11210)));
        ExchangeRate eurgbp = ExchangeRate.create(Currency.of("EUR"), Currency.of("GBP"), Amount.of(BigDecimal.valueOf(0.89919)));

        exchangeRateDAO.saveRates(eurusd, usdeur, gbpeur, eurgbp);

        Assert.assertThat(exchangeRateDAO.getRate("EUR", "USD").isEmpty(), is(false));
        Assert.assertThat(exchangeRateDAO.getRate("EUR", "USD").get().getRate(), is(eurusd.getRate()));

        Assert.assertThat(exchangeRateDAO.getRate("USD", "EUR").isEmpty(), is(false));
        Assert.assertThat(exchangeRateDAO.getRate("USD", "EUR").get().getRate(), is(usdeur.getRate()));

        Assert.assertThat(exchangeRateDAO.getRate("GBP", "EUR").isEmpty(), is(false));
        Assert.assertThat(exchangeRateDAO.getRate("GBP", "EUR").get().getRate(), is(gbpeur.getRate()));

        Assert.assertThat(exchangeRateDAO.getRate("GBP", "EUR").isEmpty(), is(false));
        Assert.assertThat(exchangeRateDAO.getRate("EUR", "GBP").get().getRate(), is(eurgbp.getRate()));
    }
}