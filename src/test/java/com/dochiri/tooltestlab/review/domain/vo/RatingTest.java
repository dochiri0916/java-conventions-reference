package com.dochiri.tooltestlab.review.domain.vo;

import com.dochiri.tooltestlab.review.domain.exception.InvalidRatingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    @Test
    @DisplayName("평점은 1부터 5까지 허용하고 값 기준 동등성을 유지한다")
    void validatesAndComparesValues() {
        // given
        final Rating minimum = Rating.of(1);

        // when
        final Rating same = Rating.of(1);
        final Rating maximum = Rating.of(5);

        // then
        assertEquals(1, minimum.value());
        assertEquals(5, maximum.value());
        assertEquals(minimum, same);
        assertEquals(minimum.hashCode(), same.hashCode());
        assertNotEquals(minimum, maximum);
        assertNotEquals(minimum, null);
        assertNotEquals(minimum, 1);
        assertEquals("Rating[value=1]", minimum.toString());
        assertThrows(InvalidRatingException.class, () -> Rating.of(0));
        assertThrows(InvalidRatingException.class, () -> Rating.of(6));
    }
}
