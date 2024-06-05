package ch.graueenergie.energieclash.model.gamelogic;

import ch.graueenergie.energieclash.controller.EnergieClash;
import ch.graueenergie.energieclash.util.Language;

import java.util.List;

/**
 * A round in {@link EnergieClash}.
 */
public class EnergieClashRound {
    private Integer id;
    private String question;
    private List<EnergieClashAnswer> answers;
    private Difficulty difficulty;
    private Language language;
    private String explanation;
    private String source;

    /**
     * Creates a new instance. Use when fetching from Database.
     *
     * @param id          the identifier.
     * @param question    the question.
     * @param answers     a {@link List} of {@link EnergieClashAnswer}s.
     * @param difficulty  the difficulty.
     * @param language    the language.
     * @param explanation the explanation.
     * @param source      the source.
     */
    public EnergieClashRound(int id, String question, List<EnergieClashAnswer> answers, Difficulty difficulty,
                             Language language, String explanation, String source) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.difficulty = difficulty;
        this.language = language;
        this.explanation = explanation;
        this.source = source;
    }

    /**
     * Creates a new instance. Use when creating a new {@link EnergieClashRound}.
     */
    public EnergieClashRound() {
    }

    /**
     * @return the question.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @return a {@link List} of {@link EnergieClashAnswer}s.
     */
    public List<EnergieClashAnswer> getAnswers() {
        return answers;
    }

    /**
     * @return the identifier.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the difficulty.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return the language.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @return the explanation.
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(List<EnergieClashAnswer> answers) {
        this.answers = answers;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
