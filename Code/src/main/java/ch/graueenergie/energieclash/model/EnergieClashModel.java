package ch.graueenergie.energieclash.model;

import ch.graueenergie.energieclash.controller.EnergieClash;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashAnswer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRound;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * The data access model for {@link EnergieClash}.
 */
public class EnergieClashModel {
    public static final String PI_URL =
        "jdbc:sqlite:/home/pi/deploy/energieclash.sqlite";
    private String url;
    private final Logger logger;
    private Connection connection;

    //Default Constructor for EnergieClashModel
    public EnergieClashModel(Logger logger) {
        this.logger = logger;
        setUrl(PI_URL);
    }

    /**
     * Fetches a {@link List} of {@link EnergieClashRound}s with the given parameters from the database.
     *
     * @param difficulty      the difficulty of which the {@link EnergieClashRound}s should be.
     * @param language        the {@link Language} of which the {@link EnergieClashRound}s should be.
     * @param amountOfResults the amount of {@link EnergieClashRound}s.
     * @return a {@link List} of {@link EnergieClashRound}s.
     */
    public List<EnergieClashRound> getRandomEnergieClashRounds(Difficulty difficulty, Language language,
                                                               int amountOfResults) {
        List<EnergieClashRound> rounds = new EnergieClashRoundSupplier(logger)
            .getRandomEnergieClashRounds(getConnection(), difficulty, language, amountOfResults);
        closeConnection();
        return rounds;
    }

    /**
     * Fetches all the {@link EnergieClashRound}s.
     *
     * @return a {@link List} of all {@link EnergieClashRound}s.
     */
    public List<EnergieClashRound> getAllEnergieClashRounds() {
        List<EnergieClashRound> rounds =
            new EnergieClashRoundSupplier(logger).getAllEnergieClashRounds(getConnection());
        closeConnection();
        return rounds;
    }

    /**
     * Fetches a {@link EnergieClashRound} with the given identifier.
     *
     * @param id identifier of the {@link EnergieClashRound}.
     * @return a {@link EnergieClashRound}.
     */
    public EnergieClashRound getEnergieClashRoundById(int id) {
        EnergieClashRound round = new EnergieClashRoundSupplier(logger).getEnergieClashRoundById(getConnection(), id);
        closeConnection();
        return round;
    }

    /**
     * Fetches a {@link EnergieClashRound} with the given question.
     *
     * @param question the question of the {@link EnergieClashRound}.
     * @return a {@link EnergieClashRound}.
     */
    public EnergieClashRound getEnergieClashRoundByQuestion(String question) {
        EnergieClashRound round =
            new EnergieClashRoundSupplier(logger).getEnergieClashRoundByQuestion(getConnection(), question);
        closeConnection();
        return round;
    }

    /**
     * Adds a {@link EnergieClashRound} to the database.
     *
     * @param energieClashRound The {@link EnergieClashRound} to be added.
     * @return {@code boolean} whether the update was successful or not.
     */
    public boolean addQuestion(EnergieClashRound energieClashRound) {
        boolean updateSuccessful;
        String sql = "INSERT INTO questions(question, difficulty, language, explanation, source) VALUES(?,?,?,?,?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, energieClashRound.getQuestion());
            preparedStatement.setInt(2, energieClashRound.getDifficulty().getValue());
            preparedStatement.setString(3, energieClashRound.getLanguage().getAbbreviation());
            preparedStatement.setString(4, energieClashRound.getExplanation());
            preparedStatement.setString(5, energieClashRound.getSource());
            preparedStatement.executeUpdate();
            int questionId = getEnergieClashRoundByQuestion(energieClashRound.getQuestion()).getId();
            updateSuccessful = energieClashRound.getAnswers().stream()
                .allMatch(energieClashAnswer -> addAnswer(energieClashAnswer, questionId));
        } catch (SQLException e) {
            updateSuccessful = false;
            logger.error(e);
        }
        closeConnection();
        return updateSuccessful;
    }

    /**
     * Adds a {@link EnergieClashAnswer} to the database.
     *
     * @param energieClashAnswer The {@link EnergieClashAnswer} to be added.
     * @return {@code boolean} whether the update was successful or not.
     */
    private boolean addAnswer(EnergieClashAnswer energieClashAnswer, int questionId) {
        boolean updateSuccessful = true;
        String sql = "INSERT INTO answers(question_id, answer_grade_id, answer) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, questionId);
            preparedStatement.setInt(2, energieClashAnswer.getAnswerGrade().getId());
            preparedStatement.setString(3, energieClashAnswer.getAnswer());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            updateSuccessful = false;
            logger.error(e);
        }
        return updateSuccessful;
    }

    /**
     * Delets a {@link EnergieClashRound} from the database.
     *
     * @param energieClashRound The {@link EnergieClashRound} to be deleted.
     * @return {@code boolean} whether the update was successful or not.
     */
    public boolean deleteQuestion(EnergieClashRound energieClashRound) {
        boolean updateSuccessful;
        String sql = "DELETE FROM questions WHERE id = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, energieClashRound.getId());
            preparedStatement.executeUpdate();
            energieClashRound.getAnswers().forEach(this::deleteAnswer);
            updateSuccessful = energieClashRound.getAnswers().stream().allMatch(this::deleteAnswer);
        } catch (SQLException e) {
            updateSuccessful = false;
            logger.error(e);
        }
        closeConnection();
        return updateSuccessful;
    }

    /**
     * Delets a {@link EnergieClashAnswer} from the database.
     *
     * @param energieClashAnswer The {@link EnergieClashAnswer} to be deleted.
     * @return {@code boolean} whether the update was successful or not.
     */
    private boolean deleteAnswer(EnergieClashAnswer energieClashAnswer) {
        boolean updateSuccessful = true;
        String sql = "DELETE FROM answers WHERE id = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, energieClashAnswer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            updateSuccessful = false;
        }
        return updateSuccessful;
    }

    /**
     * Updates a {@link EnergieClashRound}.
     *
     * @param energieClashRound the {@link EnergieClashRound} to be updated.
     * @return {@code boolean} whether the update was successful or not.
     */
    public boolean updateQuestion(EnergieClashRound energieClashRound) {
        boolean updateSuccessful;
        String sql = "UPDATE questions SET question=?, difficulty=?, language=?, explanation=?, source=? WHERE id=?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, energieClashRound.getQuestion());
            preparedStatement.setInt(2, energieClashRound.getDifficulty().getValue());
            preparedStatement.setString(3, energieClashRound.getLanguage().getAbbreviation());
            preparedStatement.setString(4, energieClashRound.getExplanation());
            preparedStatement.setString(5, energieClashRound.getSource());
            preparedStatement.setInt(6, energieClashRound.getId());
            updateSuccessful = energieClashRound.getAnswers().stream().allMatch(this::updateAnswer);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            updateSuccessful = false;
        }
        closeConnection();
        return updateSuccessful;
    }

    /**
     * Updates a {@link EnergieClashAnswer}.
     *
     * @param energieClashAnswer the {@link EnergieClashAnswer} to be updated.
     * @return {@code boolean} whether the update was successful or not.
     */
    private boolean updateAnswer(EnergieClashAnswer energieClashAnswer) {
        boolean updateSuccessful = true;
        String sql = "UPDATE answers SET answer=?, answer_grade_id=? WHERE id=? AND question_id=?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, energieClashAnswer.getAnswer());
            preparedStatement.setInt(2, energieClashAnswer.getAnswerGrade().getId());
            preparedStatement.setInt(3, energieClashAnswer.getId());
            preparedStatement.setInt(4, energieClashAnswer.getQuestionId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            updateSuccessful = false;
        }
        return updateSuccessful;
    }

    /**
     * Creates the connection to the database.
     *
     * @return the database {@link Connection}.
     */
    Connection getConnection() {

        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e);
        }

        return connection;
    }

    void setUrl(String url) {
        this.url = url;
    }

    void closeConnection() {
        Optional.ofNullable(connection).ifPresent(c -> {
            try {
                c.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        });
    }
}
