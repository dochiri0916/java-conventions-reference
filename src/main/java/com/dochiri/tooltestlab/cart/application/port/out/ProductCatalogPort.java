package com.dochiri.tooltestlab.cart.application.port.out;

import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.global.domain.vo.Money;

/**
 * Catalog 이벤트로 유지하는 Cart BC의 로컬 상품 조회 모델.
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface ProductCatalogPort {

    ProductSnapshot findProduct(ProductReferenceId productId);

    void save(ProductSnapshot snapshot, long sequence);

    record ProductSnapshot(ProductReferenceId id, ProductName name, Money price) {
    }
}
