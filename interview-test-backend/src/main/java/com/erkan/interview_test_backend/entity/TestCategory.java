package com.erkan.interview_test_backend.entity;

import lombok.Getter;

public enum TestCategory {
    CORE_JAVA("Core Java"),
    SPRING_FRAMEWORK("Spring Framework"),
    SQL("SQL"),
    DESIGN_PATTERNS("Design Patterns");

    @Getter
    private final String displayName;

    TestCategory(String displayName) {
        this.displayName = displayName;
    }

    public static TestCategory fromDisplayName(String displayName) {
        for (TestCategory category : values()) {
            if (category.getDisplayName().equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Geçersiz kategori: " + displayName);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
