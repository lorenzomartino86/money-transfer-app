package com.lmartino.bank.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Value Object for money amount representation
 * By default just EUR currency
 */
@Getter
@ToString
@EqualsAndHashCode
public class Amount {
    private double money;

    private Amount(final double money){
        this.money = money;
    }

    public static Amount of(final double money){
        return new Amount(money);
    }

    public boolean isGreaterThan(Amount other) {
        return this.money > other.getMoney();
    }

    public void decreaseBy(Amount amount) {
        this.money -= amount.getMoney();
    }

    public void increaseBy(Amount amount) {
        this.money += amount.getMoney();
    }
}
