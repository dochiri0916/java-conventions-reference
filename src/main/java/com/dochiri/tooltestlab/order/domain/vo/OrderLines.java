package com.dochiri.tooltestlab.order.domain.vo;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;
import com.dochiri.tooltestlab.order.domain.exception.OrderLinesRequiredException;
import com.dochiri.tooltestlab.order.domain.exception.ProductReferenceIdRequiredException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class OrderLines {

    private static final int MAX_LINE_COUNT = 100;

    private final List<OrderLine> values;

    private OrderLines(final List<OrderLine> values) {
        if (values == null) {
            throw new OrderLinesRequiredException();
        }

        if (values.isEmpty()) {
            throw new InvalidOrderLinesException();
        }
        if (values.size() > MAX_LINE_COUNT) {
            throw new InvalidOrderLinesException();
        }

        if (values.stream().anyMatch(Objects::isNull)) {
            throw new InvalidOrderLinesException();
        }

        this.values = List.copyOf(values);
    }

    public static OrderLines of(final OrderLine... lines) {
        if (lines == null) {
            throw new OrderLinesRequiredException();
        }
        return new OrderLines(Arrays.asList(lines));
    }

    public static OrderLines of(final List<OrderLine> lines) {
        return new OrderLines(lines);
    }

    public Money totalAmount() {
        return values.stream()
                .map(OrderLine::totalPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public int size() {
        return values.size();
    }

    public boolean containsProduct(final ProductReferenceId productId) {
        if (productId == null) {
            throw new ProductReferenceIdRequiredException();
        }
        return values.stream().anyMatch(line -> line.productId().equals(productId));
    }

    public List<OrderLine> values() {
        return values;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final OrderLines other)) {
            return false;
        }
        return values.equals(other.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public String toString() {
        return "OrderLines[values=" + values + "]";
    }

}
