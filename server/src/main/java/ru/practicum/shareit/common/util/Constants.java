package ru.practicum.shareit.common.util;

import java.util.regex.Pattern;

public final class Constants {
    public static final String REGEX_ALPHANUMERIC_ONLY = "^[\\sа-яА-Яa-zA-Z0-9]+$";

    private Constants() {
    }

    public static Pattern getAlphaNumericPattern() {
        return Pattern.compile(REGEX_ALPHANUMERIC_ONLY);
    }
}
