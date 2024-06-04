package ch.graueenergie.energieclash.controller;

import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.util.Language;
import javafx.stage.Stage;

import java.util.Properties;

public abstract class GameFactory {
    private final Properties appProps;

    public GameFactory(Properties appProps) {
        this.appProps = appProps;
    }

    public abstract EnergieClash createGame(Stage saverStage, Stage wasterStage, Language lang, Difficulty diff);

    protected Properties getAppProps() {
        return appProps;
    }
}
