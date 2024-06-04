package ch.graueenergie.energieclash.model.gamelogic;

import ch.graueenergie.energieclash.controller.EnergieClash;

/**
 * The {@link EnergieClashPlayer} of {@link EnergieClash}.
 */
public class EnergieClashPlayer {
    private final EnergieClashRole role;
    private int points;

    /**
     * Creates a new instance.
     *
     * @param role the {@link EnergieClashRole} of this player.
     */
    public EnergieClashPlayer(EnergieClashRole role) {
        super();
        this.role = role;
        points = 0;
    }

    /**
     * @return the {@link EnergieClashRole} of this player.
     */
    public EnergieClashRole getRole() {
        return role;
    }

    /**
     * @return the amount of points this player has.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Adds points to this player.
     *
     * @param pointsToAdd the amount of points to be added.
     */
    public void addPoints(int pointsToAdd) {
        points += pointsToAdd;
    }
}
