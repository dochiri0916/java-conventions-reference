package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import org.hibernate.exception.ConstraintViolationException;

import java.util.regex.Pattern;

final class CartConstraintViolationClassifier {

    private static final Pattern MEMBER_ID_CONSTRAINT = Pattern.compile(
            Pattern.quote("uk_carts_member_id") + "(?:_INDEX_[0-9A-F]+)?",
            Pattern.CASE_INSENSITIVE);

    private CartConstraintViolationClassifier() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static boolean isMemberIdViolation(final Throwable exception) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof ConstraintViolationException violation) {
                final String constraintName = violation.getConstraintName();
                if (constraintName == null) {
                    return false;
                }
                // H2는 스키마와 자동 생성한 유니크 인덱스 접미사를 함께 반환한다.
                final String unquoted = constraintName.replace("\"", "").replace("`", "");
                final String name = unquoted.substring(unquoted.lastIndexOf('.') + 1);
                return MEMBER_ID_CONSTRAINT.matcher(name).matches();
            }
        }
        return false;
    }
}
