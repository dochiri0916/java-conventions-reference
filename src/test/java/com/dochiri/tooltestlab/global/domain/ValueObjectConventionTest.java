package com.dochiri.tooltestlab.global.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectConventionTest {

    static Stream<Class<?>> valueObjects() {
        return Stream.of(
                com.dochiri.tooltestlab.cart.domain.vo.CartId.class,
                com.dochiri.tooltestlab.cart.domain.vo.CartItem.class,
                com.dochiri.tooltestlab.cart.domain.vo.CartItems.class,
                com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId.class,
                com.dochiri.tooltestlab.cart.domain.vo.ProductName.class,
                com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId.class,
                com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId.class,
                com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName.class,
                com.dochiri.tooltestlab.catalog.product.domain.vo.ProductDescription.class,
                com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId.class,
                com.dochiri.tooltestlab.catalog.product.domain.vo.ProductName.class,
                com.dochiri.tooltestlab.catalog.product.domain.vo.Stock.class,
                com.dochiri.tooltestlab.global.domain.vo.Money.class,
                com.dochiri.tooltestlab.global.domain.vo.Quantity.class,
                com.dochiri.tooltestlab.member.domain.vo.Email.class,
                com.dochiri.tooltestlab.member.domain.vo.EncodedPassword.class,
                com.dochiri.tooltestlab.member.domain.vo.MemberId.class,
                com.dochiri.tooltestlab.member.domain.vo.MemberName.class,
                com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId.class,
                com.dochiri.tooltestlab.order.domain.vo.OrderId.class,
                com.dochiri.tooltestlab.order.domain.vo.OrderLine.class,
                com.dochiri.tooltestlab.order.domain.vo.OrderLines.class,
                com.dochiri.tooltestlab.order.domain.vo.Phone.class,
                com.dochiri.tooltestlab.order.domain.vo.ProductName.class,
                com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId.class,
                com.dochiri.tooltestlab.order.domain.vo.RecipientName.class,
                com.dochiri.tooltestlab.order.domain.vo.ShippingAddress.class,
                com.dochiri.tooltestlab.order.domain.vo.StreetAddress.class,
                com.dochiri.tooltestlab.order.domain.vo.ZipCode.class,
                com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId.class,
                com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId.class,
                com.dochiri.tooltestlab.review.domain.vo.Rating.class,
                com.dochiri.tooltestlab.review.domain.vo.ReviewContent.class,
                com.dochiri.tooltestlab.review.domain.vo.ReviewId.class);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("valueObjects")
    @DisplayName("모든 VO는 비공개 생성자와 불변 상태를 가진 final class이다")
    void protectsConstructionAndState(final Class<?> type) {
        // given
        final var constructors = type.getDeclaredConstructors();

        // when
        final var fields = type.getDeclaredFields();

        // then
        assertTrue(Modifier.isFinal(type.getModifiers()));
        assertFalse(type.isRecord());
        assertTrue(type.getPackageName().endsWith(".domain.vo"));
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
        for (final var field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                assertTrue(Modifier.isPrivate(field.getModifiers()));
                assertTrue(Modifier.isFinal(field.getModifiers()));
            }
        }
        assertEquals(type, assertDoesNotThrow(() -> type.getDeclaredMethod("equals", Object.class)).getDeclaringClass());
        assertEquals(type, assertDoesNotThrow(() -> type.getDeclaredMethod("hashCode")).getDeclaringClass());
    }
}
