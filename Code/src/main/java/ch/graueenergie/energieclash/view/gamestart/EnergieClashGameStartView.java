package ch.graueenergie.energieclash.view.gamestart;

import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.AbstractSynchEnergieClashView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class EnergieClashGameStartView extends AbstractSynchEnergieClashView {
    @FXML
    private Pane defaultPane;
    @FXML
    private StackPane waitingPane;
    @FXML
    private Button logoButton;

    /**
     * Creates a new instance.
     *
     * @param energieClashPlayer The Player.
     * @param language           The {@link Language} of the game.
     */
    public EnergieClashGameStartView(EnergieClashPlayer energieClashPlayer,
                                     Language language) {
        super(energieClashPlayer, language);
    }

    @Override
    protected void setupButtons() {
        buttons.forEach(button -> button.onDown(() -> Platform.runLater(this::startButtonAction)));
    }

    @Override
    public void initialize() {
        setupButtons();
        setupScreen();
    }

    @Override
    protected void setupScreen() {
        super.setupScreen();
        logoButton.setOnAction(actionEvent -> adminButtonAction());
    }

    private void startButtonAction() {
        readyToMoveOn = true;
        support.firePropertyChange(PropertyName.GAME_STARTER_START.getPropertyChangeEvent(this));
        defaultPane.setVisible(false);
        displayWaitingPane(waitingPane);
        resetButtons();
    }
    private void adminButtonAction() {
        support.firePropertyChange(PropertyName.GAME_STARTER_ADMIN.getPropertyChangeEvent(this));
    }
}
