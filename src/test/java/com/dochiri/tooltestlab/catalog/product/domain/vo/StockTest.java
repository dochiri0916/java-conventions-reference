package com.dochiri.tooltestlab.catalog.product.domain.vo;

import com.dochiri.tooltestlab.catalog.product.domain.exception.InsufficientStockException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.InvalidStockQuantityException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.StockOverflowException;
import com.dochiri.tooltestlab.global.domain.exception.NegativeMoneyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockTest {

    @Test
    @DisplayName("음수 재고는 기존 예외로 거부하고 0과 최대 재고는 허용한다")
    void validates_stock() {
        // given
        final int negative = -1;

        // when
        final Stock empty = Stock.of(0);
        final Stock maximum = Stock.of(Integer.MAX_VALUE);

        // then
        assertThrows(NegativeMoneyException.class, () -> Stock.of(negative));
        assertEquals(0, empty.value());
        assertFalse(empty.hasStock());
        assertEquals(Integer.MAX_VALUE, maximum.value());
        assertTrue(maximum.hasStock());
    }

    @Test
    @DisplayName("재고 증감은 새 객체를 반환하고 기존 재고를 유지한다")
    void changes_stock_immutably() {
        // given
        final Stock original = Stock.of(10);

        // when
        final Stock increased = original.increase(5);
        final Stock decreased = original.decrease(10);

        // then
        assertEquals(10, original.value());
        assertEquals(Stock.of(15), increased);
        assertEquals(Stock.of(0), decreased);
        assertNotSame(original, increased);
        assertNotSame(original, decreased);
        assertFalse(decreased.hasStock());
    }

    @Test
    @DisplayName("재고 증감 수량이 0 이하이면 거부한다")
    void rejects_invalid_quantities() {
        // given
        final Stock stock = Stock.of(10);
        final int[] quantities = {0, -1, Integer.MIN_VALUE};

        // when & then
        for (final int quantity : quantities) {
            assertThrows(InvalidStockQuantityException.class, () -> stock.increase(quantity));
            assertThrows(InvalidStockQuantityException.class, () -> stock.decrease(quantity));
        }
        assertEquals(10, stock.value());
    }

    @Test
    @DisplayName("재고 부족과 정수 범위 초과를 거부하고 최대 재고 증가 경계는 허용한다")
    void validates_arithmetic_boundaries() {
        // given
        final Stock stock = Stock.of(10);
        final Stock maximum = Stock.of(Integer.MAX_VALUE);

        // when
        final Stock boundary = stock.increase(Integer.MAX_VALUE - 10);

        // then
        assertEquals(maximum, boundary);
        assertThrows(InsufficientStockException.class, () -> stock.decrease(11));
        assertThrows(StockOverflowException.class, () -> maximum.increase(1));
        assertEquals(10, stock.value());
        assertEquals(Integer.MAX_VALUE, maximum.value());
    }

    @Test
    @DisplayName("재고는 값 기준 동등성과 문자열 표현을 유지한다")
    void compares_values() {
        // given
        final Stock value = Stock.of(10);
        final Stock same = Stock.of(10);
        final Stock different = Stock.of(11);

        // when
        final int stored = value.value();

        // then
        assertEquals(value, value);
        assertEquals(value, same);
        assertEquals(same, value);
        assertEquals(value.hashCode(), same.hashCode());
        assertNotEquals(value, different);
        assertNotEquals(value, null);
        assertNotEquals(value, stored);
        assertEquals("Stock[value=10]", value.toString());
    }

    @Test
    @DisplayName("재고는 상속과 외부 생성자 호출을 차단하고 상태를 불변으로 유지한다")
    void restricts_construction_and_mutation() throws Exception {
        // given
        final Class<Stock> type = Stock.class;

        // when
        final var constructors = type.getDeclaredConstructors();
        final var field = type.getDeclaredField("value");

        // then
        assertTrue(Modifier.isFinal(type.getModifiers()));
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
        assertTrue(Modifier.isPrivate(field.getModifiers()));
        assertTrue(Modifier.isFinal(field.getModifiers()));
    }
}
