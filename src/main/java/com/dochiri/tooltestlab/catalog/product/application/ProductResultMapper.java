package com.dochiri.tooltestlab.catalog.product.application;

import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.RegisterProductResult;
import com.dochiri.tooltestlab.catalog.product.domain.Product;

import java.util.Collection;
import java.util.List;

public final class ProductResultMapper {
    private ProductResultMapper() {
    }

    public static RegisterProductResult toRegisterResult(final Product product, final Collection<Category> categories) {
        return new RegisterProductResult(
                product.id().value(),
                product.name().value(),
                product.price().amount(),
                product.stock().value(),
                product.status().name(),
                categoryNames(categories)
        );
    }

    public static ListProductsResult.ProductSummary toSummary(
            final Product product,
            final Collection<Category> categories
    ) {
        return new ListProductsResult.ProductSummary(
                product.id().value(), product.name().value(), product.price().amount(),
                product.stock().value(), product.status().name(), categoryNames(categories));
    }

    private static List<String> categoryNames(final Collection<Category> categories) {
        return categories.stream()
                .map(category -> category.name().value())
                .sorted()
                .toList();
    }
}
