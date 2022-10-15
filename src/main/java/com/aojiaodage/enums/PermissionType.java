package com.aojiaodage.enums;

public enum PermissionType {
    Menu(1, "菜单"),
    OPERATION(2, "操作");

    private final int value;
    private final String description;

    PermissionType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static boolean in(int input) {
        PermissionType[] values = PermissionType.values();
        for (PermissionType type : values) {
            if (type.getValue() == input) {
                return true;
            }
        }
        return false;
    }
}
