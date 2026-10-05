package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

/**
 * 상품 검색 쿼리의 조회 결과. Entity 대신 필요한 컬럼만 읽는 영속성 내부 DTO다.
 */
public record ProductRow(
        String publicId,
        String name,
        String description,
        long priceAmount,
        int stock,
        String status
) {

    static ProductRow from(final ProductEntity entity) {
        return new ProductRow(
                entity.getPublicId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPriceAmount(),
                entity.getStock(),
                entity.getStatus()
        );
    }

}
