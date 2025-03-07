package com.connection.api.v1.model.enums;

public enum Modes {
    STABLE("Stable"), PREVIEW("Preview");

    private Modes(String mode) {
        this.mode = mode;
    }

    private String mode;

    public String getMode() {
        return this.mode;
    }

    public static Modes parse(String text) {
        for (Modes value : Modes.values()) {
            if (value.getMode().equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}

