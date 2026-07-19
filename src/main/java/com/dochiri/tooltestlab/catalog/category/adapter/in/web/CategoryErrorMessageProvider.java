package com.dochiri.tooltestlab.catalog.category.adapter.in.web;

import com.dochiri.tooltestlab.catalog.category.application.exception.CategoryErrorCode;
import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryDomainErrorCode;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiErrorMessage;
import com.dochiri.tooltestlab.global.error.ApiErrorMessageProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CategoryErrorMessageProvider implements ApiErrorMessageProvider {

    private static final Map<String, ApiErrorMessage> ERROR_MESSAGES = Map.ofEntries(
            message(CategoryDomainErrorCode.CATEGORY_ID_INVALID, "카테고리 ID 오류", "카테고리 ID는 공백일 수 없습니다."),
            message(CategoryDomainErrorCode.CATEGORY_ID_REQUIRED, "카테고리 ID 누락", "카테고리 ID는 필수입니다."),
            message(CategoryDomainErrorCode.INVALID_CAT_NAME, "카테고리명 오류", "카테고리명은 공백일 수 없습니다."),
            message(CategoryDomainErrorCode.CATEGORY_NAME_REQUIRED, "카테고리명 누락", "카테고리명은 필수입니다."),
            message(CategoryErrorCode.INVALID_INPUT, "입력값 오류", "입력값이 올바르지 않습니다."),
            message(CategoryErrorCode.DUPLICATE_CATEGORY_NAME, "카테고리명 중복", "이미 등록된 카테고리명입니다.")
    );

    @Override
    public Map<String, ApiErrorMessage> errorMessages() {
        return Map.copyOf(ERROR_MESSAGES);
    }

    private static Map.Entry<String, ApiErrorMessage> message(
            final Enum<?> errorCode,
            final String title,
            final String detail) {
        return Map.entry(ApiErrorCode.from(errorCode), new ApiErrorMessage(title, detail));
    }
}
