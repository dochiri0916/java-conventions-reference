package com.dochiri.tooltestlab.global.exception;

public enum GlobalErrorCode implements ErrorCode {

    CONCURRENT_MODIFICATION(
            "CONCURRENT_MODIFICATION",
            ErrorKind.CONFLICT,
            "다른 요청에서 이미 리소스를 변경했습니다. 최신 상태를 조회한 후 다시 시도해 주세요."
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            ErrorKind.INTERNAL_ERROR,
            "일시적인 오류가 발생했습니다."
    );

    private final String code;
    private final ErrorKind kind;
    private final String detail;

    GlobalErrorCode(
            final String code,
            final ErrorKind kind,
            final String detail
    ) {
        this.code = code;
        this.kind = kind;
        this.detail = detail;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public ErrorKind kind() {
        return kind;
    }

    @Override
    public String detail() {
        return detail;
    }

}
