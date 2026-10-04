package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import java.util.List;
import java.util.Optional;

/** Cart 이벤트로 유지하는 주문 BC의 로컬 조회 모델. */
public interface CartSnapshotPort {
    Optional<CartSnapshot> claim(MemberReferenceId memberId);
    void update(CartSnapshot snapshot, long sequence);
    void release(CartReferenceId cartId, long revision);

    record CartSnapshot(CartReferenceId cartId, MemberReferenceId memberId, long revision, List<OrderLine> items) {
        public CartSnapshot {
            items = List.copyOf(items);
        }
    }
}
