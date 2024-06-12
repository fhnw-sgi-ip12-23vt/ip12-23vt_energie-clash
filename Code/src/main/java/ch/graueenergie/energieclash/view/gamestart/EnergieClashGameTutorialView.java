package ch.graueenergie.energieclash.view.gamestart;

import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRole;
import ch.graueenergie.energieclash.util.ButtonColor;
import ch.graueenergie.energieclash.util.EnergieClashButton;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.AbstractSynchEnergieClashView;
import ch.graueenergie.energieclash.view.util.Translation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class EnergieClashGameTutorialView extends AbstractSynchEnergieClashView {
    @FXML
    private HBox buttonHBox;
    @FXML
    private Pane startPane;
    @FXML
    private Pane rolePane;
    @FXML
    private Pane tutorialPane;
    @FXML
    private Pane greyEnergyPane;
    @FXML
    private StackPane waitingPane;
    @FXML
    private VBox startPaneVBox;
    @FXML
    private VBox rolePaneVBox;
    @FXML
    private VBox tutorialPaneVBox;
    @FXML
    private HBox greyEnergyPaneHBox;
    @FXML
    private Label waitingForPlayerLbl;
    @FXML
    private ImageView logoImage;
    private final Difficulty difficulty;

    /**
     * Creates a new instance.
     *
     * @param energieClashPlayer The Player.
     * @param language           The {@link Language} of the game.
     * @param difficulty         the difficulty.
     */
    public EnergieClashGameTutorialView(EnergieClashPlayer energieClashPlayer,
                                        Language language, Difficulty difficulty) {
        super(energieClashPlayer, language);
        this.difficulty = difficulty;
    }

    @Override
    protected void setupButtons() {
        buttons.stream().filter(button -> button.getButtonColor().equals(ButtonColor.RED)).findFirst()
            .ifPresent(button -> button.onDown(() -> Platform.runLater(this::startButtonAction)));
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
        Label startLbl = new Label();
        ButtonColor buttonColor = ButtonColor.RED;
        startLbl.setText(
            String.format(Translation.TUTORIAL_DIFFICULTY.getTextForLanguage(language), difficulty.getText(language)));
        startPaneVBox.getChildren().add(startLbl);
        setButtons(createButtonHBox(
            buttons.stream()
                .filter(button -> button.getButtonColor().equals(buttonColor))
                .findFirst()
                .orElseThrow(
                    () -> new NoSuchElementException(String.format("No such button with color %s", buttonColor))),
            Translation.BUTTON_CONTINUE.getTextForLanguage(language)));
    }

    private void startButtonAction() {
        resetButtons();
        makeAllOtherPanesInvisible(rolePane);
        rolePane.setVisible(true);
        //Setup buttonactions
        buttons.stream().filter(button -> ButtonColor.GREEN.equals(button.getButtonColor())).findFirst()
            .ifPresent(button -> button.onDown(() -> Platform.runLater(this::greyEnergyButtonAction)));
        buttons.stream().filter(button -> ButtonColor.GREY.equals(button.getButtonColor())).findFirst()
            .ifPresent(button -> button.onDown(() -> Platform.runLater(this::tutorialButtonAction)));
        buttons.stream().filter(button -> ButtonColor.RED.equals(button.getButtonColor())).findFirst()
            .ifPresent(button -> button.onDown(() -> Platform.runLater(this::skipButtonAction)));
        //Display text
        Label roleTitleLbl = new Label();
        roleTitleLbl.setText(String.format(Translation.TUTORIAL_ROLE_TITLE.getTextForLanguage(language),
            energieClashPlayer.getRole().getRoleName().getTextForLanguage(language)));
        Label roleTextLbl = new Label();
        if (EnergieClashRole.SAVER.equals(energieClashPlayer.getRole())) {
            roleTextLbl.setText(Translation.TUTORIAL_ROLE_SAVER_TEXT.getTextForLanguage(language));
        } else if (EnergieClashRole.WASTER.equals(energieClashPlayer.getRole())) {
            roleTextLbl.setText(Translation.TUTORIAL_ROLE_WASTER_TEXT.getTextForLanguage(language));
        }
        rolePaneVBox.getChildren().clear();
        rolePaneVBox.getChildren().addAll(roleTitleLbl, roleTextLbl);
        //Display buttons
        HBox greyEnergyButton = createButtonHBox(
            buttons.stream().filter(button -> button.getButtonColor().equals(ButtonColor.GREEN)).findFirst()
                .orElseThrow(
                    EnergieClashButton::getButtonException),
            Translation.TUTORIAL_BUTTON_GREY_ENERGY.getTextForLanguage(language));
        HBox tutorialButton = createButtonHBox(
            buttons.stream().filter(button -> button.getButtonColor().equals(ButtonColor.GREY)).findFirst().orElseThrow(
                EnergieClashButton::getButtonException),
            Translation.TUTORIAL_BUTTON_TUTORIAL.getTextForLanguage(language));
        HBox skipButton = createButtonHBox(
            buttons.stream().filter(button -> button.getButtonColor().equals(ButtonColor.RED)).findFirst().orElseThrow(
                EnergieClashButton::getButtonException),
            Translation.TUTORIAL_BUTTON_SKIP.getTextForLanguage(language));
        setButtons(greyEnergyButton, tutorialButton, skipButton);
    }

    private void greyEnergyButtonAction() {
        resetButtons();
        makeAllOtherPanesInvisible(greyEnergyPane);
        greyEnergyPane.setVisible(true);
        //setup buttonaction
        buttons.stream().filter(button -> ButtonColor.GREEN.equals(button.getButtonColor())).findFirst()
            .ifPresent(button -> button.onDown(() -> Platform.runLater(this::tutorialButtonAction)));
        buttons.stream().filter(button -> ButtonColor.RED.equals(button.getButtonColor())).findFirst()
            .ifPresent(button -> button.onDown(() -> Platform.runLater(this::skipButtonAction)));

        greyEnergyPaneHBox.getChildren().clear();
        Label textLbl = new Label();
        textLbl.setWrapText(true);
        textLbl.setText(Translation.TUTORIAL_GREY_ENERGY.getTextForLanguage(language));
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(Translation.TUTORIAL_GREY_ENERGY_IMAGE.getTextForLanguage(language)));
        imageView.setPreserveRatio(true);
        greyEnergyPaneHBox.getChildren().addAll(textLbl, imageView);
        ButtonColor tutorialButtonColor = ButtonColor.GREEN;
        HBox tutorialButton = createButtonHBox(
            buttons.stream().filter(button -> button.getButtonColor().equals(tutorialButtonColor)).findFirst()
                .orElseThrow(
                    () -> new NoSuchElementException(
                        String.format("No such button with color %s", tutorialButtonColor))),
            Translation.TUTORIAL_BUTTON_TUTORIAL.getTextForLanguage(language));
        ButtonColor skipButtonColor = ButtonColor.RED;
        HBox skipButton = createButtonHBox(
            buttons.stream().filter(button -> button.getButtonColor().equals(skipButtonColor)).findFirst().orElseThrow(
                () -> new NoSuchElementException(String.format("No such button with color %s", skipButtonColor))),
            Translation.TUTORIAL_BUTTON_SKIP.getTextForLanguage(language));
        setButtons(tutorialButton, skipButton);
    }

    private void tutorialButtonAction() {
        resetButtons();
        makeAllOtherPanesInvisible(tutorialPane);
        tutorialPane.setVisible(true);
        //Setup buttonactions
        buttons.stream().filter(button -> ButtonColor.GREEN.equals(button.getButtonColor())).findFirst()
            .ifPresent(button -> button.onDown(() -> Platform.runLater(this::greyEnergyButtonAction)));
        buttons.stream().filter(button -> ButtonColor.RED.equals(button.getButtonColor())).findFirst()
            .ifPresent(button -> button.onDown(() -> Platform.runLater(this::skipButtonAction)));
        //Display text
        Label textLbl = new Label();
        textLbl.setText(Translation.TUTORIAL_TUTORIAL_TEXT.getTextForLanguage(language));
        textLbl.setWrapText(true);
        tutorialPaneVBox.getChildren().clear();
        tutorialPaneVBox.getChildren().add(textLbl);

        //Display buttons
        ButtonColor greyEnergyButtonColor = ButtonColor.GREEN;
        HBox greyEnergyButton = createButtonHBox(
            buttons.stream().filter(button -> button.getButtonColor().equals(greyEnergyButtonColor)).findFirst()
                .orElseThrow(
                    () -> new NoSuchElementException(
                        String.format("No such button with color %s", greyEnergyButtonColor))),
            Translation.TUTORIAL_BUTTON_GREY_ENERGY.getTextForLanguage(language));
        ButtonColor skipButtonColor = ButtonColor.RED;
        HBox skipButton = createButtonHBox(
            buttons.stream().filter(button -> button.getButtonColor().equals(ButtonColor.RED)).findFirst().orElseThrow(
                () -> new NoSuchElementException(String.format("No such button with color %s", skipButtonColor))),
            Translation.TUTORIAL_BUTTON_SKIP.getTextForLanguage(language));
        setButtons(greyEnergyButton, skipButton);
    }

    private void skipButtonAction() {
        readyToMoveOn = true;
        resetButtons();
        makeAllOtherPanesInvisible(waitingPane);
        displayWaitingPane(waitingPane);
        buttonHBox.setVisible(false);
        waitingForPlayerLbl.setText(Translation.WAITING_FOR_PLAYER_TEXT.getTextForLanguage(language));
        support.firePropertyChange(PropertyName.GAME_STARTER_TUTORIAL.getPropertyChangeEvent(this));
    }

    private void makeAllOtherPanesInvisible(Pane pane) {
        Stream.of(startPane, rolePane, tutorialPane, greyEnergyPane, waitingPane)
            .filter(p -> !p.equals(pane))
            .forEach(p -> p.setVisible(false));
    }

    private void setButtons(Node... nodes) {
        buttonHBox.getChildren().clear();
        buttonHBox.getChildren().addAll(nodes);
    }
}
