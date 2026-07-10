package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidMoneyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode errorCode;
    private final long invalidAmount;
    private final int invalidQuantity;

    private InvalidMoneyException(final ProductDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidAmount = 0L;
        this.invalidQuantity = 0;
    }

    private InvalidMoneyException(
            final ProductDomainErrorCode errorCode,
            final long amount,
            final int quantity) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidAmount = amount;
        this.invalidQuantity = quantity;
    }

    public static InvalidMoneyException negative(final long amount) {
        return new InvalidMoneyException(ProductDomainErrorCode.MONEY_NEGATIVE, amount, 0);
    }

    public static InvalidMoneyException addNull() {
        return new InvalidMoneyException(ProductDomainErrorCode.MONEY_ADD_NULL);
    }

    public static InvalidMoneyException multiplyNegative(final int quantity) {
        return new InvalidMoneyException(ProductDomainErrorCode.MONEY_MULTIPLY_NEGATIVE, 0L, quantity);
    }

    public static InvalidMoneyException required() {
        return new InvalidMoneyException(ProductDomainErrorCode.MONEY_REQUIRED);
    }

    public ProductDomainErrorCode code() {
        return errorCode;
    }

    public long amount() {
        return invalidAmount;
    }

    public int quantity() {
        return invalidQuantity;
    }
}
