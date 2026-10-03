package com.dochiri.tooltestlab.catalog.product.domain.vo;

import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionTooLongException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionTooShortException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class ProductDescriptionTest {

    @Test
    @DisplayName("상품 설명이 null이면 필수 예외가 발생한다")
    void rejectsNull() {
        // given
        final String value = null;

        // when
        // then
        assertThrows(ProductDescriptionRequiredException.class, () -> ProductDescription.of(value));
    }

    @Test
    @DisplayName("공백 제거 후 상품 설명이 5자 미만이거나 1000자를 초과하면 거부한다")
    void rejectsInvalidLengths() {
        // given
        final String[] shortValues = {"", " ", "\u2003", "abcd", "  abcd  "};
        final String longValue = "a".repeat(1_001);

        // when
        // then
        for (final String value : shortValues) {
            assertThrows(ProductDescriptionTooShortException.class, () -> ProductDescription.of(value));
        }
        assertThrows(ProductDescriptionTooLongException.class, () -> ProductDescription.of(longValue));
    }

    @Test
    @DisplayName("상품 설명의 양끝 공백을 제거하고 최소 및 최대 길이를 허용한다")
    void normalizesBoundaryValues() {
        // given
        final String minimum = "abcde";
        final String maximum = "a".repeat(1_000);

        // when
        final ProductDescription shortDescription = ProductDescription.of("\u2003 " + minimum + " \t");
        final ProductDescription longDescription = ProductDescription.of(" " + maximum + " ");

        // then
        assertEquals(minimum, shortDescription.value());
        assertEquals(maximum, longDescription.value());
    }

    @Test
    @DisplayName("상품 설명은 값 기준 동등성과 문자열 표현을 유지한다")
    void comparesValues() {
        // given
        final ProductDescription value = ProductDescription.of("상품 설명입니다");
        final ProductDescription same = ProductDescription.of(" 상품 설명입니다 ");
        final ProductDescription different = ProductDescription.of("다른 상품 설명입니다");

        // when
        final String stored = value.value();

        // then
        assertEquals(value, value);
        assertEquals(value, same);
        assertEquals(same, value);
        assertEquals(value.hashCode(), same.hashCode());
        assertNotEquals(value, different);
        assertNotEquals(value, null);
        assertNotEquals(value, stored);
        assertEquals("ProductDescription[value=상품 설명입니다]", value.toString());
    }

    @Test
    @DisplayName("상품 설명은 상속과 외부 생성자 호출을 차단하고 상태를 불변으로 유지한다")
    void restrictsConstructionAndMutation() {
        // given
        final Class<ProductDescription> type = ProductDescription.class;

        // when
        final var constructors = type.getDeclaredConstructors();
        final var field = assertDoesNotThrow(() -> type.getDeclaredField("value"));

        // then
        assertTrue(Modifier.isFinal(type.getModifiers()));
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
        assertTrue(Modifier.isPrivate(field.getModifiers()));
        assertTrue(Modifier.isFinal(field.getModifiers()));
    }
}
