package com.dochiri.tooltestlab.order.application.port.in;

public record HandleStockReservationResultCommand(String orderId, boolean reserved, String rejectionReason) {
}
