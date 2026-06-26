package com.dochiri.tooltestlab.product.application.port.in;

import com.dochiri.tooltestlab.product.application.result.GetProductResult;

/**
 * 상품 단건 조회 UseCase.
 */
public interface GetProductUseCase {
    GetProductResult getProduct(String publicId);
}
