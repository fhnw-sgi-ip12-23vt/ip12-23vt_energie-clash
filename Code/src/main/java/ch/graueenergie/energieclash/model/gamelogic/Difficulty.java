package ch.graueenergie.energieclash.model.gamelogic;

import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.view.util.Translation;

import java.util.Arrays;

public enum Difficulty {
    EASY(1, Translation.DIFFICULTY_EASY),
    MEDIUM(2, Translation.DIFFICULTY_MEDIUM),
    HARD(3, Translation.DIFFICULTY_HARD);
    private final int value;
    private final Translation translation;

    Difficulty(int value, Translation text) {
        this.value = value;
        this.translation = text;
    }

    public int getValue() {
        return value;
    }

    public String getText(Language language) {
        return translation.getTextForLanguage(language);
    }

    public Translation getTranslation() {
        return translation;
    }

    public static Difficulty getDifficulty(int value) {
        return Arrays.stream(values()).filter(difficulty -> difficulty.getValue() == value).findFirst().orElse(null);
    }

    public static Difficulty getDifficulty(String text) {
        return Arrays.stream(values())
            .filter(difficulty -> difficulty.getTranslation().getTranslations().stream().anyMatch(s -> s.equals(text)))
            .findFirst()
            .orElse(null);
    }
}
