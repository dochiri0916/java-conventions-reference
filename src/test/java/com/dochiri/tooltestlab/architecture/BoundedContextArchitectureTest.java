package com.dochiri.tooltestlab.architecture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundedContextArchitectureTest {
    private static final Path ROOT = Path.of("src/main/java/com/dochiri/tooltestlab");
    private static final List<String> CONTEXTS = List.of("catalog", "cart", "order", "member", "review");
    private static final Pattern IMPORT = Pattern.compile("import\\s+(?:static\\s+)?com\\.dochiri\\.tooltestlab\\.([a-z]+)\\.([^;]+);");

    @Test
    @DisplayName("다른 BC는 메시징 소비자에서 공개 이벤트로만 접근한다")
    void other_contexts_are_accessed_only_through_public_events_in_messaging_consumers() throws Exception {
        // given
        final Path root = ROOT;

        // when & then
        try (final var paths = Files.walk(root)) {
            for (final var path : paths.filter(p -> p.toString().endsWith(".java")).toList()) {
                final String bc = root.relativize(path).getName(0).toString();
                final var matcher = IMPORT.matcher(Files.readString(path));
                while (matcher.find()) {
                    final String importedBc = matcher.group(1);
                    if (!CONTEXTS.contains(importedBc) || importedBc.equals(bc)) {
                        continue;
                    }
                    assertTrue(path.toString().contains("/adapter/in/messaging/"),
                            () -> path + " imports foreign BC: " + matcher.group());
                    final String eventPackage = importedBc.equals("catalog")
                            ? "product.application.port.out.event." : "application.port.out.event.";
                    assertTrue(matcher.group(2).startsWith(eventPackage),
                            () -> path + " imports a non-public contract: " + matcher.group());
                }
            }
        }
    }

    @Test
    @DisplayName("공개 이벤트는 도메인·기술 타입을 포함하지 않는다")
    void public_events_contain_no_domain_or_technology_types() throws Exception {
        // given
        final List<String> forbidden = List.of(".domain.", "org.springframework", "jakarta.persistence");

        // when & then
        for (final var bc : CONTEXTS) {
            final var aggregateRoot = ROOT.resolve(bc.equals("catalog") ? "catalog/product" : bc);
            final var eventRoot = aggregateRoot.resolve("application/port/out/event");
            assertTrue(Files.isRegularFile(aggregateRoot.resolve("application/port/out/IntegrationEventPort.java")));
            assertTrue(Files.isDirectory(aggregateRoot.resolve("adapter/out/messaging")));
            assertTrue(Files.isDirectory(aggregateRoot.resolve("adapter/out/persistence/outbox")));
            try (final var paths = Files.walk(eventRoot)) {
                for (final var path : paths.filter(p -> p.toString().endsWith(".java")).toList()) {
                    final String source = Files.readString(path);
                    for (final String token : forbidden) {
                        assertFalse(source.contains(token), path.toString());
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("catalog는 애그리거트 루트만 묶는다")
    void catalog_only_groups_aggregate_roots() throws Exception {
        // given
        final List<String> aggregates = List.of("category", "product");

        // when & then
        try (final var paths = Files.list(ROOT.resolve("catalog"))) {
            assertEquals(aggregates, paths.map(path -> path.getFileName().toString()).sorted().toList());
        }
        for (final var aggregate : aggregates) {
            final String className = aggregate.equals("category") ? "Category" : "Product";
            assertTrue(Files.isRegularFile(ROOT.resolve("catalog/" + aggregate + "/domain/" + className + ".java")));
        }
    }

    @Test
    @DisplayName("도메인은 애플리케이션·어댑터에 의존하지 않고 로컬 참조 ID를 사용한다")
    void domains_have_no_application_or_adapter_dependencies_and_use_local_reference_ids() throws Exception {
        // given
        final List<String> forbidden = List.of(".application.", ".adapter.", "jakarta.persistence", "org.springframework");

        // when & then
        try (final var paths = Files.walk(ROOT)) {
            for (final var path : paths.filter(p -> p.toString().contains("/domain/") && p.toString().endsWith(".java")).toList()) {
                final String source = Files.readString(path);
                for (final String token : forbidden) {
                    assertFalse(source.contains(token), path.toString());
                }
            }
        }
        assertTrue(Files.isRegularFile(ROOT.resolve("order/domain/vo/CartReferenceId.java")));
        for (final var bc : List.of("cart", "order", "review")) {
            assertTrue(Files.isRegularFile(ROOT.resolve(bc + "/domain/vo/MemberReferenceId.java")));
            assertTrue(Files.isRegularFile(ROOT.resolve(bc + "/domain/vo/ProductReferenceId.java")));
        }
    }

    @Test
    @DisplayName("메시징 영속성은 애플리케이션 밖에, 전송 기술은 소비자 밖에 둔다")
    void messaging_persistence_stays_outside_application_and_transport_outside_consumers() throws Exception {
        // given
        final Path root = ROOT;

        // when & then
        try (final var paths = Files.walk(root)) {
            for (final var path : paths.filter(p -> p.toString().endsWith(".java")).toList()) {
                final String source = Files.readString(path);
                if (path.toString().contains("/application/")) {
                    assertFalse(source.contains("jakarta.persistence"), path.toString());
                    assertFalse(source.contains(".messaging.persistence."), path.toString());
                    assertFalse(source.contains(".adapter."), path.toString());
                }
                if (path.toString().contains("/adapter/in/messaging/")) {
                    assertFalse(source.contains("@EventListener"), path.toString());
                    assertFalse(source.contains("org.springframework.kafka"), path.toString());
                }
            }
        }
    }
}
