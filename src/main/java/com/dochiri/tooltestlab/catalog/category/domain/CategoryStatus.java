package com.dochiri.tooltestlab.catalog.category.domain;

public enum CategoryStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    DELETED("삭제");

    private final String displayName;

    CategoryStatus(final String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }

}
