package com.dochiri.tooltestlab.catalog.category.adapter.in.web;

import com.dochiri.tooltestlab.catalog.category.application.exception.CategoryErrorCode;
import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryDomainErrorCode;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryIdException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryNameException;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class CategoryErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                validation(CategoryDomainErrorCode.INVALID_CAT_ID),
                validation(CategoryDomainErrorCode.CATEGORY_ID_REQUIRED),
                validation(CategoryDomainErrorCode.INVALID_CAT_NAME),
                validation(CategoryDomainErrorCode.CATEGORY_NAME_REQUIRED),
                validation(CategoryErrorCode.INVALID_INPUT),
                conflict(CategoryErrorCode.DUPLICATE_CATEGORY_NAME)
        );
    }

    @Override
    public Optional<ApiExceptionMapper.MappedError> resolve(final RuntimeException exception) {
        if (exception instanceof InvalidCategoryIdException invalidCategoryIdException) {
            return resolve(invalidCategoryIdException.code());
        }
        if (exception instanceof InvalidCategoryNameException invalidCategoryNameException) {
            return resolve(invalidCategoryNameException.code());
        }
        if (exception instanceof InvalidCommandException invalidCommandException) {
            return resolve(invalidCommandException.code());
        }
        if (exception instanceof DuplicateCategoryNameException duplicateCategoryNameException) {
            return resolve(duplicateCategoryNameException.code());
        }
        return Optional.empty();
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> validation(final Enum<?> errorCode) {
        return Map.entry(
                ApiErrorCode.from(errorCode),
                new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)
        );
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> conflict(final Enum<?> errorCode) {
        return Map.entry(
                ApiErrorCode.from(errorCode),
                new ApiExceptionMapper.Mapping(HttpStatus.CONFLICT, ProblemType.CONFLICT)
        );
    }
}
