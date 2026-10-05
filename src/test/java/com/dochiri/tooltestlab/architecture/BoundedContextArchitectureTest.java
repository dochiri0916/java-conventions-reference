package com.dochiri.tooltestlab.architecture;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

class BoundedContextArchitectureTest {
    private static final Path ROOT = Path.of("src/main/java/com/dochiri/tooltestlab");
    private static final List<String> CONTEXTS = List.of("catalog", "cart", "order", "member", "review");
    private static final Pattern IMPORT = Pattern.compile("import\\s+(?:static\\s+)?com\\.dochiri\\.tooltestlab\\.([a-z]+)\\.([^;]+);");

    @Test
    void otherContextsAreAccessedOnlyThroughPublicEventsInMessagingConsumers() throws Exception {
        try (final var paths = Files.walk(ROOT)) {
            for (final var path : paths.filter(p -> p.toString().endsWith(".java")).toList()) {
                final String bc = ROOT.relativize(path).getName(0).toString();
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
    void publicEventsContainNoDomainOrTechnologyTypes() throws Exception {
        for (final var bc : CONTEXTS) {
            final var aggregateRoot = ROOT.resolve(bc.equals("catalog") ? "catalog/product" : bc);
            final var eventRoot = aggregateRoot.resolve("application/port/out/event");
            assertTrue(Files.isRegularFile(aggregateRoot.resolve("application/port/out/IntegrationEventPort.java")));
            assertTrue(Files.isDirectory(aggregateRoot.resolve("adapter/out/messaging")));
            assertTrue(Files.isDirectory(aggregateRoot.resolve("adapter/out/persistence/outbox")));
            try (final var paths = Files.walk(eventRoot)) {
                for (final var path : paths.filter(p -> p.toString().endsWith(".java")).toList()) {
                    final String source = Files.readString(path);
                    assertFalse(source.contains(".domain."), path.toString());
                    assertFalse(source.contains("org.springframework"), path.toString());
                    assertFalse(source.contains("jakarta.persistence"), path.toString());
                }
            }
        }
    }

    @Test
    void catalogOnlyGroupsAggregateRoots() throws Exception {
        try (final var paths = Files.list(ROOT.resolve("catalog"))) {
            assertEquals(List.of("category", "product"),
                    paths.map(path -> path.getFileName().toString()).sorted().toList());
        }
        for (final var aggregate : List.of("category", "product")) {
            final String className = aggregate.equals("category") ? "Category" : "Product";
            assertTrue(Files.isRegularFile(ROOT.resolve("catalog/" + aggregate + "/domain/" + className + ".java")));
        }
    }

    @Test
    void domainsHaveNoApplicationOrAdapterDependenciesAndUseLocalReferenceIds() throws Exception {
        try (final var paths = Files.walk(ROOT)) {
            for (final var path : paths.filter(p -> p.toString().contains("/domain/") && p.toString().endsWith(".java")).toList()) {
                final String source = Files.readString(path);
                assertFalse(source.contains(".application."), path.toString());
                assertFalse(source.contains(".adapter."), path.toString());
                assertFalse(source.contains("jakarta.persistence"), path.toString());
                assertFalse(source.contains("org.springframework"), path.toString());
            }
        }
        assertTrue(Files.isRegularFile(ROOT.resolve("order/domain/vo/CartReferenceId.java")));
        for (final var bc : List.of("cart", "order", "review")) {
            assertTrue(Files.isRegularFile(ROOT.resolve(bc + "/domain/vo/MemberReferenceId.java")));
            assertTrue(Files.isRegularFile(ROOT.resolve(bc + "/domain/vo/ProductReferenceId.java")));
        }
    }

    @Test
    void messagingPersistenceStaysOutsideApplicationAndTransportOutsideConsumers() throws Exception {
        try (final var paths = Files.walk(ROOT)) {
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
