package com.dochiri.tooltestlab.messaging;

import com.dochiri.tooltestlab.global.messaging.OutboxRelay;
import com.dochiri.tooltestlab.global.messaging.OutboxSchedulingConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class OutboxSchedulingTest {
    @Test
    void oneFailingRelayDoesNotStarveOtherContextsOrFuturePolls() {
        final var failing = mock(OutboxRelay.class);
        final var healthy = mock(OutboxRelay.class);
        when(failing.relay()).thenThrow(new IllegalStateException("DB 실패"));
        final var scheduler = new OutboxSchedulingConfiguration(List.of(failing, healthy));

        assertDoesNotThrow(scheduler::relay);
        assertDoesNotThrow(scheduler::relay);

        verify(failing, times(2)).relay();
        verify(healthy, times(2)).relay();
    }
}
