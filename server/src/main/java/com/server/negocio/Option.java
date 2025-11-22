package com.server.negocio;

public enum Option {
    A,
    B,
    C,
    D,
    E;

    public static Option fromString(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Option value cannot be null or blank");

        return fromChar(value.trim().toUpperCase().charAt(0));
    }

    public static Option fromChar(char c) {
        char upper = Character.toUpperCase(c);

        switch (upper) {
            case 'A' -> {
                return A;
            }
            case 'B' -> {
                return B;
            }
            case 'C' -> {
                return C;
            }
            case 'D' -> {
                return D;
            }
            case 'E' -> {
                return E;
            }
            default -> throw new IllegalArgumentException("Invalid option: " + c);
        }

    }


}
