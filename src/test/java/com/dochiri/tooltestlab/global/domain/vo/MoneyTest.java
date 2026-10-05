package com.dochiri.tooltestlab.global.domain.vo;

import com.dochiri.tooltestlab.global.domain.exception.MoneyOverflowException;
import com.dochiri.tooltestlab.global.domain.exception.MoneyRequiredException;
import com.dochiri.tooltestlab.global.domain.exception.NegativeMoneyException;
import com.dochiri.tooltestlab.global.domain.exception.NegativeMoneyMultiplierException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    @DisplayName("금액은 0 이상만 허용하고 값 기준 동등성을 유지한다")
    void validates_and_compares_values() {
        // given
        final Money money = Money.of(100);

        // when
        final Money same = Money.of(100);

        // then
        assertEquals(100, money.amount());
        assertEquals(Money.ZERO, Money.of(0));
        assertEquals(money, same);
        assertEquals(money.hashCode(), same.hashCode());
        assertNotEquals(money, Money.of(101));
        assertNotEquals(money, null);
        assertNotEquals(money, 100L);
        assertEquals("Money[amount=100]", money.toString());
        assertThrows(NegativeMoneyException.class, () -> Money.of(-1));
    }

    @Test
    @DisplayName("금액 덧셈과 곱셈은 새 객체를 반환하고 원본을 유지한다")
    void calculates_immutably() {
        // given
        final Money money = Money.of(100);

        // when
        final Money added = money.add(Money.of(50));
        final Money multiplied = money.multiply(3);

        // then
        assertEquals(Money.of(150), added);
        assertEquals(Money.of(300), multiplied);
        assertEquals(Money.ZERO, money.multiply(0));
        assertEquals(100, money.amount());
    }

    @Test
    @DisplayName("금액 연산의 null과 음수 배수 및 정수 범위 초과를 거부한다")
    void rejects_invalid_arithmetic() {
        // given
        final Money maximum = Money.of(Long.MAX_VALUE);

        // when & then
        assertThrows(MoneyRequiredException.class, () -> maximum.add(null));
        assertThrows(NegativeMoneyMultiplierException.class, () -> maximum.multiply(-1));
        assertThrows(MoneyOverflowException.class, () -> maximum.add(Money.of(1)));
        assertThrows(MoneyOverflowException.class, () -> maximum.multiply(2));
        assertEquals(Long.MAX_VALUE, maximum.amount());
    }
}
