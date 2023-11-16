package com.ark.security.models.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {

    USER_READ("user:read"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_READ("admin:read"),
    ADMIN_DELETE("admin:delete"),
    EMPLOYEE_CREATE("employee:create"),
    EMPLOYEE_UPDATE("employee:update"),
    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_DELETE("employee:delete");

    private final String permission;
}
