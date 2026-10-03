package com.dochiri.tooltestlab.catalog.category.application.exception;

public final class DuplicateCategoryNameException extends CategoryApplicationException {

    public DuplicateCategoryNameException() {
        super("이미 등록된 카테고리명입니다.");
    }

}
