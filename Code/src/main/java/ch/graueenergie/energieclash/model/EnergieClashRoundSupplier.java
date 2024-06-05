package ch.graueenergie.energieclash.model;

import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.model.gamelogic.AnswerGrade;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashAnswer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRound;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * The supplier of {@link EnergieClashRound}s.
 */
class EnergieClashRoundSupplier {
    private final Logger logger;

    EnergieClashRoundSupplier(Logger logger) {
        this.logger = logger;
    }

    /**
     * Fetches a {@link List} of {@link EnergieClashRound}s with the given parameters from the database.
     *
     * @param connection      the {@link Connection} to the database.
     * @param difficulty      the difficulty of which the {@link EnergieClashRound}s should be.
     * @param language        the {@link Language} of which the {@link EnergieClashRound}s should be.
     * @param amountOfResults the amount of {@link EnergieClashRound}s.
     * @return a {@link List} of {@link EnergieClashRound}s.
     */
    List<EnergieClashRound> getRandomEnergieClashRounds(Connection connection, Difficulty difficulty, Language language,
                                                        Integer amountOfResults) {
        ResultSet questionsResultSet = getRandomQuestionsResultSet(connection, difficulty, language, amountOfResults);
        return mapToEnergieClashRounds(questionsResultSet, connection);
    }

    /**
     * Fetches all the {@link EnergieClashRound}s.
     *
     * @param connection the {@link Connection} to the database.
     * @return a {@link List} of all {@link EnergieClashRound}s.
     */
    List<EnergieClashRound> getAllEnergieClashRounds(Connection connection) {
        ResultSet questionsResultSet = getAllQuestionsResultSet(connection);
        return mapToEnergieClashRounds(questionsResultSet, connection);
    }

    /**
     * Fetches a {@link EnergieClashRound} with the given identifier.
     *
     * @param connection the {@link Connection} to the database.
     * @param id         identifier of the {@link EnergieClashRound}.
     * @return a {@link EnergieClashRound}.
     */
    EnergieClashRound getEnergieClashRoundById(Connection connection, int id) {
        ResultSet questionsResultSet = getQuestionByIdResultSet(connection, id);
        return mapToEnergieClashRounds(questionsResultSet, connection).stream().findFirst().orElse(null);
    }

    /**
     * Fetches a {@link EnergieClashRound} with the given question.
     *
     * @param connection the {@link Connection} to the database.
     * @param question   the question of the {@link EnergieClashRound}.
     * @return a {@link EnergieClashRound}.
     */
    EnergieClashRound getEnergieClashRoundByQuestion(Connection connection, String question) {
        ResultSet questionsResultSet = getQuestionByQuestionResultSet(connection, question);
        return mapToEnergieClashRounds(questionsResultSet, connection).stream().findFirst().orElse(null);
    }

    /**
     * Fetches a {@link ResultSet} with the given parameters from the database.
     *
     * @param connection      the {@link Connection} to the database.
     * @param difficulty      the difficulty of which the {@link EnergieClashRound}s should be.
     * @param language        the {@link Language} of which the {@link EnergieClashRound}s should be.
     * @param amountOfResults the amount of {@link EnergieClashRound}s.
     * @return a {@link ResultSet} containing the information of the {@link EnergieClashRound}s.
     */
    private ResultSet getRandomQuestionsResultSet(Connection connection, Difficulty difficulty, Language language,
                                                  Integer amountOfResults) {
        String sql = "SELECT * FROM questions WHERE difficulty=? AND language LIKE ? ORDER BY random() LIMIT ?";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, difficulty.getValue());
            preparedStatement.setString(2, language.getAbbreviation());
            preparedStatement.setInt(3, amountOfResults);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
        }
        return resultSet;
    }

    /**
     * Fetches a {@link ResultSet} of all questions.
     *
     * @param connection the {@link Connection} to the database.
     * @return a {@link ResultSet}.
     */
    private ResultSet getAllQuestionsResultSet(Connection connection) {
        String sql = "SELECT * FROM questions ORDER BY id";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
        }
        return resultSet;
    }

    /**
     * @param connection the {@link Connection} to the database.
     * @param id         identifier of the {@link EnergieClashRound}.
     * @return a {@link ResultSet}.
     */
    private ResultSet getQuestionByIdResultSet(Connection connection, int id) {
        String sql = "SELECT * FROM questions WHERE id=? ORDER BY id";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
        }
        return resultSet;
    }

    /**
     * Fetches a {@link ResultSet} with the correct {@code question} from the database.
     *
     * @param connection the {@link Connection} to the database.
     * @param question   the question of the {@link EnergieClashRound}.
     * @return a {@link ResultSet}.
     */
    private ResultSet getQuestionByQuestionResultSet(Connection connection, String question) {
        String sql = "SELECT * FROM questions WHERE question=? ORDER BY id";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, question);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
        }
        return resultSet;
    }

    /**
     * Fetches a {@link ResultSet} with the correct {@code questionId} from the database.
     *
     * @param connection the {@link Connection} to the database.
     * @param questionId the Id of the {@link EnergieClashRound}.
     * @return a {@link ResultSet} containing the information of the {@link EnergieClashAnswer}s.
     */
    private ResultSet getAnswersResultSet(Connection connection, int questionId) {
        String sql = "SELECT * FROM answers WHERE question_id=?";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, questionId);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
        }
        return resultSet;
    }

    /**
     * Maps the {@link ResultSet} to a {@link List} of {@link EnergieClashRound}s.
     *
     * @param resultSet  the {@link ResultSet} to be mapped.
     * @param connection the {@link Connection} to the database.
     * @return a {@link List} of mapped {@link EnergieClashRound}s.
     */
    private List<EnergieClashRound> mapToEnergieClashRounds(ResultSet resultSet, Connection connection) {
        List<EnergieClashRound> energieClashRounds = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int questionId = resultSet.getInt("id");
                String question = resultSet.getString("question");
                List<EnergieClashAnswer> energieClashAnswers =
                    mapToEnergieClashAnswers(getAnswersResultSet(connection, questionId));
                Difficulty energieClashDifficulty = Difficulty.getDifficulty(resultSet.getInt("difficulty"));
                Language energieClashLanguage = Language.getLanguage(resultSet.getString("language"));
                String explanation = resultSet.getString("explanation");
                String source = resultSet.getString("source");
                energieClashRounds.add(
                    new EnergieClashRound(questionId, question, energieClashAnswers, energieClashDifficulty,
                        energieClashLanguage, explanation, source));
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return energieClashRounds;
    }

    /**
     * Maps the {@link ResultSet} to a {@link List} of {@link EnergieClashAnswer}s.
     *
     * @param resultSet the {@link ResultSet} to be mapped.
     * @return a {@link List} of mapped {@link EnergieClashAnswer}s.
     */
    private List<EnergieClashAnswer> mapToEnergieClashAnswers(ResultSet resultSet) {
        List<EnergieClashAnswer> energieClashAnswers = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int answerId = resultSet.getInt("id");
                int questionId = resultSet.getInt("question_id");
                int answerGradeId = resultSet.getInt("answer_grade_id");
                AnswerGrade answerGrade = Stream.of(AnswerGrade.values())
                    .filter(answerGradeElement -> answerGradeElement.getId() == answerGradeId)
                    .findFirst()
                    .orElse(null);
                String answer = resultSet.getString("answer");
                energieClashAnswers.add(new EnergieClashAnswer(answerId, questionId, answerGrade, answer));
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return energieClashAnswers;
    }
}
