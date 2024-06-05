package ch.graueenergie.energieclash.controller.turnbased.multiplayer;

import ch.graueenergie.energieclash.controller.EnergieClashFactory;
import ch.graueenergie.energieclash.controller.EnergieClash;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRole;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.util.WinCriteria;
import javafx.stage.Stage;

import java.util.Properties;

public class MultiPlayerTurnBasedEnergieClashFactory extends EnergieClashFactory {
    /**
     * Creates a new instance with a new {@link ch.graueenergie.energieclash.model.EnergieClashModel}.
     *
     * @param appProps the {@link Properties} containing the needed information to create the right {@link EnergieClash}
     */
    public MultiPlayerTurnBasedEnergieClashFactory(Properties appProps) {
        super(appProps);
    }

    @Override
    public EnergieClash createGame(Stage saverStage, Stage wasterStage, Language lang, Difficulty diff) {
        WinCriteria winCriteria = WinCriteria.valueOf(getAppProps().getProperty("winCriteria"));
        int goalGameScore = Integer.parseInt(getAppProps().getProperty("goalGameScore"));
        int amountOfRounds = Integer.parseInt(getAppProps().getProperty("amountOfRounds"));
        return new MultiPlayerTurnBasedEnergieClash(lang, diff, saverStage, wasterStage,
            new EnergieClashPlayer(EnergieClashRole.SAVER),
            new EnergieClashPlayer(EnergieClashRole.WASTER), winCriteria, amountOfRounds, goalGameScore);
    }

}
