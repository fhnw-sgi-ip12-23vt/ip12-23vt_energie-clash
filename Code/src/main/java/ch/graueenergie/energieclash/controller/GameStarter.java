package ch.graueenergie.energieclash.controller;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class GameStarter extends Application {
    private static final Logger LOGGER = LogManager.getLogger(GameStarter.class);

    @Override
    public void start(Stage stage) throws Exception {
        // Fetch appProps
        try {
            Properties appProps = AppPropertiesLoader.getAppProperties();
            // Get gameName
            String gameName = Optional.ofNullable(appProps.getProperty("gameName"))
                .orElseThrow(() -> new NoSuchElementException("No game was found"));
            LOGGER.info(String.format("Game\"%s\" was found and started", gameName));

            switch (gameName) {
            case "energieclash" -> startEnergieClash(appProps, stage, new Stage());
            default -> throw new NoSuchElementException(String.format("Game\"%s\" does not exist.", gameName));
            }
        } catch (NoSuchElementException e) {
            LOGGER.error(e);
        }
    }

    private void startEnergieClash(Properties appProps, Stage saverStage, Stage wasterStage) {
        setStageToSecondScreen(wasterStage);

        wasterStage.setMaximized(true);
        wasterStage.setFullScreen(true);

        saverStage.initStyle(StageStyle.UNDECORATED);
        saverStage.setMaximized(true);
        saverStage.setFullScreen(true);

        EnergieClashStartController energieClashStarter =
            new EnergieClashStartController(appProps, saverStage, wasterStage);
        energieClashStarter.start();
    }

    private void setStageToSecondScreen(Stage stage) {
        List<Screen> screens = Screen.getScreens();
        if (screens.size() > 1) {
            Rectangle2D secondScreenBounds = screens.get(1).getBounds();
            stage.setX(secondScreenBounds.getMinX());
            stage.setY(secondScreenBounds.getMinY());
        }
    }
}
