package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;
import com.dochiri.tooltestlab.order.domain.exception.OrderLinesRequiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderLinesTest {

    @Test
    @DisplayName("주문 목록은 방어적으로 복사하고 값 동등성과 합계를 유지한다")
    void copiesAndProtectsValues() {
        // given
        final OrderLine line = OrderLine.of(
                ProductReferenceId.parse("01900000-0000-7000-8000-000000000001"),
                ProductName.of("상품"), Quantity.of(2), Money.of(100));
        final List<OrderLine> source = new ArrayList<>(List.of(line));

        // when
        final OrderLines lines = OrderLines.of(source);
        source.clear();

        // then
        assertEquals(List.of(line), lines.values());
        assertEquals(OrderLines.of(line), lines);
        assertEquals(OrderLines.of(line).hashCode(), lines.hashCode());
        assertEquals(1, lines.size());
        assertTrue(lines.containsProduct(line.productId()));
        assertEquals(Money.of(200), lines.totalAmount());
        assertThrows(UnsupportedOperationException.class, () -> lines.values().clear());
    }

    @Test
    @DisplayName("주문 목록은 null 빈 목록 null 요소와 100개 초과 항목을 거부한다")
    void validatesCollectionBoundaries() {
        // given
        final OrderLine line = OrderLine.of(
                ProductReferenceId.parse("01900000-0000-7000-8000-000000000001"),
                ProductName.of("상품"), Quantity.of(1), Money.of(100));

        // when
        final OrderLines maximum = OrderLines.of(Collections.nCopies(100, line));

        // then
        assertEquals(100, maximum.size());
        assertThrows(OrderLinesRequiredException.class, () -> OrderLines.of((List<OrderLine>) null));
        assertThrows(OrderLinesRequiredException.class, () -> OrderLines.of((OrderLine[]) null));
        assertThrows(InvalidOrderLinesException.class, () -> OrderLines.of(List.of()));
        assertThrows(InvalidOrderLinesException.class, () -> OrderLines.of(Arrays.asList(line, null)));
        assertThrows(InvalidOrderLinesException.class, () -> OrderLines.of(Collections.nCopies(101, line)));
    }
}
