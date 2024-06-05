package ch.graueenergie.energieclash.controller.rapidfire;

import ch.graueenergie.energieclash.controller.EnergieClash;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.util.Language;
import javafx.stage.Stage;

public abstract class RapidFireEnergieClash extends EnergieClash {
    /**
     * Creates a new instance.
     *
     * @param language    the language of this instance.
     * @param difficulty
     * @param saverStage  the Stage which is shown on the Savers screen.
     * @param wasterStage the Stage which is shown on the Wasters screen.
     * @param saver
     * @param waster
     */
    public RapidFireEnergieClash(Language language,
                                 Difficulty difficulty,
                                 Stage saverStage, Stage wasterStage,
                                 EnergieClashPlayer saver,
                                 EnergieClashPlayer waster) {
        super(language, difficulty, saverStage, wasterStage, saver, waster);
    }
}
