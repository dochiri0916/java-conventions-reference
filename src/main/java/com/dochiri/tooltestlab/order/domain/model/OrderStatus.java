package com.dochiri.tooltestlab.order.domain.model;

/**
 * 주문 상태.
 */
public enum OrderStatus {
    PENDING,    // 주문 접수
    PAID,       // 결제 완료
    SHIPPING,   // 배송 중
    DELIVERED,  // 배송 완료
    CANCELLED   // 취소
}
