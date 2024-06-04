package ch.graueenergie.energieclash.controller.turnbased;

import ch.graueenergie.energieclash.controller.EnergieClash;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.util.Language;
import javafx.stage.Stage;

public abstract class TurnBasedEnergieClash extends EnergieClash {

    public TurnBasedEnergieClash(
                                 Language language,
                                 Difficulty difficulty,
                                 Stage saverStage, Stage wasterStage,
                                 EnergieClashPlayer saver,
                                 EnergieClashPlayer waster) {
        super(language, difficulty, saverStage, wasterStage, saver, waster);
    }
}
