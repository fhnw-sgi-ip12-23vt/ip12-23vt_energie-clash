package ch.graueenergie.energieclash.model;

import ch.graueenergie.energieclash.model.gamelogic.AnswerGrade;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashAnswer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRound;
import ch.graueenergie.energieclash.util.Language;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnergieClashModelTest {
    private static final String TEST_DB_URL = "src/test/resources/energieclashTest.sqlite";
    private static final String TEST_DB_BACKUP_URL =
            TEST_DB_URL.substring(0, TEST_DB_URL.lastIndexOf('.')) + "Backup.sqlite";
    private static final String TEST_JDBC_URL = "jdbc:sqlite:" + TEST_DB_URL;
    static Logger mockLogger;
    static EnergieClashModel model;

    /**
     * Creates {@code mockLogger} <br>
     * Sets {@code mockLogger} for {@code this} {@link EnergieClashModel} <br>
     * Sets url with {@code setUrl} of the {@link EnergieClashModel}to the address of the test sqlite.
     */
    @BeforeEach
    void setUpBeforeEach() {
        mockLogger = mock(Logger.class);
        model = new EnergieClashModel(mockLogger);
        model.setUrl(TEST_JDBC_URL);
    }

    @BeforeAll
    static void setUpBeforeAll() {
        Path srcPath = Path.of(TEST_DB_URL);
        Path dstPath = Path.of(TEST_DB_BACKUP_URL);
        try {
            Files.copy(srcPath, dstPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @AfterEach
    void tearDownAfterEach() {
        model.closeConnection();
    }

    @AfterAll
    static void tearDownAfterAll() {
        Path srcPath = Path.of(TEST_DB_URL);
        Path dstPath = Path.of(TEST_DB_BACKUP_URL);
        try {
            Files.deleteIfExists(srcPath);
            Files.copy(dstPath, srcPath);
            Files.deleteIfExists(dstPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the handling of a failed SQL connection
     * <p>
     * Asserts that {@code getConnection()} returns {@code null}, indicating no connection is established.
     * Verifies that an error is logged, specifically looking for a {@link SQLException} to be logged.
     * </ol>
     * <p>
     */
    @Test
    void logExceptionOnFailedConnectionTest() {
        model.setUrl(" ");
        assertNull(model.getConnection());
        verify(mockLogger).error(any(SQLException.class));
    }

    /**
     * Tests that the {@link EnergieClashModel#getConnection()} method successfully establishes a connection
     * to the SQLite database using a specified path in the URL. Verifies that no errors are logged when the connection
     * is successfully established.
     */
    @Test
    void getConnectionTest() {
        assertNotNull(model.getConnection());
        verify(mockLogger, never()).error((Message) any());
    }

    @ParameterizedTest
    @MethodSource("getRandomEnergieClashRoundsTestData")
    void getRandomEnergieClashRoundsTest(Difficulty difficulty, Language language,
                                         int amountOfResults) {
        List<EnergieClashRound> rounds =
                assertDoesNotThrow(() -> model.getRandomEnergieClashRounds(difficulty, language, amountOfResults));

        assertAll(
                () -> assertFalse(rounds.isEmpty()),
                () -> assertEquals(rounds.size(), amountOfResults),
                () -> assertTrue(rounds.stream().allMatch(round -> round.getDifficulty().equals(difficulty))),
                () -> assertTrue(rounds.stream().allMatch(round -> round.getLanguage().equals(language)))
        );
    }

    static Stream<Arguments> getRandomEnergieClashRoundsTestData() {
        return Stream.of(
                Arguments.of(Difficulty.EASY, Language.GERMAN, 1),
                Arguments.of(Difficulty.MEDIUM, Language.ENGLISH, 2),
                Arguments.of(Difficulty.HARD, Language.FRENCH, 1)
        );
    }

    /**
     * Tests whether a new {@link EnergieClashRound} can be added to the Database.
     * Uses the energieclashTest.sqlite DB.
     * Also tests the {@link EnergieClashModel#addQuestion(EnergieClashRound)} method, which is called by {@link EnergieClashModel#addQuestion(EnergieClashRound)}
     */
    @Test
    void addQuestionTest() {
        EnergieClashRound energieClashRound = new EnergieClashRound();
        energieClashRound.setQuestion("Dies ist eine neue Testfrage?");
        energieClashRound.setDifficulty(Difficulty.EASY);
        energieClashRound.setLanguage(Language.GERMAN);
        energieClashRound.setExplanation("Das ist die Erklärung der neuen Testfrage!");

        List<EnergieClashAnswer> answers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            EnergieClashAnswer answer = new EnergieClashAnswer();
            answer.setAnswer("Antwort " + (i + 1));
            answer.setAnswerGrade(AnswerGrade.values()[i]);
            answers.add(answer);
        }
        energieClashRound.setAnswers(answers);

        assertTrue(model.addQuestion(energieClashRound));
        verify(mockLogger, never()).error((Message) any());
    }

    /**
     * Tests whether an existing {@link EnergieClashRound} can be deleted.
     * Uses the energieclashTest.sqlite DB.
     * Also tests the {@link EnergieClashModel#deleteQuestion(EnergieClashRound)} method, which is called by {@link EnergieClashModel#deleteQuestion(EnergieClashRound)}
     */
    @Test
    void deleteQuestionTest() {

        EnergieClashRound energieClashRound = new EnergieClashRound();

        energieClashRound.setId(32);
        energieClashRound.setDifficulty(Difficulty.HARD);
        energieClashRound.setLanguage(Language.GERMAN);
        energieClashRound.setExplanation("explanation");
        List<EnergieClashAnswer> answers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            EnergieClashAnswer answer = new EnergieClashAnswer();
            answer.setAnswer("Antwort " + (i + 1));
            answer.setAnswerGrade(AnswerGrade.values()[i]);
            answers.add(answer);
        }
        energieClashRound.setAnswers(answers);

        assertTrue(model.deleteQuestion(energieClashRound));
        assertNull(model.getEnergieClashRoundById(32));
        verify(mockLogger, never()).error((Message) any());
    }

    /**
     * Tests whether an existing {@link EnergieClashRound} can be updated.
     * Uses the energieclashTest.sqlite DB.
     * Also tests the {@link EnergieClashModel#updateQuestion(EnergieClashRound)} method, which is called by {@link EnergieClashModel#updateQuestion(EnergieClashRound)}
     */
    @Test
    void updateQuestionTest() {
        EnergieClashRound energieClashRound = new EnergieClashRound();

        energieClashRound.setId(33);
        energieClashRound.setQuestion("TestQuestion 3");
        energieClashRound.setDifficulty(Difficulty.MEDIUM);
        energieClashRound.setLanguage(Language.ENGLISH);
        energieClashRound.setExplanation("explanation");

        List<EnergieClashAnswer> answers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            EnergieClashAnswer answer = new EnergieClashAnswer();
            answer.setAnswer("Antwort " + (i + 1));
            answer.setAnswerGrade(AnswerGrade.values()[i]);
            answers.add(answer);
        }
        energieClashRound.setAnswers(answers);

        assertTrue(model.updateQuestion(energieClashRound));
        assertNotNull(model.getEnergieClashRoundById(33));
        verify(mockLogger, never()).error((Message) any());
    }
}
