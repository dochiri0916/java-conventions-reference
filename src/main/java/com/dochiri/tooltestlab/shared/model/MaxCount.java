package com.dochiri.tooltestlab.shared.model;

public record MaxCount(int value) {

    public MaxCount {
        if (value <= 0) {
            throw new IllegalArgumentException("최대 개수는 0보다 커야 합니다. 입력값: " + value);
        }
    }

    public static MaxCount of(final int value) {
        return new MaxCount(value);
    }
}
