package com.dochiri.tooltestlab.category.domain.exception;

public final class InvalidCategoryNameException extends CategoryDomainException {

    public InvalidCategoryNameException() {
        super(CategoryDomainErrorCode.INVALID_CAT_NAME, "카테고리명은 빈 값일 수 없습니다.");
    }
}
