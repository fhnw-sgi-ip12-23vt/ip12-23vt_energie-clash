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

public class EnergieClashExplanationView extends AbstractSynchEnergieClashView {
    @FXML
    private Pane defaultPane;
    @FXML
    private StackPane waitingPane;
    @FXML
    private Label explanationTitleLbl;
    @FXML
    private Label explanationLbl;
    @FXML
    private ImageView logoImage;
    private final String explanation;


    public EnergieClashExplanationView(EnergieClashPlayer energieClashPlayer, Language language, String explanation) {
        super(energieClashPlayer, language);
        this.explanation = explanation;
    }

    @Override
    protected void setupButtons() {
        buttons.forEach(button -> button.onDown(() -> Platform.runLater(this::explanationButtonAction)));
    }

    @Override
    public void initialize() {
        setupScreen();
        setupButtons();
    }

    @Override
    protected void setupScreen() {
        super.setupScreen();
        displayLogo(logoImage);
        explanationLbl.setText(explanation);
        explanationTitleLbl.setText(Translation.EXPLANATION_TITLE.getTextForLanguage(language));
    }
    private void explanationButtonAction() {
        readyToMoveOn = true;
        support.firePropertyChange(PropertyName.EXPLANATION.getPropertyChangeEvent(this));
        defaultPane.setVisible(false);
        displayWaitingPane(waitingPane);
        resetButtons();
    }
}
