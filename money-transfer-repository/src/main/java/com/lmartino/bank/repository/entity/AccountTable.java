package com.lmartino.bank.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@DatabaseTable(tableName = "account")
public class AccountTable {
    @DatabaseField(canBeNull = false, id = true)
    private String id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private Double balance;

    @DatabaseField(canBeNull = false)
    private Date createdBy;

}
