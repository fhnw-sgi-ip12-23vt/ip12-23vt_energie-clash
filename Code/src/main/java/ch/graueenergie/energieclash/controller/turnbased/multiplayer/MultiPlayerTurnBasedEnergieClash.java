package ch.graueenergie.energieclash.controller.turnbased.multiplayer;

import ch.graueenergie.energieclash.controller.turnbased.TurnBasedEnergieClash;
import ch.graueenergie.energieclash.model.gamelogic.Difficulty;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRole;
import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.util.PropertyName;
import ch.graueenergie.energieclash.view.util.i2c.I2CLED;
import ch.graueenergie.energieclash.view.util.i2c.I2CLEDController;
import ch.graueenergie.energieclash.view.util.i2c.I2CLEDPlacement;
import ch.graueenergie.energieclash.view.util.SceneCreator;
import ch.graueenergie.energieclash.util.WinCriteria;
import ch.graueenergie.energieclash.view.game.EnergieClashEndView;
import ch.graueenergie.energieclash.view.game.EnergieClashExplanationView;
import ch.graueenergie.energieclash.view.game.EnergieClashRoundView;
import ch.graueenergie.energieclash.view.game.EnergieClashStartView;
import ch.graueenergie.energieclash.view.util.i2c.I2CLEDColor;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.graueenergie.energieclash.model.gamelogic.EnergieClashRole.SAVER;

public class MultiPlayerTurnBasedEnergieClash extends TurnBasedEnergieClash {
    private final WinCriteria winCriteria;
    private final int amountOfRounds;
    private final int goalGameScore;
    private int currentRound;

    private int addedSaverScore;
    private int addedWasterScore;

    private int turnedOnSaverLeds;
    private int turnedOnWasterLeds;

    public MultiPlayerTurnBasedEnergieClash(
        Language language,
        Difficulty difficulty,
        Stage saverStage, Stage wasterStage, EnergieClashPlayer saver,
        EnergieClashPlayer waster,
        WinCriteria winCriteria,
        int amountOfRounds, int goalGameScore) {
        super(language, difficulty, saverStage, wasterStage, saver, waster);
        this.winCriteria = winCriteria;
        this.amountOfRounds = amountOfRounds;
        this.goalGameScore = goalGameScore;
        this.currentRound = 0;
        this.addedSaverScore = 0;
        this.addedWasterScore = 0;
    }

    /**
     * Starts the game by creating new {@link EnergieClashStartView}s and displaying them on the screens.
     */
    @Override
    public void start() {
        currentRound = 0;

        //setup view controllers
        removePropertyChangeListenerFromViews(this);
        saverView = new EnergieClashStartView(saver, language);
        wasterView = new EnergieClashStartView(waster, language);
        addPropertyChangeListenerToViews(this);

        //setup stage for saver
        SceneCreator.setViewToStage(saverStage, saverView, "/energieClashViews/StartView.fxml", true);

        //setup stage for waster
        SceneCreator.setViewToStage(wasterStage, wasterView, "/energieClashViews/StartView.fxml", true);
        displayDefaultTotalScore();
    }

    private void handleStartViewAction() {
        //Fetch and set new EnergieClashRounds
        setEnergieClashRounds(model.getRandomEnergieClashRounds(getDifficulty(), language, amountOfRounds));
        if (areBothPlayersReady()) {
            setEnergieClashRoundView();
        }
    }

    private void handleEnergieClashRoundAnswerAction(Object source) {
        if (source instanceof EnergieClashRoundView view) {
            if (view.equals(saverView)) {
                addPointsToPlayerAndDisplay(saver.getRole()
                        .getPointsForAnswer(((EnergieClashRoundView) saverView).getSelectedAnswer().getAnswerGrade()),
                    saver);

            } else if (view.equals(wasterView)) {
                addPointsToPlayerAndDisplay(waster.getRole()
                        .getPointsForAnswer(((EnergieClashRoundView) wasterView).getSelectedAnswer().getAnswerGrade()),
                    waster);
            }
        }
        if (areBothPlayersReady()) {
            displayNewTotalScore();
            setExplanationView();
        }
    }

    private void handleEnergieClashRoundTimeViewTimeElapsedAction() {
        displayNewTotalScore();
        setExplanationView();
    }

    private void addPointsToPlayerAndDisplay(int points, EnergieClashPlayer player) {
        player.addPoints(points);
        I2CLEDPlacement placement;
        boolean reverseOrder;
        if (SAVER.equals(player.getRole())) {
            placement = I2CLEDPlacement.SAVER_SCORE;
            addedSaverScore = points;
            reverseOrder = true;
        } else {
            placement = I2CLEDPlacement.WASTER_SCORE;
            addedWasterScore = points;
            reverseOrder = false;
        }
        //Display Player Score
        List<I2CLED> leds = getI2cLeds().stream()
            .filter(i2CLED -> i2CLED.getPlacement().equals(placement))
            .collect(Collectors.toList());
        if (reverseOrder) {
            Collections.reverse(leds);
        }
        leds.forEach(I2CLED::turnOff);
        leds.stream().limit(points).forEach(I2CLED::turnOn);
    }

    private void displayNewTotalScore() {
        List<I2CLED> allLeds = getI2cLeds().stream() //Get LEDs for total gamescore
            .filter(i2CLED -> i2CLED.getPlacement().equals(I2CLEDPlacement.TOTAL_SCORE)).toList();

        turnedOnSaverLeds = (int) allLeds.stream()
            .filter(i2CLED -> i2CLED.getColor().equals(I2CLEDColor.SAVER))
            .filter(I2CLED::isOn)
            .count();
        int nrOfSaverLedsToTurnOn = calculateLedsToTurnOn(turnedOnSaverLeds, addedSaverScore, addedWasterScore);

        turnedOnWasterLeds = (int) allLeds.stream()
            .filter(i2CLED -> i2CLED.getColor().equals(I2CLEDColor.WASTER))
            .filter(I2CLED::isOn)
            .count();
        int nrOfWasterLedsToTurnOn = calculateLedsToTurnOn(turnedOnWasterLeds, addedWasterScore, addedSaverScore);
        displayTotalScore(nrOfSaverLedsToTurnOn, nrOfWasterLedsToTurnOn, true);
    }

    private int calculateLedsToTurnOn(int turnedOnLeds, int ownAddedScore, int opponentAddedScore) {
        return Math.max(0, Math.min(10, turnedOnLeds + (ownAddedScore - opponentAddedScore)));
    }

    private void displayDefaultTotalScore() {
        displayTotalScore(5, 5, false);
    }

    private void displayTotalScore(int nrOfSaverLeds, int nrOfWasterLeds, boolean blink) {
        List<I2CLED> allLeds = getI2cLeds().stream() //Get LEDs for total gamescore
            .filter(i2CLED -> i2CLED.getPlacement().equals(I2CLEDPlacement.TOTAL_SCORE)).toList();
        allLeds.forEach(I2CLED::turnOff);
        List<I2CLED> saverLeds = allLeds.stream()
            .filter(i2CLED -> i2CLED.getColor().equals(I2CLEDColor.SAVER))
            .limit(nrOfSaverLeds)
            .toList();

        List<I2CLED> wasterLeds = allLeds.stream()
            .filter(i2CLED -> i2CLED.getColor().equals(I2CLEDColor.WASTER))
            .collect(Collectors.toList());
        Collections.reverse(wasterLeds);
        wasterLeds = wasterLeds.stream()
            .limit(nrOfWasterLeds)
            .toList();

        if (blink) {
            List<I2CLED> ledsToMakeBlink = new ArrayList<>();
            if (nrOfSaverLeds > turnedOnSaverLeds) {
                ledsToMakeBlink = saverLeds;
            } else if (nrOfWasterLeds > turnedOnWasterLeds) {
                ledsToMakeBlink = wasterLeds;
            }
            makeChangedLedsBlink(ledsToMakeBlink);
        }
        saverLeds.forEach(I2CLED::turnOn);
        wasterLeds.forEach(I2CLED::turnOn);

        I2CLEDController.handleLeds(getI2cLeds());
    }

    private void makeChangedLedsBlink(List<I2CLED> leds) {
        new Thread(() -> {
            final int[] i = {0};
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                if (i[0] < 5) {
                    leds.forEach(I2CLED::toggle);
                    I2CLEDController.handleLeds(getI2cLeds());
                    i[0]++;
                } else {
                    leds.forEach(I2CLED::turnOn);
                    I2CLEDController.handleLeds(getI2cLeds());
                    scheduler.shutdown();
                }
            }, 0, 200, TimeUnit.MILLISECONDS);
        }).start();
    }

    private void handleExplanationViewAction() {
        if (areBothPlayersReady()) {
            if (doesWinnerExist()) {
                setEndView();
            } else {
                setEnergieClashRoundView();
            }
        }
    }

    private void handleEndViewAction() {
        if (areBothPlayersReady()) {
            getI2cLeds().forEach(I2CLED::turnOff);
            I2CLEDController.handleLeds(getI2cLeds());
            getSupport().firePropertyChange(PropertyName.GAME_RESTART.getPropertyChangeEvent(this));
        }
    }


    private void setEnergieClashRoundView() {
        String viewPath = "/energieClashViews/EnergieClashRoundView.fxml";
        //setup view controllers
        removePropertyChangeListenerFromViews(this);
        saverView = new EnergieClashRoundView(saver, getEnergieClashRounds().get(currentRound), language);
        wasterView = new EnergieClashRoundView(waster, getEnergieClashRounds().get(currentRound), language);
        addPropertyChangeListenerToViews(this);
        //setup stage for saver
        SceneCreator.setViewToStage(saverStage, saverView, viewPath, true);
        //setup stage for waster
        SceneCreator.setViewToStage(wasterStage, wasterView, viewPath, true);
    }

    private void setExplanationView() {
        String viewPath = "/energieClashViews/EnergieClashExplanationView.fxml";
        removePropertyChangeListenerFromViews(this);
        saverView =
            new EnergieClashExplanationView(saver, language,
                getEnergieClashRounds().get(currentRound).getExplanation());
        wasterView =
            new EnergieClashExplanationView(waster, language,
                getEnergieClashRounds().get(currentRound).getExplanation());
        currentRound++;
        addPropertyChangeListenerToViews(this);
        SceneCreator.setViewToStage(saverStage, saverView, viewPath, true);
        SceneCreator.setViewToStage(wasterStage, wasterView, viewPath, true);
    }

    private void setEndView() {
        String viewPath = "/energieClashViews/EnergieClashEndView.fxml";
        EnergieClashRole winner = null;
        I2CLEDColor color;
        if (saver.getPoints() > waster.getPoints()) {
            winner = saver.getRole();
            color = I2CLEDColor.SAVER;
        } else if (waster.getPoints() > saver.getPoints()) {
            winner = waster.getRole();
            color = I2CLEDColor.WASTER;
        } else {
            color = null;
        }
        removePropertyChangeListenerFromViews(this);
        saverView = new EnergieClashEndView(saver, language, winner);
        wasterView = new EnergieClashEndView(waster, language, winner);
        addPropertyChangeListenerToViews(this);
        SceneCreator.setViewToStage(saverStage, saverView, viewPath, true);
        SceneCreator.setViewToStage(wasterStage, wasterView, viewPath, true);
        // Make LEDs blink
        enablePartyMode(color);
    }

    private void enablePartyMode(I2CLEDColor color) {
        new Thread(() -> {
            List<I2CLED> i2CLEDS = getI2cLeds();
            i2CLEDS.stream()
                .filter(i2CLED -> i2CLED.getPlacement().equals(I2CLEDPlacement.TOTAL_SCORE))
                .filter(i2CLED -> !i2CLED.getColor().equals(color))
                .forEach(I2CLED::turnOff);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                if (!areBothPlayersReady()) {
                    Random rnd = new Random();
                    i2CLEDS.stream()
                        .filter(i2CLED -> i2CLED.getColor().equals(color))
                        .collect(Collectors.collectingAndThen(Collectors.toList(),
                            collected -> {
                                Collections.shuffle(collected, rnd);
                                return collected;
                            }))
                        .stream()
                        .limit(rnd.nextInt(i2CLEDS.size()))
                        .forEach(I2CLED::toggle);
                    I2CLEDController.handleLeds(getI2cLeds());
                } else {
                    scheduler.shutdown();
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
        }).start();
    }

    private boolean doesWinnerExist() {
        boolean ret = false;
        if (WinCriteria.AMOUNT_OF_ROUNDS.equals(winCriteria)) {
            ret = currentRound >= amountOfRounds;
        } else if (WinCriteria.GAME_SCORE.equals(winCriteria)) {
            ret = Stream.of(saver, waster).anyMatch(player -> player.getPoints() >= goalGameScore);
        }
        return ret;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (PropertyName.valueOf(evt.getPropertyName())) {
        case START -> handleStartViewAction();
        case ENERGIE_CLASH_ROUND_ANSWER -> handleEnergieClashRoundAnswerAction(evt.getSource());
        case ENERGIE_CLASH_ROUND_TIMER_ELAPSED -> handleEnergieClashRoundTimeViewTimeElapsedAction();
        case EXPLANATION -> handleExplanationViewAction();
        case END -> handleEndViewAction();
        default -> { /*nothing */ }
        }
    }
}
