package com.dochiri.tooltestlab.cart.application;

import com.dochiri.tooltestlab.cart.application.port.in.result.CartResultItems;
import com.dochiri.tooltestlab.cart.application.port.in.result.GetCartResult;
import com.dochiri.tooltestlab.cart.domain.Cart;
import com.dochiri.tooltestlab.cart.domain.vo.CartId;
import com.dochiri.tooltestlab.cart.domain.vo.CartItem;
import com.dochiri.tooltestlab.cart.domain.vo.CartItems;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;

import java.util.List;

public final class CartResultMapper {

    private CartResultMapper() {
    }

    public static GetCartResult toGetResult(final Cart cart) {
        final CartId id = cart.id();
        final MemberReferenceId memberId = cart.memberId();
        final CartItems items = cart.items();
        final List<CartItem> values = items.values();
        final List<GetCartResult.Item> resultItems = values.stream()
                .map(CartResultMapper::toItem)
                .toList();
        final Money total = cart.totalAmount();
        return new GetCartResult(
                id.value(),
                memberId.value(),
                new CartResultItems(resultItems),
                total.amount());
    }

    private static GetCartResult.Item toItem(final CartItem item) {
        final ProductReferenceId productId = item.productId();
        final ProductName productName = item.productName();
        final Quantity quantity = item.quantity();
        final Money unitPrice = item.unitPrice();
        final Money totalPrice = item.totalPrice();
        return new GetCartResult.Item(
                productId.value(),
                productName.value(),
                quantity.value(),
                unitPrice.amount(),
                totalPrice.amount());
    }
}
