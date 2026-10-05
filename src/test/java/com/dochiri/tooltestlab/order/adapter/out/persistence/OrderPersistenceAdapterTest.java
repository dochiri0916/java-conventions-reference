package com.dochiri.tooltestlab.order.adapter.out.persistence;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "tool-test-lab.concurrency.safe=false")
class OrderPersistenceAdapterTest extends OrderPersistenceAdapterTestBase {
}
