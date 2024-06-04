package ch.graueenergie.energieclash.view.game;

import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRole;
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

import java.util.Optional;

public class EnergieClashEndView extends AbstractSynchEnergieClashView {
    private final String winnerText;
    @FXML
    private Pane defaultPane;
    @FXML
    private StackPane waitingPane;
    @FXML
    private Label endTitleLbl;
    @FXML
    private Label winnerLbl;
    @FXML
    private ImageView logoImage;

    /**
     * Creates a new instance.
     *
     * @param energieClashPlayer The Player.
     * @param language           The {@link Language} of the game.
     * @param winnerRole role of the winner
     */
    public EnergieClashEndView(EnergieClashPlayer energieClashPlayer, Language language, EnergieClashRole winnerRole) {
        super(energieClashPlayer, language);
        this.winnerText = Optional.ofNullable(winnerRole)
            .map(winner -> Translation.END_WINNER_TEXT
                    .getTextForLanguage(language) + winner.getRoleName().getTextForLanguage(language))
            .orElse(Translation.END_DRAW_TEXT.getTextForLanguage(language));
    }

    @Override
    protected void setupButtons() {
        buttons.forEach(button -> button.onDown(() -> Platform.runLater(this::endButtonAction)));
    }

    @Override
    public void initialize() {
        setupButtons();
        setupScreen();
    }

    @Override
    protected void setupScreen() {
        super.setupScreen();
        endTitleLbl.setText(Translation.END_TITLE.getTextForLanguage(language));
        winnerLbl.setText(winnerText);
        displayLogo(logoImage);
    }

    private void endButtonAction() {
        readyToMoveOn = true;
        support.firePropertyChange(PropertyName.END.getPropertyChangeEvent(this));
        defaultPane.setVisible(false);
        displayWaitingPane(waitingPane);
        resetButtons();
    }
}
