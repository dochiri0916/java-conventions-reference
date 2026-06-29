package com.dochiri.tooltestlab.category.domain.exception;

public final class InvalidCategoryIdException extends CategoryDomainException {

    public InvalidCategoryIdException() {
        super(CategoryDomainErrorCode.INVALID_CAT_ID, "카테고리 ID는 공백일 수 없습니다.");
    }
}
