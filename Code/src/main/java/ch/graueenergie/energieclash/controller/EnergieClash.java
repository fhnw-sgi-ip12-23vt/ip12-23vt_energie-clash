package ch.graueenergie.energieclash.controller;

import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRound;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.view.util.i2c.I2CLED;
import ch.graueenergie.energieclash.view.util.PuiSupplier;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;


public abstract class EnergieClash extends Controller {
    private final PropertyChangeSupport support;
    private final Difficulty difficulty;
    private List<EnergieClashRound> energieClashRounds;
    private final List<I2CLED> i2cLeds;
    /**
     * Creates a new instance.
     *
     * @param language    the language of this instance.
     * @param saverStage  the Stage which is shown on the Savers screen.
     * @param wasterStage the Stage which is shown on the Wasters screen.
     * @param difficulty the selected difficulty
     * @param saver the role of the player
     * @param waster the role of the player
     */
    public EnergieClash(Language language, Difficulty difficulty, Stage saverStage,
                        Stage wasterStage, EnergieClashPlayer saver, EnergieClashPlayer waster) {
        super(saverStage, wasterStage, saver, waster, language);
        this.difficulty = difficulty;
        support = new PropertyChangeSupport(this);
        i2cLeds = PuiSupplier.getI2CLEDs();
    }

    protected void setEnergieClashRounds(List<EnergieClashRound> energieClashRounds) {
        this.energieClashRounds = energieClashRounds;
    }

    protected List<EnergieClashRound> getEnergieClashRounds() {
        return energieClashRounds;
    }

    protected PropertyChangeSupport getSupport() {
        return support;
    }

    protected Difficulty getDifficulty() {
        return difficulty;
    }

    protected List<I2CLED> getI2cLeds() {
        return i2cLeds;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

}
