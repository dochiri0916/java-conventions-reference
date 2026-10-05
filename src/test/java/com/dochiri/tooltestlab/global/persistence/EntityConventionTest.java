package com.dochiri.tooltestlab.global.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityConventionTest {

    static Stream<Class<?>> entities() {
        return Stream.of(
                com.dochiri.tooltestlab.cart.adapter.out.persistence.CartEntity.class,
                com.dochiri.tooltestlab.cart.adapter.out.persistence.CartItemEntity.class,
                com.dochiri.tooltestlab.catalog.category.adapter.out.persistence.CategoryEntity.class,
                com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.ProductEntity.class,
                com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.ProductCategoryEntity.class,
                com.dochiri.tooltestlab.member.adapter.out.persistence.MemberEntity.class,
                com.dochiri.tooltestlab.order.adapter.out.persistence.OrderEntity.class,
                com.dochiri.tooltestlab.order.adapter.out.persistence.OrderLineEntity.class,
                com.dochiri.tooltestlab.review.adapter.out.persistence.ReviewEntity.class);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("entities")
    @DisplayName("영속성 Entity의 UUID 컬럼은 36자리이며 기술 키와 버전을 공개하지 않는다")
    void stores_uuid_and_hides_persistence_keys(final Class<?> type) throws Exception {
        // given
        final var id = type.getDeclaredField("id");

        // when
        final var fields = type.getDeclaredFields();
        final var noArgsConstructor = type.getDeclaredConstructor();

        // then
        assertTrue(type.getPackageName().endsWith(".adapter.out.persistence"));
        assertTrue(Modifier.isProtected(noArgsConstructor.getModifiers()));
        assertEquals(Long.class, id.getType());
        assertNotNull(id.getAnnotation(GeneratedValue.class));
        assertThrows(NoSuchMethodException.class, () -> type.getMethod("getId"));
        for (final var field : fields) {
            if (field.getName().equals("version")) {
                assertThrows(NoSuchMethodException.class, () -> type.getMethod("getVersion"));
            }
            if (field.getType() == String.class && field.getName().endsWith("Id")) {
                final Column column = field.getAnnotation(Column.class);
                assertNotNull(column);
                assertEquals(36, column.length());
                assertFalse(column.nullable());
            }
        }
    }
}
