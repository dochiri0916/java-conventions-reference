package com.dochiri.tooltestlab.cart.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class CartDomainException extends DomainException {

    private CartDomainException(final CartDomainErrorCode errorCode, final Map<String, ?> extensions) {
        super(errorCode, errorCode.detail(), extensions);
    }

    public static CartDomainException cartIdRequired() {
        return of(CartDomainErrorCode.CART_ID_REQUIRED);
    }

    public static CartDomainException invalidCartId() {
        return of(CartDomainErrorCode.INVALID_CART_ID);
    }

    public static CartDomainException cartItemRequired() {
        return of(CartDomainErrorCode.CART_ITEM_REQUIRED);
    }

    public static CartDomainException invalidCartItem(final String reason) {
        return new CartDomainException(CartDomainErrorCode.INVALID_CART_ITEM, Map.of("reason", reason));
    }

    public static CartDomainException cartRequired() {
        return of(CartDomainErrorCode.CART_REQUIRED);
    }

    public static CartDomainException invalidCart() {
        return of(CartDomainErrorCode.INVALID_CART);
    }

    public static CartDomainException tooManyItems(final int actualSize) {
        return new CartDomainException(CartDomainErrorCode.TOO_MANY_ITEMS, Map.of("actualSize", actualSize));
    }
    public static CartDomainException productIdRequired() { return of(CartDomainErrorCode.PRODUCT_ID_REQUIRED); }
    public static CartDomainException productNameRequired() { return of(CartDomainErrorCode.PRODUCT_NAME_REQUIRED); }
    public static CartDomainException quantityRequired() { return of(CartDomainErrorCode.QUANTITY_REQUIRED); }
    public static CartDomainException unitPriceRequired() { return of(CartDomainErrorCode.UNIT_PRICE_REQUIRED); }

    private static CartDomainException of(final CartDomainErrorCode errorCode) {
        return new CartDomainException(errorCode, Map.of());
    }
}
