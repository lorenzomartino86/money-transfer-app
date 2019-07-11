package com.lmartino.bank.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object for money representation as value and currency
 */
@Getter
@ToString
@EqualsAndHashCode
public class Money {
    private BigDecimal value;
    private Currency currency;

    private Money(final BigDecimal value, final Currency currency){
        this.value = value;
        this.currency = currency;
    }

    public static Money of(final BigDecimal value, final Currency currency){
        return new Money(value.setScale(2, RoundingMode.CEILING), currency);
    }

    public boolean isGreaterThan(Money other) {
        return this.value.doubleValue() > other.getValue().doubleValue();
    }

    public void decreaseBy(Money money) {
        this.value = this.value.subtract(money.getValue());
    }

    public void increaseBy(Money money) {
        this.value = this.value.add(money.getValue());
    }

    public boolean hasSameCurrency(final Money other){
        return other.getCurrency().getValue().equals(currency.getValue());
    }

    public Money applyRate(final BigDecimal rate, final Currency currency){
        return Money.of(value.multiply(rate), currency);
    }

}
