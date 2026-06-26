package com.dochiri.tooltestlab.order.application.command;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.ShippingAddress;

import static java.util.Objects.requireNonNull;

public record PlaceOrderCommand(MemberId memberId, ShippingAddress shippingAddress) {
    public PlaceOrderCommand {
        requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        requireNonNull(shippingAddress, "배송지는 null일 수 없습니다.");
    }
}
