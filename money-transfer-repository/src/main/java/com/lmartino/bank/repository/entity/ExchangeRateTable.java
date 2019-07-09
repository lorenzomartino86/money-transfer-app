package com.lmartino.bank.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@DatabaseTable(tableName = "account")
public class ExchangeRateTable {
    @DatabaseField(canBeNull = false, id = true)
    private String id;

    @DatabaseField(canBeNull = false, uniqueCombo = true)
    private String fromCurrency;

    @DatabaseField(canBeNull = false, uniqueCombo = true)
    private String toCurrency;

    @DatabaseField(canBeNull = false)
    private BigDecimal rate;

}
