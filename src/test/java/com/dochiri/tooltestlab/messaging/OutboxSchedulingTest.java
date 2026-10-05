package com.dochiri.tooltestlab.messaging;

import org.junit.jupiter.api.DisplayName;
import com.dochiri.tooltestlab.global.messaging.OutboxRelay;
import com.dochiri.tooltestlab.global.messaging.OutboxSchedulingConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OutboxSchedulingTest {
    @Test
    @DisplayName("실패한 릴레이 하나가 다른 BC나 이후 폴링을 막지 않는다")
    void one_failing_relay_does_not_starve_other_contexts_or_future_polls() {
        // given
        final var failing = mock(OutboxRelay.class);
        final var healthy = mock(OutboxRelay.class);
        when(failing.relay()).thenThrow(new IllegalStateException("DB 실패"));
        final var scheduler = new OutboxSchedulingConfiguration(List.of(failing, healthy));

        // when & then
        assertDoesNotThrow(scheduler::relay);
        assertDoesNotThrow(scheduler::relay);
        verify(failing, times(2)).relay();
        verify(healthy, times(2)).relay();
        assertEquals(2, mockingDetails(healthy).getInvocations().size());
    }
}
