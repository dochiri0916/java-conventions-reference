package com.dochiri.tooltestlab.catalog.product.application;

import com.dochiri.tooltestlab.catalog.category.application.port.out.Categories;
import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.CategoryNameItems;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ProductSummaries;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.RegisterProductResult;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductDescription;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductName;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductStatus;
import com.dochiri.tooltestlab.catalog.product.domain.vo.Stock;
import com.dochiri.tooltestlab.global.domain.vo.Money;

import java.util.List;

public final class ProductResultMapper {
    private ProductResultMapper() {
    }

    public static RegisterProductResult toRegisterResult(final Product product, final Categories categories) {
        final ProductId id = product.id();
        final ProductName name = product.name();
        final Money price = product.price();
        final Stock stock = product.stock();
        final ProductStatus status = product.status();
        return new RegisterProductResult(
                id.value(),
                name.value(),
                price.amount(),
                stock.value(),
                status.name(),
                categoryNames(categories));
    }

    public static GetProductResult toGetResult(final Product product, final Categories categories) {
        final ProductId id = product.id();
        final ProductName name = product.name();
        final ProductDescription description = product.description();
        final Money price = product.price();
        final Stock stock = product.stock();
        final ProductStatus status = product.status();
        return new GetProductResult(
                id.value(),
                name.value(),
                description.value(),
                price.amount(),
                stock.value(),
                status.name(),
                categoryNames(categories));
    }

    public static ListProductsResult.ProductSummary toSummary(final Product product, final Categories categories) {
        final ProductId id = product.id();
        final ProductName name = product.name();
        final Money price = product.price();
        final Stock stock = product.stock();
        final ProductStatus status = product.status();
        return new ListProductsResult.ProductSummary(
                id.value(),
                name.value(),
                price.amount(),
                stock.value(),
                status.name(),
                categoryNames(categories));
    }

    public static ListProductsResult toListResult(
            final List<ListProductsResult.ProductSummary> summaries,
            final int page,
            final int size,
            final long totalCount) {
        return new ListProductsResult(new ProductSummaries(summaries), page, size, totalCount);
    }

    private static CategoryNameItems categoryNames(final Categories categories) {
        final List<String> names = categories.values().stream()
                .map(ProductResultMapper::categoryName)
                .sorted()
                .toList();
        return new CategoryNameItems(names);
    }

    private static String categoryName(final Category category) {
        final CategoryName name = category.name();
        return name.value();
    }
}
