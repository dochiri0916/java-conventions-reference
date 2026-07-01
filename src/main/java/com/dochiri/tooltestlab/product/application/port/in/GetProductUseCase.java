package com.dochiri.tooltestlab.product.application.port.in;

/**
 * 상품 단건 조회 UseCase.
 */
@FunctionalInterface
public interface GetProductUseCase {
    GetProductResult getProduct(String publicId);
}
