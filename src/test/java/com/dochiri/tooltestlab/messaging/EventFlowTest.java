package com.dochiri.tooltestlab.messaging;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:event-flow;DB_CLOSE_DELAY=-1",
        "tool-test-lab.concurrency.safe=false"
})
class EventFlowTest extends EventFlowTestBase {
}
