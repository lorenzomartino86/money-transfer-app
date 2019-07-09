package com.lmartino.bank.domain.adapter;

import com.lmartino.bank.domain.model.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateRepository {

    Optional<ExchangeRate> getRate(final String fromCurrency, final String toCurrency);

    void saveRates(final ExchangeRate ... rates);

}
