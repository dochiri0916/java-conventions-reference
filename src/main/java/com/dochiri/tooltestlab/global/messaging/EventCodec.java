package com.dochiri.tooltestlab.global.messaging;

import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public final class EventCodec {
    private final JsonMapper mapper = JsonMapper.builder().build();

    public String encode(final Object value) {
        return mapper.writeValueAsString(value);
    }

    public <T> T decode(final String payload, final Class<T> type) {
        return mapper.readValue(payload, type);
    }
}
