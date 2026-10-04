package com.dochiri.tooltestlab.order.domain;

public enum OrderStatus {
    STOCK_PENDING, // 재고 예약 결과 대기
    PENDING,    // 주문 접수
    PAID,       // 결제 완료
    SHIPPING,   // 배송 중
    DELIVERED,  // 배송 완료
    CANCELLED,  // 취소
    REJECTED    // 재고 예약 실패
}
