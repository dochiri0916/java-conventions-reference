package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MemberErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                Map.entry("INVALID_MEMBER_ID", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("MEMBER_ID_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_EMAIL", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("EMAIL_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_PASSWORD", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("PASSWORD_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_MBR_NAME", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("MEMBER_NAME_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("INVALID_MEMBER", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("MEMBER_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("NAME_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED)),
                Map.entry("MEMBER_NOT_FOUND", new ApiExceptionMapper.Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND)),
                Map.entry("LOGIN_FAILED", new ApiExceptionMapper.Mapping(HttpStatus.UNAUTHORIZED, ProblemType.UNAUTHORIZED)),
                Map.entry("DUPLICATE_EMAIL", new ApiExceptionMapper.Mapping(HttpStatus.CONFLICT, ProblemType.CONFLICT)),
                Map.entry("AUTHENTICATION_REQUIRED", new ApiExceptionMapper.Mapping(HttpStatus.UNAUTHORIZED, ProblemType.UNAUTHORIZED)),
                Map.entry("INVALID_INPUT", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED))
        );
    }
}
