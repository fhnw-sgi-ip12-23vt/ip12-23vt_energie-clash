package ch.graueenergie.energieclash.view.admin;

import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.model.gamelogic.AnswerGrade;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashAnswer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRound;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.AbstractSynchView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * View used for Admin Detail Screen
 */
public class AdminDetailView extends AbstractSynchView {
    @FXML
    private AnchorPane adminDetailAnchorPane;
    @FXML
    private TextArea questionTextArea;
    @FXML
    private TextArea explanationTextArea;
    @FXML
    private TextArea sourceTextArea;
    @FXML
    private ChoiceBox<String> languageChoiceBox;
    @FXML
    private ChoiceBox<String> difficultyChoiceBox;
    @FXML
    private TextArea firstAnswerTextArea;
    @FXML
    private TextArea secondAnswerTextArea;
    @FXML
    private TextArea thirdAnswerTextArea;
    @FXML
    private TextArea fourthAnswerTextArea;
    @FXML
    private TextArea fifthAnswerTextArea;
    @FXML
    private ChoiceBox<String> firstAnswerChoiceBox;
    @FXML
    private ChoiceBox<String> secondAnswerChoiceBox;
    @FXML
    private ChoiceBox<String> thirdAnswerChoiceBox;
    @FXML
    private ChoiceBox<String> fourthAnswerChoiceBox;
    @FXML
    private ChoiceBox<String> fifthAnswerChoiceBox;
    @FXML
    CheckBox editableCheckBox;
    @FXML
    private Button saveBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button exitBtn;
    private EnergieClashRound energieClashRound;
    private Popup updatePopup;
    private final Map<TextArea, Integer> textAreaMap = new HashMap<>();
    private static final int MAX_QUESTION_CHARS = 130;
    private static final int MAX_ANSWER_CHARS = 80;
    private static final int MAX_EXPLANATION_CHARS = 500;

    public AdminDetailView(Language language, EnergieClashRound energieClashRound) {
        super(language);
        this.energieClashRound = energieClashRound;
    }

    public AdminDetailView(Language language) {
        super(language);
    }

    @FXML
    public void initialize() {
        textAreaMap.put(questionTextArea, MAX_QUESTION_CHARS);
        textAreaMap.put(explanationTextArea, MAX_EXPLANATION_CHARS);
        textAreaMap.put(firstAnswerTextArea, MAX_ANSWER_CHARS);
        textAreaMap.put(secondAnswerTextArea, MAX_ANSWER_CHARS);
        textAreaMap.put(thirdAnswerTextArea, MAX_ANSWER_CHARS);
        textAreaMap.put(fourthAnswerTextArea, MAX_ANSWER_CHARS);
        textAreaMap.put(fifthAnswerTextArea, MAX_ANSWER_CHARS);
        setupScreen();
        saveBtn.setOnAction(this::saveEnergieClashRound);
        deleteBtn.setOnAction(this::delete);
        exitBtn.setOnAction(this::exit);
    }

    @Override
    protected void setupScreen() {
        super.setupScreen();
        setupChoiceBoxes();
        populateView();
        setUpCheckBox();
    }

    private void setUpCheckBox() {
        editableCheckBox.setOnAction(
            actionEvent -> List.of(firstAnswerChoiceBox, secondAnswerChoiceBox, thirdAnswerChoiceBox,
                    fourthAnswerChoiceBox,
                    fifthAnswerChoiceBox)
                .forEach(stringChoiceBox -> stringChoiceBox.setDisable(!editableCheckBox.isSelected())));
    }

    @FXML
    private void saveEnergieClashRound(ActionEvent actionEvent) {
        if (textAreaMap.keySet().stream().allMatch(this::isInputValid)) {
            Optional.ofNullable(energieClashRound)
                .ifPresentOrElse(e -> updateEnergieClashRoundAndRefresh(), this::createNewEnergieClashRound);
            support.firePropertyChange(PropertyName.ADMIN_DETAIL_SAVE.getPropertyChangeEvent(this));
        }
    }

    private void populateView() {
        Optional.ofNullable(energieClashRound)
            .ifPresentOrElse(e -> setEnergieClashRoundValuesToView(), this::setDefaultValuesToView);
        setupTextAreaValidation();
    }

    @FXML
    private void exit(ActionEvent actionEvent) {
        Optional.ofNullable(updatePopup).ifPresent(Popup::hide);
        support.firePropertyChange(PropertyName.ADMIN_DETAIL_EXIT.getPropertyChangeEvent(this));
    }

    @FXML
    private void delete(ActionEvent actionEvent) {
        Popup deletePopup = new Popup(); //Create Popup with components
        VBox vBox = new VBox();
        Label deleteLabel = new Label("Are you sure you want to delete?");
        vBox.getChildren().add(deleteLabel);
        HBox hBox = new HBox();
        //Create Yes Button and set Eventhandler
        Button yesPopupButton = new Button("Yes");
        yesPopupButton.setOnAction(actionEvent1 -> {
            surelyDelete(actionEvent1);
            deletePopup.hide();
        });
        Button cancelPopupButton = new Button("Cancel"); //Create Cancel Button and set Eventhandler
        cancelPopupButton.setOnAction(actionEvent12 -> deletePopup.hide());
        hBox.getChildren().addAll(yesPopupButton, cancelPopupButton, hBox);
        deletePopup.getContent().add(vBox);
        deletePopup.show(adminDetailAnchorPane.getScene().getWindow());
    }

    @FXML
    private void surelyDelete(ActionEvent actionEvent) {
        support.firePropertyChange(PropertyName.ADMIN_DETAIL_DELETE.getPropertyChangeEvent(this));
    }

    private void createNewEnergieClashRound() {
        energieClashRound = new EnergieClashRound();
        energieClashRound.setAnswers(Stream.generate(EnergieClashAnswer::new).limit(5).toList());
        updateEnergieClashRoundAndRefresh();
    }

    private void updateEnergieClashRoundAndRefresh() {
        energieClashRound.setQuestion(questionTextArea.getText());
        energieClashRound.setDifficulty(Difficulty.getDifficulty(difficultyChoiceBox.getValue()));
        energieClashRound.setLanguage(Language.getLanguage(languageChoiceBox.getValue()));
        energieClashRound.setExplanation(explanationTextArea.getText());
        energieClashRound.setSource(sourceTextArea.getText());
        updateEnergieClashAnswers(energieClashRound.getAnswers());
    }

    private void updateEnergieClashAnswers(List<EnergieClashAnswer> energieClashAnswers) {
        updateEnergieClashAnswer(energieClashAnswers.get(0), firstAnswerTextArea, firstAnswerChoiceBox);
        updateEnergieClashAnswer(energieClashAnswers.get(1), secondAnswerTextArea, secondAnswerChoiceBox);
        updateEnergieClashAnswer(energieClashAnswers.get(2), thirdAnswerTextArea, thirdAnswerChoiceBox);
        updateEnergieClashAnswer(energieClashAnswers.get(3), fourthAnswerTextArea, fourthAnswerChoiceBox);
        updateEnergieClashAnswer(energieClashAnswers.get(4), fifthAnswerTextArea, fifthAnswerChoiceBox);
    }

    private void updateEnergieClashAnswer(EnergieClashAnswer energieClashAnswer, TextArea answerTextArea,
                                          ChoiceBox<String> answerChoiceBox) {
        energieClashAnswer.setAnswer(answerTextArea.getText());
        energieClashAnswer.setAnswerGrade(AnswerGrade.valueOf(answerChoiceBox.getValue()));
    }

    private void setEnergieClashRoundValuesToView() {
        questionTextArea.setText(energieClashRound.getQuestion());
        explanationTextArea.setText(energieClashRound.getExplanation());
        sourceTextArea.setText(energieClashRound.getSource());
        languageChoiceBox.setValue(energieClashRound.getLanguage().getAbbreviation());
        difficultyChoiceBox.setValue(energieClashRound.getDifficulty().getText(language));
        setEnergieClashAnswersToView(energieClashRound.getAnswers());
    }

    private void setEnergieClashAnswersToView(List<EnergieClashAnswer> energieClashAnswers) {
        setEnergieClashAnswerValuesToView(energieClashAnswers.get(0), firstAnswerTextArea, firstAnswerChoiceBox);
        setEnergieClashAnswerValuesToView(energieClashAnswers.get(1), secondAnswerTextArea, secondAnswerChoiceBox);
        setEnergieClashAnswerValuesToView(energieClashAnswers.get(2), thirdAnswerTextArea, thirdAnswerChoiceBox);
        setEnergieClashAnswerValuesToView(energieClashAnswers.get(3), fourthAnswerTextArea, fourthAnswerChoiceBox);
        setEnergieClashAnswerValuesToView(energieClashAnswers.get(4), fifthAnswerTextArea, fifthAnswerChoiceBox);
    }

    private void setEnergieClashAnswerValuesToView(EnergieClashAnswer energieClashAnswer, TextArea answerTextArea,
                                                   ChoiceBox<String> answerChoiceBox) {
        answerTextArea.setText(energieClashAnswer.getAnswer());
        answerChoiceBox.getSelectionModel().select(energieClashAnswer.getAnswerGrade().name());
    }

    private void setDefaultValuesToView() {
        questionTextArea.setText("Question");
        explanationTextArea.setText("Explanation");
        sourceTextArea.setText("Source");
        List.of(firstAnswerTextArea, secondAnswerTextArea, thirdAnswerTextArea, fourthAnswerTextArea,
                fifthAnswerTextArea)
            .forEach(TextArea -> TextArea.setText("Answer"));
        List<ChoiceBox<String>>
            list = List.of(firstAnswerChoiceBox, secondAnswerChoiceBox, thirdAnswerChoiceBox, fourthAnswerChoiceBox,
            fifthAnswerChoiceBox);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).getSelectionModel().select(i);
        }
    }

    private void setupTextAreaValidation() {
        textAreaMap.keySet()
            .forEach(textArea -> textArea.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if (!newValue) { // when focus is lost
                    Color borderColor = isInputValid(textArea) ? Color.WHITE : Color.RED;
                    BorderStroke borderStroke =
                        new BorderStroke(borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                            BorderStroke.DEFAULT_WIDTHS);
                    textArea.setBorder(new Border(borderStroke));
                    saveBtn.setDisable(textAreaMap.keySet().stream().anyMatch(e -> !isInputValid(e)));
                }
            }));
    }

    private boolean isInputValid(TextArea textArea) {
        System.out.println(textArea.getText().toCharArray().length);
        System.out.println(textAreaMap.get(textArea));
        System.out.println(!textArea.getText().isBlank()
            && textArea.getText().toCharArray().length < textAreaMap.get(textArea));
        return !textArea.getText().isBlank()
            && textArea.getText().toCharArray().length < textAreaMap.get(textArea);
    }

    private void setupChoiceBoxes() {
        difficultyChoiceBox.getItems().clear();
        difficultyChoiceBox.getItems()
            .addAll(Arrays.stream(Difficulty.values())
                .map(difficulty -> difficulty.getText(language))
                .toList());
        languageChoiceBox.getItems().clear();
        languageChoiceBox.getItems()
            .addAll(Arrays.stream(Language.values())
                .map(Language::getAbbreviation)
                .toList());
        List.of(firstAnswerChoiceBox, secondAnswerChoiceBox, thirdAnswerChoiceBox, fourthAnswerChoiceBox,
                fifthAnswerChoiceBox)
            .forEach(stringChoiceBox -> {
                stringChoiceBox.setDisable(true);
                stringChoiceBox.getItems().clear();
                stringChoiceBox.getItems()
                    .addAll(Arrays.stream(AnswerGrade.values())
                        .map(AnswerGrade::name)
                        .toList());
            });
    }

    public EnergieClashRound getEnergieClashRound() {
        return energieClashRound;
    }

    public void updateEnergieClashRoundAndRefresh(EnergieClashRound energieClashRound) {
        this.energieClashRound = energieClashRound;
        populateView();
    }

    public void createUpdateStatusPopup(boolean updateSuccessful) {
        Label updateLabel = new Label();
        updateLabel.getStyleClass().add("admin-content");
        Optional.ofNullable(updatePopup).ifPresent(PopupWindow::hide);
        updatePopup = new Popup();
        updateLabel.getStylesheets().add("energieClashViews/style.css");
        updateLabel.setText(
            updateSuccessful ? "Update successful!" : "Update unsuccessful! Try again or check logs for details.");
        updatePopup.getContent().add(updateLabel);
        updatePopup.show(adminDetailAnchorPane.getScene().getWindow());
    }
}
