package com.dochiri.tooltestlab.messaging;

import org.junit.jupiter.api.DisplayName;
import com.dochiri.tooltestlab.ToolTestLabApplication;
import com.dochiri.tooltestlab.cart.application.port.in.query.GetCartQuery;
import com.dochiri.tooltestlab.cart.application.port.in.command.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.AddToCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.GetCartUseCase;
import com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.outbox.CatalogOutboxEntry;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.RegisterProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.CategoryPublicIds;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.global.messaging.OutboxRelay;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxDispatcher;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OutboxRestartTest {
    @TempDir Path directory;

    @Test
    @DisplayName("커밋된 미발행 이벤트는 재시작 후에도 유지되고 실제 로컬 브로커로 전달된다")
    void committed_unpublished_event_survives_restart_and_uses_the_real_local_broker() {
        // given
        final String database = "jdbc:h2:file:" + directory.resolve("events") + ";DB_CLOSE_ON_EXIT=FALSE";
        final String eventId;
        final String productId;

        // when & then
        try (final var context = start(database)) {
            productId = context.getBean(RegisterProductUseCase.class).register(new RegisterProductCommand(
                    "재시작 상품", "재시작 검증을 위한 상품 설명입니다.", 100, 10, new CategoryPublicIds(Set.of()))).publicId();
            final var jdbc = context.getBean(JdbcTemplate.class);
            eventId = jdbc.queryForObject("select event_id from catalog_outbox", String.class);
            assertEquals(1L, jdbc.queryForObject(
                    "select count(*) from catalog_outbox where published_at is null", Long.class));
        }
        try (final var context = start(database)) {
            final var jdbc = context.getBean(JdbcTemplate.class);
            assertEquals(1L, jdbc.queryForObject(
                    "select count(*) from catalog_outbox where event_id = ? and published_at is null", Long.class, eventId));
            assertTrue(context.getBean(OutboxDispatcher.class).dispatch(CatalogOutboxEntry.class) >= 1);
            assertEquals(1L, jdbc.queryForObject(
                    "select count(*) from catalog_outbox where event_id = ? and published_at is not null", Long.class, eventId));
            assertEquals(1L, jdbc.queryForObject("select count(*) from cart_product_snapshots", Long.class));
            final String memberId = UuidCreator.getTimeOrderedEpoch().toString();
            context.getBean(AddToCartUseCase.class).add(new AddToCartCommand(memberId, productId, 2));
            assertEquals(2, context.getBean(GetCartUseCase.class).getCart(new GetCartQuery(memberId)).items().values().getFirst().quantity());
            context.getBeansOfType(OutboxRelay.class).values().forEach(OutboxRelay::relay);
            assertEquals(1L, jdbc.queryForObject("select count(*) from order_cart_snapshots", Long.class));
        }
    }

    private ConfigurableApplicationContext start(final String database) {
        return new SpringApplicationBuilder(ToolTestLabApplication.class).web(WebApplicationType.NONE).profiles("test").run(
                "--spring.datasource.url=" + database,
                "--spring.datasource.driver-class-name=org.h2.Driver",
                "--messaging.outbox.enabled=false",
                "--spring.main.banner-mode=off",
                "--spring.jpa.show-sql=false",
                "--logging.level.root=WARN",
                "--logging.level.org.hibernate.SQL=OFF",
                "--logging.level.com.dochiri.tooltestlab=WARN");
    }
}
