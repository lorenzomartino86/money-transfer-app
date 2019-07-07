package com.lmartino.bank.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object for money amount representation
 * By default just EUR currency
 */
@Getter
@ToString
@EqualsAndHashCode
public class Amount {
    private BigDecimal money;

    private Amount(final BigDecimal money){
        this.money = money;
    }

    public static Amount of(final BigDecimal money){
        return new Amount(money.setScale(2, RoundingMode.CEILING));
    }

    public boolean isGreaterThan(Amount other) {
        return this.money.doubleValue() > other.getMoney().doubleValue();
    }

    public void decreaseBy(Amount amount) {
        money = money.subtract(amount.getMoney());
    }

    public void increaseBy(Amount amount) {
        money = money.add(amount.getMoney());
    }
}
