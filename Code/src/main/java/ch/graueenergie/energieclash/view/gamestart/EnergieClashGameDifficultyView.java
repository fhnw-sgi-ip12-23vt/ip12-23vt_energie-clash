package ch.graueenergie.energieclash.view.gamestart;

import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.util.EnergieClashButton;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.AbstractSynchEnergieClashView;
import ch.graueenergie.energieclash.view.util.Translation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class EnergieClashGameDifficultyView extends AbstractSynchEnergieClashView {
    @FXML
    private VBox difficultyVBox;
    @FXML
    private Label difficultyTextLbl;
    @FXML
    private ImageView logoImage;
    private Difficulty selectedDifficulty;

    /**
     * Creates a new instance.
     *
     * @param energieClashPlayer The Player.
     * @param language           The {@link Language} of the game.
     */
    public EnergieClashGameDifficultyView(
        EnergieClashPlayer energieClashPlayer,
        Language language) {
        super(energieClashPlayer, language);
    }

    @Override
    protected void setupButtons() {
        for (int i = 0; i < Difficulty.values().length && i < buttons.size(); i++) {
            Difficulty difficulty = Difficulty.values()[i];
            buttons.get(i).onDown(() -> Platform.runLater(() -> selectDifficultyButtonAction(difficulty)));
        }
    }

    @Override
    public void initialize() {
        setupButtons();
        setupScreen();
    }

    @Override
    protected void setupScreen() {
        super.setupScreen();
        displayLogo(logoImage);
        for (int i = 0; i < Difficulty.values().length && i < buttons.size(); i++) {
            String gameMode = Difficulty.values()[i].getText(language);
            EnergieClashButton button = buttons.get(i);
            difficultyVBox.getChildren().add(createButtonHBox(button, gameMode));
        }
        difficultyTextLbl.setText(Translation.DIFFICULTY_TEXT.getTextForLanguage(language));
    }

    private void selectDifficultyButtonAction(Difficulty difficulty) {
        selectedDifficulty = difficulty;
        support.firePropertyChange(PropertyName.GAME_STARTER_DIFFICULTY.getPropertyChangeEvent(this));
    }

    public Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }
}
