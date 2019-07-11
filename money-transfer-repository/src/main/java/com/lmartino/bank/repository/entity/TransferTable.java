package com.lmartino.bank.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@DatabaseTable(tableName = "transfer")
public class TransferTable {
    @DatabaseField(canBeNull = false, id = true)
    private String id;

    @DatabaseField(foreign = true, columnName = "from_account_id")
    private AccountTable fromAccount;

    @DatabaseField(foreign = true, columnName = "to_account_id")
    private AccountTable toAccount;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(canBeNull = false)
    private String withdrawCurrency;

    @DatabaseField(canBeNull = false)
    private BigDecimal withdrawAmount;

    @DatabaseField(canBeNull = false)
    private String depositCurrency;

    @DatabaseField(canBeNull = false)
    private BigDecimal depositAmount;

    @DatabaseField(canBeNull = false)
    private BigDecimal exchangeRate;

    @DatabaseField(canBeNull = false)
    private Date createdAt;

}
