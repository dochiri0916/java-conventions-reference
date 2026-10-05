package com.dochiri.tooltestlab.catalog.product.application.port.out;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductStatus;
import com.dochiri.tooltestlab.global.domain.vo.Money;

/**
 * 상품 목록 검색 조건. 값이 없는 항목(null)은 조건에서 제외한다.
 */
public record ProductSearchCondition(
        String keyword,
        ProductStatus status,
        CategoryId categoryId,
        Money minPrice,
        Money maxPrice
) {

    public static ProductSearchCondition none() {
        return new ProductSearchCondition(null, null, null, null, null);
    }

}
