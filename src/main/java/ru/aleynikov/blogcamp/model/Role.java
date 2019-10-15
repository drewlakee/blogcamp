package ru.aleynikov.blogcamp.model;

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
