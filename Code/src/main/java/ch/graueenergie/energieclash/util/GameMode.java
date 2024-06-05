package ch.graueenergie.energieclash.util;

import ch.graueenergie.energieclash.view.util.Translation;

public enum GameMode {
    TURNBASED(Translation.GAME_MODE_TURNBASED),
    RAPIDFIRE(Translation.GAME_MODE_RAPIDFIRE);
    private final Translation translation;

    GameMode(Translation translation) {
        this.translation = translation;
    }
    public Translation getTranslation() {
        return translation;
    }

    public String getText(Language language) {
        return translation.getTextForLanguage(language);
    }
}
