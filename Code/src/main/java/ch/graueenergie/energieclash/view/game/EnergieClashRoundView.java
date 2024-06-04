package ch.graueenergie.energieclash.view.game;

import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashAnswer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRound;
import ch.graueenergie.energieclash.util.EnergieClashButton;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.AbstractSynchEnergieClashView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/**
 * {@link EnergieClashRoundView} is a ViewController used for the View in which the question is shown.
 */

public class EnergieClashRoundView extends AbstractSynchEnergieClashView {
    @FXML
    private Label timerLbl;
    @FXML
    private ImageView logoImage;
    @FXML
    private StackPane waitingPane;
    @FXML
    private VBox answerVBox;
    private EnergieClashAnswer selectedAnswer;
    private static final int TIME = 30;
    private final EnergieClashRound energieClashRound;
    private Timeline countDownTimeLine;
    private final List<EnergieClashAnswer> answerList;

    /**
     * Creates a new instance.
     *
     * @param energieClashPlayer The Player.
     * @param energieClashRound  The {@link EnergieClashRound} played in this view.
     * @param language           the language.
     */
    public EnergieClashRoundView(EnergieClashPlayer energieClashPlayer, EnergieClashRound energieClashRound,
                                 Language language) {
        super(energieClashPlayer, language);
        this.energieClashRound = energieClashRound;
        this.answerList = energieClashRound.getAnswers();
        Collections.shuffle(answerList);
    }

    /**
     * Initializes the view.
     */
    @FXML
    @Override
    public void initialize() {
        setupButtons();
        setupScreen();
        setupTimer();
    }

    @Override
    protected void setupButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            EnergieClashButton energieClashButton = buttons.get(i);
            EnergieClashAnswer energieClashAnswer = answerList.get(i);
            energieClashButton.onDown(() -> Platform.runLater(() -> selectAnswerButtonAction(energieClashAnswer)));
        }
    }

    @Override
    protected void setupScreen() {
        super.setupScreen();
        displayLogo(logoImage);
        Label questionLbl = new Label(energieClashRound.getQuestion());
        questionLbl.setWrapText(true);
        answerVBox.getChildren().add(questionLbl);
        for (int i = 0; i < answerList.size(); i++) {
            EnergieClashAnswer answer = answerList.get(i);
            EnergieClashButton button = buttons.get(i);
            HBox hBox = createButtonHBox(button, answer.getAnswer());
            answerVBox.getChildren().add(hBox);
        }
    }

    private void setupTimer() {
        ObjectProperty<java.time.Duration> remainingDuration
            = new SimpleObjectProperty<>(java.time.Duration.ofSeconds(TIME));

        // Binding with media time format (hh:mm:ss):
        timerLbl.textProperty().bind(Bindings.createStringBinding(() ->
                String.format("%02d",
                    remainingDuration.get().toSecondsPart()),
            remainingDuration));

        // Create time line to lower remaining duration every second:
        countDownTimeLine = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) ->
            remainingDuration.setValue(remainingDuration.get().minus(1, ChronoUnit.SECONDS))));

        // Set number of cycles (remaining duration in seconds):
        countDownTimeLine.setCycleCount((int) remainingDuration.get().getSeconds());

        // Show alert when time is up:
        countDownTimeLine.setOnFinished(event -> Platform.runLater(() -> support.firePropertyChange(
            PropertyName.ENERGIE_CLASH_ROUND_TIMER_ELAPSED.getPropertyChangeEvent(this))));

        // Start the time line:
        countDownTimeLine.play();
    }

    private void selectAnswerButtonAction(EnergieClashAnswer energieClashAnswer) {
        countDownTimeLine.stop();
        selectedAnswer = energieClashAnswer;
        readyToMoveOn = true;
        support.firePropertyChange(PropertyName.ENERGIE_CLASH_ROUND_ANSWER.getPropertyChangeEvent(this));
        displayWaitingPane(waitingPane);
        resetButtons();
        //highlight selected Answer
        answerVBox.getChildren().stream()
            .filter(node -> node instanceof HBox)
            .map(node -> (HBox) node)
            .flatMap(hBox -> hBox.getChildren().stream())
            .filter(r -> r instanceof Label)
            .map(r -> (Label) r)
            .filter(label -> label.getText().equals(energieClashAnswer.getAnswer()))
            .findFirst()
            .ifPresent(label -> ((HBox) label.getParent()).setBackground(new Background(new BackgroundFill(
                Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY))));
    }

    public EnergieClashAnswer getSelectedAnswer() {
        return selectedAnswer;
    }
}
