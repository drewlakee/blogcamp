package ru.aleynikov.blogcamp.domain.models;

public enum  Role {

    USER,
    ADMIN,
    UNKNOWN;

    public static Role getRoleEnum(String role) {
        for (Role enumRole : Role.values()) {
            if (enumRole.name().equals(role))
                return enumRole;
        }

        return UNKNOWN;
    }
}
