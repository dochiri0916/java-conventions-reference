package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidMoneyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode errorCode;

    private InvalidMoneyException(final ProductDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidMoneyException negative(final long amount) {
        return new InvalidMoneyException(ProductDomainErrorCode.MONEY_NEGATIVE);
    }

    public static InvalidMoneyException addNull() {
        return new InvalidMoneyException(ProductDomainErrorCode.MONEY_ADD_NULL);
    }

    public static InvalidMoneyException multiplyNegative(final int quantity) {
        return new InvalidMoneyException(ProductDomainErrorCode.MONEY_MULTIPLY_NEGATIVE);
    }

    public static InvalidMoneyException required() {
        return new InvalidMoneyException(ProductDomainErrorCode.MONEY_REQUIRED);
    }

    public ProductDomainErrorCode code() {
        return errorCode;
    }
}
