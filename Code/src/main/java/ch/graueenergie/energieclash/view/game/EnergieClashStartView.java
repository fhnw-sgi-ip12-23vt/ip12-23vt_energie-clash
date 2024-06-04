package ch.graueenergie.energieclash.view.game;

import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.AbstractSynchEnergieClashView;
import ch.graueenergie.energieclash.view.util.Translation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class EnergieClashStartView extends AbstractSynchEnergieClashView {
    @FXML
    private Pane defaultPane;
    @FXML
    private StackPane waitingPane;
    @FXML
    private Label startTextLbl;
    @FXML
    private ImageView logoImage;
    /**
     * Creates a new instance.
     * @param energieClashPlayer The Player.
     * @param language The {@link Language} of the game.
     */
    public EnergieClashStartView(EnergieClashPlayer energieClashPlayer, Language language) {
        super(energieClashPlayer, language);
    }

    /**
     * Initializes the view.
     */
    @FXML
    @Override
    public void initialize() {
        setupButtons();
        setupScreen();
    }

    @Override
    protected void setupButtons() {
        buttons.forEach(button -> button.onDown(() -> Platform.runLater(this::startButtonAction)));
    }

    @Override
    protected void setupScreen() {
        super.setupScreen();
        displayLogo(logoImage);
        startTextLbl.setText(Translation.START_TEXT.getTextForLanguage(language));
    }

    private void startButtonAction() {
        readyToMoveOn = true;
        support.firePropertyChange(PropertyName.START.getPropertyChangeEvent(this));
        defaultPane.setVisible(false);
        displayWaitingPane(waitingPane);
        resetButtons();
    }
}
