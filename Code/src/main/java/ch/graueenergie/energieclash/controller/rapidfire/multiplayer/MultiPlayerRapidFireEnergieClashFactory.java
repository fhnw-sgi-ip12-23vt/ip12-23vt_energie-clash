package ch.graueenergie.energieclash.controller.rapidfire.multiplayer;

import ch.graueenergie.energieclash.controller.EnergieClash;
import ch.graueenergie.energieclash.controller.EnergieClashFactory;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.util.Language;
import javafx.stage.Stage;

import java.util.Properties;

public class MultiPlayerRapidFireEnergieClashFactory extends EnergieClashFactory {
    /**
     * Creates a new instance with a new {@link ch.graueenergie.energieclash.model.EnergieClashModel}.
     *
     * @param appProps the {@link Properties} containing the needed information to create the right {@link EnergieClash}
     */
    public MultiPlayerRapidFireEnergieClashFactory(Properties appProps) {
        super(appProps);
    }

    @Override
    public EnergieClash createGame(Stage saverStage, Stage wasterStage, Language lang, Difficulty diff) {
        return null;
    }
}
