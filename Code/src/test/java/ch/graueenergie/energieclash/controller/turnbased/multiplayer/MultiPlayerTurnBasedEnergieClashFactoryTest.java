package ch.graueenergie.energieclash.controller.turnbased.multiplayer;

import ch.graueenergie.energieclash.controller.EnergieClash;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.util.WinCriteria;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class MultiPlayerTurnBasedEnergieClashFactoryTest {

    @Test
    void createGameTest() {

        // Mocking the necessary objects
        Stage saverStage = mock(Stage.class);
        Stage wasterStage = mock(Stage.class);
        Language lang = Language.ENGLISH;
        Difficulty diff = Difficulty.EASY;

        WinCriteria winCriteria = WinCriteria.GAME_SCORE;
        int goalGameScore = 25;
        int amountOfRounds = 10;

        Properties mockAppProps = new Properties();

        mockAppProps.setProperty("winCriteria", winCriteria.toString());
        mockAppProps.setProperty("goalGameScore", String.valueOf(goalGameScore));
        mockAppProps.setProperty("amountOfRounds", String.valueOf(amountOfRounds));

        MultiPlayerTurnBasedEnergieClashFactory factory = new MultiPlayerTurnBasedEnergieClashFactory(mockAppProps);

        EnergieClash energieClash = factory.createGame(saverStage, wasterStage, lang, diff);

        assertNotNull(energieClash);
        assertEquals(MultiPlayerTurnBasedEnergieClash.class, energieClash.getClass());

    }
}