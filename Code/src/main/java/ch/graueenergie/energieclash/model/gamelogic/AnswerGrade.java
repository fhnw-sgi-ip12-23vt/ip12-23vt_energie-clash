package ch.graueenergie.energieclash.model.gamelogic;

/**
 * The answer grade of a {@link EnergieClashAnswer}.
 */
public enum AnswerGrade {
    /*
    These AnswerGrades are formulated in relation to the environment
    For example: BEST-> Is the Best for the environment
    * */
    BEST(1),
    GOOD(2),
    MEDIUM(3),
    BAD(4),
    WORST(5);
    private final int id;

    AnswerGrade(int id) {
        this.id = id;
    }

    /**
     *
     * @return the identifier.
     */
    public int getId() {
        return id;
    }
}
