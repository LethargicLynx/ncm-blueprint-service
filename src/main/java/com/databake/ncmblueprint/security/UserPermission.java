package com.databake.ncmblueprint.security;

public enum UserPermission {
    MACHINE_VIEW("machine:view"),
    MACHINE_CREATE("machine:create"),
    MACHINE_UPDATE("machine:update"),
    MACHINE_DELETE("machine:delete"),
    MACHINE_DOWNLOAD("machine:download"),
    PART_VIEW("part:view"),
    PART_CREATE("part:create"),
    PART_UPDATE("part:update"),
    PART_DELETE("part:delete"),
    PART_DOWNLOAD("part:download"),
    MATERIAL_VIEW("material:view"),
    MATERIAL_CREATE("material:create"),
    MATERIAL_UPDATE("material:update"),
    MATERIAL_DELETE("material:delete"),
    MATERIAL_DOWNLOAD("material:download"),
    SUMMARY_VIEW("summary:view"),
    SUMMARY_CREATE("summary:create"),
    SUMMARY_DOWNLOAD("summary:download");


    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
