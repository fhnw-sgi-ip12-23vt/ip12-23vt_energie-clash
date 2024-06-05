package ch.graueenergie.energieclash.view.admin;

import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRound;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.AbstractSynchView;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminDashboardView extends AbstractSynchView {
    @FXML
    TableView<EnergieClashRound> energieClashRoundTableView;
    @FXML
    Button addBtn;
    @FXML
    Button editBtn;
    @FXML
    Button exitBtn;

    @FXML
    ComboBox<String> languageFilterComboBox;
    @FXML
    ComboBox<String> difficultyFilterComboBox;
    private final List<EnergieClashRound> energieClashRounds;
    private static final String WILDCARD = "%";
    private EnergieClashRound selectedEnergieClashRound;

    public AdminDashboardView(Language language, List<EnergieClashRound> energieClashRounds) {
        super(language);
        this.energieClashRounds = energieClashRounds;
    }

    @FXML
    public void initialize() {
        setupScreen();
    }

    @Override
    protected void setupScreen() {
        super.setupScreen();
        languageFilterComboBox.setOnAction(this::updateTable);
        difficultyFilterComboBox.setOnAction(this::updateTable);
        addBtn.setOnAction(this::addQuestion);
        editBtn.setOnAction(this::editQuestion);
        exitBtn.setOnAction(this::exit);
        initTableColumns();
        initFilters();
    }

    private void initFilters() {
        difficultyFilterComboBox.getItems().add(WILDCARD);
        difficultyFilterComboBox.getItems().addAll(Arrays.stream(Difficulty.values())
            .map(difficulty -> difficulty.getText(Language.ENGLISH))
            .toList());
        difficultyFilterComboBox.setValue(WILDCARD);
        languageFilterComboBox.getItems().add(WILDCARD);
        languageFilterComboBox.getItems().addAll(Arrays.stream(Language.values())
                .map(Language::getAbbreviation)
                .toList());
        languageFilterComboBox.setValue(WILDCARD);
    }
    private void initTableColumns() {
        TableColumn<EnergieClashRound, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<EnergieClashRound, String> questionColumn = new TableColumn<>("Question");
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));

        TableColumn<EnergieClashRound, String> languageColumn = new TableColumn<>("Language");
        languageColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getLanguage().getAbbreviation()));

        TableColumn<EnergieClashRound, Integer> difficultyColumn = new TableColumn<>("Difficulty");
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        energieClashRoundTableView.getColumns()
                .addAll(List.of(idColumn, questionColumn, languageColumn, difficultyColumn));
        energieClashRoundTableView.getItems().addAll(energieClashRounds);
        energieClashRoundTableView.refresh();
    }
    @FXML
    private void updateTable(ActionEvent actionEvent) {
        List<EnergieClashRound> obsoleteEnergieClashRounds = new ArrayList<>();
        String difficultyFilterValue = difficultyFilterComboBox.getSelectionModel().getSelectedItem();
        if (!WILDCARD.equals(difficultyFilterValue)) {
            obsoleteEnergieClashRounds
                    .addAll(filterByDifficulty(energieClashRounds, Difficulty.getDifficulty(difficultyFilterValue)));
        }
        String languageFilterValue = languageFilterComboBox.getSelectionModel().getSelectedItem();
        if (!WILDCARD.equals(languageFilterValue)) {
            obsoleteEnergieClashRounds
                    .addAll(filterByLanguage(energieClashRounds, Language.getLanguage(languageFilterValue)));
        }
        energieClashRoundTableView.getItems().removeAll(energieClashRounds);
        energieClashRoundTableView.getItems().addAll(energieClashRounds);
        energieClashRoundTableView.getItems().removeAll(obsoleteEnergieClashRounds);
        energieClashRoundTableView.refresh();
    }
    private List<EnergieClashRound> filterByDifficulty(List<EnergieClashRound> list, Difficulty difficulty) {
        return new ArrayList<>(list).stream()
                .filter(energieClashRound -> energieClashRound.getDifficulty() != difficulty)
                .toList();
    }
    private List<EnergieClashRound> filterByLanguage(List<EnergieClashRound> list, Language language) {
        return new ArrayList<>(list).stream()
                .filter(energieClashRound -> !energieClashRound.getLanguage().equals(language))
                .toList();
    }
    @FXML
    private void addQuestion(ActionEvent actionEvent) {
        support.firePropertyChange(PropertyName.ADMIN_DASHBOARD_ADD.getPropertyChangeEvent(this));
    }

    @FXML
    private void editQuestion(ActionEvent actionEvent) {
        selectedEnergieClashRound = energieClashRoundTableView.getSelectionModel().getSelectedItem();
        support.firePropertyChange(PropertyName.ADMIN_DASHBOARD_EDIT.getPropertyChangeEvent(this));
    }

    @FXML
    private void exit(ActionEvent actionEvent) {
        support.firePropertyChange(PropertyName.ADMIN_DASHBOARD_EXIT.getPropertyChangeEvent(this));
    }
    public EnergieClashRound getSelectedEnergieClashRound() {
        return selectedEnergieClashRound;
    }
}
