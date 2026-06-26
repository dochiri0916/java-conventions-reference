package com.dochiri.tooltestlab.order.application.exception;

public class OrderNotFoundException extends OrderApplicationException {

    public OrderNotFoundException(String publicId) {
        super(
                OrderApplicationErrorCode.ORDER_NOT_FOUND,
                "해당 주문을 찾을 수 없습니다. publicId=" + publicId
        );
    }

}
