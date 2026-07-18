package com.dochiri.tooltestlab.catalog.product.application.port.in;

import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;

/**
 * 상품 단건 조회 UseCase.
 */
@FunctionalInterface
public interface GetProductUseCase {
    GetProductResult getProduct(String publicId);
}
