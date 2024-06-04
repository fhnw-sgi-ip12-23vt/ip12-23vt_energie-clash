package ch.graueenergie.energieclash.model.gamelogic;

import ch.graueenergie.energieclash.controller.EnergieClash;
import ch.graueenergie.energieclash.view.util.Translation;

import java.util.HashMap;
import java.util.Map;

/**
 * The roles in {@link EnergieClash}.
 */
public enum EnergieClashRole {
    /*
     * The initialization takes the n element of the int-array and maps it as the amount of points for the n AnswerGrade
     *
     * ***EXAMPLE***
     * SAVER(createAnswerGradeIntegerMap(new int[]{5, 4, 3, 2, 1}))
     * Here the first element of the array "5" will be the points for the answerGrade "BEST"
     * "4" will be the points for answerGrade "GOOD" so on and so forth
     *
     * This is done this way, in case if there are new roles or answergrades added in the future
     * */
    SAVER(Translation.SAVER_NAME, createAnswerGradeIntegerMap(new int[] {5, 4, 3, 2, 1})),
    WASTER(Translation.WASTER_NAME, createAnswerGradeIntegerMap(new int[] {1, 2, 3, 4, 5}));
    private final Translation roleName;
    private final Map<AnswerGrade, Integer> answerGradeIntegerMap;


    /**
     * Creates a new instance
     *
     * @param roleName              the name of the role.
     * @param answerGradeIntegerMap a {@link Map} of {@link AnswerGrade}s and {@link Integer}s used to calculate points.
     */
    EnergieClashRole(Translation roleName, Map<AnswerGrade, Integer> answerGradeIntegerMap) {
        this.roleName = roleName;
        this.answerGradeIntegerMap = answerGradeIntegerMap;
    }

    private static Map<AnswerGrade, Integer> createAnswerGradeIntegerMap(int[] ints) throws IllegalArgumentException {
        AnswerGrade[] answerGrades = AnswerGrade.values();
        if (ints.length != answerGrades.length) {
            throw new IllegalArgumentException();
        }
        Map<AnswerGrade, Integer> answerGradeIntegerMap = new HashMap<>();
        for (int i = 0; i < answerGrades.length; i++) {
            answerGradeIntegerMap.put(answerGrades[i], ints[i]);
        }
        return answerGradeIntegerMap;
    }

    /**
     * Calculates the points and returns them.
     *
     * @param answerGrade the {@link AnswerGrade} of the answer of which the points are to be calculated.
     * @return the amount of points.
     */
    public int getPointsForAnswer(AnswerGrade answerGrade) {
        return answerGradeIntegerMap.get(answerGrade);
    }

    /**
     * @return the name of the role.
     */
    public Translation getRoleName() {
        return roleName;
    }
}

