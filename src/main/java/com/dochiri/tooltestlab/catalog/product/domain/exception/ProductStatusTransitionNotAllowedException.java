package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;
import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class ProductStatusTransitionNotAllowedException extends DomainException {

    public ProductStatusTransitionNotAllowedException(final ProductStatus currentStatus, final ProductStatus targetStatus) {
        super(
                ProductDomainErrorCode.PRODUCT_STATUS_TRANSITION_NOT_ALLOWED,
                "상품 상태를 %s에서 %s(으)로 변경할 수 없습니다."
                        .formatted(currentStatus, targetStatus),
                Map.of(
                        "currentStatus", currentStatus.name(),
                        "targetStatus", targetStatus.name()
                )
        );
    }

}
