package ch.graueenergie.energieclash.util;

import java.util.Arrays;
import java.util.Objects;

/**
 * Available languages.
 */
public enum Language {
    GERMAN("de"),
    ENGLISH("en"),
    FRENCH("fr");
    private final String abbreviation;

    Language(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     *
     * @return the abbreviation of the language.
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     *
     * @param abbreviation of the language.
     * @return the language. {@code default} is {@code GERMAN}.
     */
    public static Language getLanguage(String abbreviation) {
        return Arrays.stream(values()).filter(language -> Objects
                .equals(language.getAbbreviation(), abbreviation)).findFirst().orElse(GERMAN);
    }
}
