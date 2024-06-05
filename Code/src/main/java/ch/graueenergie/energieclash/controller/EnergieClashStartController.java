package ch.graueenergie.energieclash.controller;

import ch.graueenergie.energieclash.controller.rapidfire.multiplayer.MultiPlayerRapidFireEnergieClashFactory;
import ch.graueenergie.energieclash.controller.turnbased.multiplayer.MultiPlayerTurnBasedEnergieClashFactory;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRole;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRound;
import ch.graueenergie.energieclash.util.GameMode;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.util.SceneCreator;
import ch.graueenergie.energieclash.view.admin.AdminDashboardView;
import ch.graueenergie.energieclash.view.admin.AdminDetailView;
import ch.graueenergie.energieclash.view.gamestart.EnergieClashGameDifficultyView;
import ch.graueenergie.energieclash.view.gamestart.EnergieClashGameModeView;
import ch.graueenergie.energieclash.view.gamestart.EnergieClashGameStartView;
import ch.graueenergie.energieclash.view.gamestart.EnergieClashGameTutorialView;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

public class EnergieClashStartController extends Controller implements PropertyChangeListener {
    private final List<GameMode> availableGameModes;
    private final Properties appProps;
    private GameMode selectedGameMode;
    private Difficulty selectedDifficulty;
    private EnergieClash energieClash;

    public EnergieClashStartController(Properties appProps, Stage saverStage, Stage wasterStage) {
        super(saverStage, wasterStage, new EnergieClashPlayer(EnergieClashRole.SAVER),
            new EnergieClashPlayer(EnergieClashRole.WASTER),
            Language.getLanguage(appProps.getProperty("language", "de")));
        this.appProps = appProps;
        availableGameModes =
            Arrays.stream(appProps.getProperty("gameModes")
                .split(","))
                .map(GameMode::valueOf)
                .toList();
    }

    @Override
    public void start() {
        //Setup gamemode selection
        removePropertyChangeListenerFromViews(this);
        saverView = new EnergieClashGameStartView(saver, language);
        wasterView = new EnergieClashGameStartView(waster, language);
        addPropertyChangeListenerToViews(this);
        SceneCreator.setViewToStage(saverStage, saverView,
            "/energieClashViews/gameStart/EnergieClashGameStartView.fxml", false);
        SceneCreator.setViewToStage(wasterStage, wasterView,
            "/energieClashViews/gameStart/EnergieClashGameStartView.fxml", false);
    }


    private void handleGameStarterStart() {
        if (areBothPlayersReady()) {
            removePropertyChangeListenerFromViews(this);
            if (availableGameModes.size() > 1) {
                saverView = new EnergieClashGameModeView(saver, language, availableGameModes);
                wasterView = new EnergieClashGameModeView(waster, language, availableGameModes);
                addPropertyChangeListenerToViews(this);
                SceneCreator.setViewToStage(saverStage, saverView,
                    "/energieClashViews/gameStart/EnergieClashGameModeView.fxml", true);
                SceneCreator.setViewToStage(wasterStage, wasterView,
                    "/energieClashViews/gameStart/EnergieClashGameModeView.fxml", true);
            } else {
                handleGameStarterGameMode(null);
            }
        }
    }

    private void handleGameStarterAdmin() {
        setAdminDashboardView();
    }

    private void handleGameStarterGameMode(Object source) {
        selectedGameMode =
            Optional.ofNullable(source)
                .map(o -> ((EnergieClashGameModeView) o).getSelectedGameMode())
                .orElseGet(() -> availableGameModes.stream()
                    .findFirst()
                    .orElseThrow());
        removePropertyChangeListenerFromViews(this);
        saverView = new EnergieClashGameDifficultyView(saver, language);
        wasterView = new EnergieClashGameDifficultyView(waster, language);
        addPropertyChangeListenerToViews(this);
        SceneCreator.setViewToStage(saverStage, saverView,
            "/energieClashViews/gameStart/EnergieClashGameDifficultyView.fxml", true);
        SceneCreator.setViewToStage(wasterStage, wasterView,
            "/energieClashViews/gameStart/EnergieClashGameDifficultyView.fxml", true);

    }

    private void handleGameStarterDifficulty(Object source) {
        if (source instanceof EnergieClashGameDifficultyView view) {
            selectedDifficulty = view.getSelectedDifficulty();
            removePropertyChangeListenerFromViews(this);
            saverView = new EnergieClashGameTutorialView(saver, language, selectedDifficulty);
            wasterView = new EnergieClashGameTutorialView(waster, language, selectedDifficulty);
            addPropertyChangeListenerToViews(this);
            SceneCreator.setViewToStage(saverStage, saverView,
                "/energieClashViews/gameStart/EnergieClashGameTutorialView.fxml", true);
            SceneCreator.setViewToStage(wasterStage, wasterView,
                "/energieClashViews/gameStart/EnergieClashGameTutorialView.fxml", true);
        }
    }

    private void handleGameStarterTutorial() {
        if (areBothPlayersReady()) {
            createGameAndStart();
        }
    }

    private void handleAdminDashboardAddViewAction() {
        setAdminCreateDetailView();
    }

    private void handleAdminDashboardEditViewAction(Object o) {
        if (o instanceof AdminDashboardView adminDashboardView) {
            setAdminEditDetailView(adminDashboardView.getSelectedEnergieClashRound());
        }
    }

    private void handleAdminDashboardExitViewAction() {
        start();
    }

    private void handleAdminDetailSaveViewAction() {
        AdminDetailView adminDetailView = getFirstViewOfType(AdminDetailView.class);
        EnergieClashRound energieClashRound = adminDetailView.getEnergieClashRound();
        boolean updateSuccessful;
        if (energieClashRound.getId() == null) {
            updateSuccessful = model.addQuestion(energieClashRound);
        } else {
            updateSuccessful = model.updateQuestion(energieClashRound);
        }
        adminDetailView.createUpdateStatusPopup(updateSuccessful);
        adminDetailView.updateEnergieClashRoundAndRefresh(
            model.getEnergieClashRoundByQuestion(energieClashRound.getQuestion()));

    }

    private void handleAdminDetailDeleteViewAction() {
        AdminDetailView adminDetailView = getFirstViewOfType(AdminDetailView.class);
        adminDetailView.createUpdateStatusPopup(model.deleteQuestion(adminDetailView.getEnergieClashRound()));
    }

    private void handleAdminDetailExitViewAction() {
        setAdminDashboardView();
    }

    private <T> T getFirstViewOfType(Class<T> clazz) {
        return Stream.of(saverView, wasterView)
            .filter(clazz::isInstance)
            .findFirst()
            .map(clazz::cast)
            .orElseThrow();
    }

    private void setAdminDashboardView() {
        String viewPath = "/energieClashViews/admin/AdminDashboardView.fxml";
        List<EnergieClashRound> energieClashRounds = model.getAllEnergieClashRounds();
        saverView.removePropertyChangeListener(this);
        saverView = new AdminDashboardView(language, energieClashRounds);
        saverView.addPropertyChangeListener(this);
        SceneCreator.setViewToStage(saverStage, saverView, viewPath, false);
    }

    private void setAdminEditDetailView(EnergieClashRound energieClashRound) {
        setAdminDetailView(new AdminDetailView(language, energieClashRound));
    }

    private void setAdminCreateDetailView() {
        setAdminDetailView(new AdminDetailView(language));
    }

    private void setAdminDetailView(AdminDetailView adminDetailView) {
        String viewPath = "/energieClashViews/admin/AdminDetailView.fxml";
        saverView.removePropertyChangeListener(this);
        saverView = adminDetailView;
        saverView.addPropertyChangeListener(this);
        SceneCreator.setViewToStage(saverStage, saverView, viewPath, false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            switch (PropertyName.valueOf(evt.getPropertyName())) {
            case GAME_STARTER_START -> handleGameStarterStart();
            case GAME_STARTER_ADMIN -> handleGameStarterAdmin();
            case GAME_STARTER_GAME_MODE -> handleGameStarterGameMode(evt.getSource());
            case GAME_STARTER_DIFFICULTY -> handleGameStarterDifficulty(evt.getSource());
            case GAME_STARTER_TUTORIAL -> handleGameStarterTutorial();
            case ADMIN_DASHBOARD_ADD -> handleAdminDashboardAddViewAction();
            case ADMIN_DASHBOARD_EDIT -> handleAdminDashboardEditViewAction(evt.getSource());
            case ADMIN_DASHBOARD_EXIT -> handleAdminDashboardExitViewAction();
            case ADMIN_DETAIL_SAVE -> handleAdminDetailSaveViewAction();
            case ADMIN_DETAIL_DELETE -> handleAdminDetailDeleteViewAction();
            case ADMIN_DETAIL_EXIT -> handleAdminDetailExitViewAction();
            case GAME_RESTART -> start();
            default -> throw new IllegalStateException("Unexpected value: " + evt.getPropertyName());
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e);
        }
    }

    private void createGameAndStart() {
        Optional.ofNullable(energieClash).ifPresent(e -> e.removePropertyChangeListener(this));
        if (GameMode.TURNBASED.equals(selectedGameMode)) {
            MultiPlayerTurnBasedEnergieClashFactory factory = new MultiPlayerTurnBasedEnergieClashFactory(appProps);
            energieClash = factory.createGame(saverStage, wasterStage, language, selectedDifficulty);
            energieClash.addPropertyChangeListener(this);
            energieClash.start();
        } else if (GameMode.RAPIDFIRE.equals(selectedGameMode)) {
            MultiPlayerRapidFireEnergieClashFactory factory = new MultiPlayerRapidFireEnergieClashFactory(appProps);
            energieClash = factory.createGame(saverStage, wasterStage, language, selectedDifficulty);
            energieClash.addPropertyChangeListener(this);
            energieClash.start();
        }
    }
}
