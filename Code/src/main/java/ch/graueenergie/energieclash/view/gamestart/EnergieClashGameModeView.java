package ch.graueenergie.energieclash.view.gamestart;

import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.util.EnergieClashButton;
import ch.graueenergie.energieclash.util.GameMode;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.AbstractSynchEnergieClashView;
import ch.graueenergie.energieclash.view.util.Translation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

public class EnergieClashGameModeView extends AbstractSynchEnergieClashView {
    @FXML
    private VBox gameModeVBox;
    @FXML
    private ImageView logoImage;
    private GameMode selectedGameMode;
    private final List<GameMode> availableGameModes;

    public EnergieClashGameModeView(EnergieClashPlayer energieClashPlayer, Language language,
                                    List<GameMode> availableGameModes) {
        super(energieClashPlayer, language);
        this.availableGameModes = availableGameModes;
    }

    @Override
    protected void setupButtons() {
        for (int i = 0; i < availableGameModes.size() && i < buttons.size(); i++) {
            GameMode gameMode = availableGameModes.get(i);
            EnergieClashButton button = buttons.get(i);
            //setup buttonaction
            button.onDown(() -> Platform.runLater(() -> selectGameModeButtonAction(gameMode)));
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
        Label label = new Label(Translation.CHOOSE_GAMEMODE.getTextForLanguage(language));
        label.setWrapText(true);
        gameModeVBox.getChildren().add(label);
        for (int i = 0; i < availableGameModes.size() && i < buttons.size(); i++) {
            String gameMode = availableGameModes.get(i).getText(language);
            EnergieClashButton button = buttons.get(i);
            gameModeVBox.getChildren().add(createButtonHBox(button, gameMode));
        }
    }

    private void selectGameModeButtonAction(GameMode gameMode) {
        selectedGameMode = gameMode;
        support.firePropertyChange(PropertyName.GAME_STARTER_GAME_MODE.getPropertyChangeEvent(this));
    }

    public GameMode getSelectedGameMode() {
        return selectedGameMode;
    }
}
