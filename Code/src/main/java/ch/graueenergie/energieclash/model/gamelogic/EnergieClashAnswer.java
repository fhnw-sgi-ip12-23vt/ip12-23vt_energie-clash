package ch.graueenergie.energieclash.model.gamelogic;

/**
 * One of {@code n} answers in a {@link EnergieClashRound}.
 */
public class EnergieClashAnswer {
    private int id;
    private int questionId;
    private AnswerGrade answerGrade;
    private String answer;

    /**
     * Creates a new instance. Use when fetching from Database.
     * @param id the identifier
     * @param questionId the identifier of the {@link EnergieClashRound}.
     * @param answerGrade the {@link AnswerGrade} of this instance.
     * @param answer the answer of this instance.
     */
    public EnergieClashAnswer(int id, int questionId, AnswerGrade answerGrade, String answer) {
        this.id = id;
        this.questionId = questionId;
        this.answerGrade = answerGrade;
        this.answer = answer;
    }

    /**
     * Creates a new instance. Use when creating a new {@link EnergieClashAnswer}.
     */
    public EnergieClashAnswer() {

    }

    /**
     *
     * @return the identifier.
     */
    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return questionId;
    }

    /**
     *
     * @return the identifier of the {@link EnergieClashRound}.
     */
    public AnswerGrade getAnswerGrade() {
        return answerGrade;
    }

    /**
     *
     * @return the answer of this instance.
     */
    public String getAnswer() {
        return answer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setAnswerGrade(AnswerGrade answerGrade) {
        this.answerGrade = answerGrade;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
