package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;

import java.util.Optional;

/** Cart 이벤트로 유지하는 주문 BC의 로컬 조회 모델. */
public interface CartSnapshotPort {
    Optional<CartSnapshot> claim(MemberReferenceId memberId);
    void update(CartSnapshot snapshot, long sequence);

    record CartSnapshot(CartReferenceId cartId, MemberReferenceId memberId, long revision, CartSnapshotLines items) {
    }
}
