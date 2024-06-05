package ch.graueenergie.energieclash.controller;

import ch.graueenergie.energieclash.model.EnergieClashModel;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.view.AbstractSynchView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.util.Optional;

public abstract class Controller implements PropertyChangeListener {
    protected static final Logger LOGGER = LogManager.getLogger(Controller.class);
    protected EnergieClashModel model;
    protected Stage saverStage;
    protected Stage wasterStage;
    protected AbstractSynchView saverView;
    protected AbstractSynchView wasterView;
    protected final EnergieClashPlayer saver;
    protected final EnergieClashPlayer waster;
    protected Language language;

    public Controller(Stage saverStage, Stage wasterStage,
                      EnergieClashPlayer saver, EnergieClashPlayer waster, Language language) {
        this.saverStage = saverStage;
        this.wasterStage = wasterStage;
        this.saver = saver;
        this.waster = waster;
        this.language = language;
        this.model = new EnergieClashModel(LogManager.getLogger(EnergieClashModel.class));

    }

    /**
     * starts the game
     */
    public abstract void start();

    protected boolean areBothPlayersReady() {
        return saverView.isReadyToMoveOn() && wasterView.isReadyToMoveOn();
    }

    protected <T extends Controller> void addPropertyChangeListenerToViews(T controller) {
        Optional.ofNullable(saverView).ifPresent(view -> view.addPropertyChangeListener(this));
        Optional.ofNullable(wasterView).ifPresent(view -> view.addPropertyChangeListener(this));
    }
    protected <T extends Controller> void removePropertyChangeListenerFromViews(T controller) {
        Optional.ofNullable(saverView).ifPresent(view -> view.removePropertyChangeListener(this));
        Optional.ofNullable(wasterView).ifPresent(view -> view.removePropertyChangeListener(this));
    }
}
