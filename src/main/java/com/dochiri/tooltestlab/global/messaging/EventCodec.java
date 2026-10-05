package com.dochiri.tooltestlab.global.messaging;

import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public final class EventCodec {
    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    public String encode(final Object value) {
        return MAPPER.writeValueAsString(value);
    }

    public <T> T decode(final String payload, final Class<T> type) {
        return MAPPER.readValue(payload, type);
    }
}
