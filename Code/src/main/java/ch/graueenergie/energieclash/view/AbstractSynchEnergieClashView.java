package ch.graueenergie.energieclash.view;

import ch.graueenergie.energieclash.util.Language;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashPlayer;
import ch.graueenergie.energieclash.util.EnergieClashButton;
import ch.graueenergie.energieclash.view.util.PuiSupplier;
import ch.graueenergie.energieclash.util.Button;
import ch.graueenergie.energieclash.view.util.Translation;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.List;

/**
 * An abstract class for views which can be synchronised with other instances of itself.
 */
public abstract class AbstractSynchEnergieClashView extends AbstractSynchView {
    protected final EnergieClashPlayer energieClashPlayer;
    protected final List<EnergieClashButton> buttons;

    /**
     * Creates a new instance.
     *
     * @param energieClashPlayer The Player.
     * @param language           The {@link Language} of the game.
     */
    public AbstractSynchEnergieClashView(EnergieClashPlayer energieClashPlayer, Language language) {
        super(language);
        this.energieClashPlayer = energieClashPlayer;
        this.buttons = PuiSupplier.getButtons(energieClashPlayer.getRole());
    }

    protected abstract void setupButtons();

    protected void resetButtons() {
        buttons.forEach(Button::reset);
    }

    protected HBox createButtonHBox(EnergieClashButton button, String text) {
        ImageView imageView = new ImageView();
        imageView.setImage(button.getButtonColor().getButtonImage());

        Label textLbl = new Label(text);
        textLbl.setWrapText(true);
        HBox hBox = new HBox(imageView, textLbl);
        hBox.setSpacing(10);
        hBox.getStyleClass().add("answer");
        return hBox;
    }

    protected void displayLogo(ImageView logoImage) {
        if (logoImage != null) {
            logoImage.setImage(new Image("/images/logo.png"));
        }
    }

    protected void displayWaitingPane(StackPane stackPane) {
        stackPane.getChildren().add(new Label(Translation.WAITING_FOR_PLAYER_TEXT.getTextForLanguage(language)));
        stackPane.setVisible(true);
    }
}
