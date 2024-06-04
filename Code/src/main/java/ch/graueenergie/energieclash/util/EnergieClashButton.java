package ch.graueenergie.energieclash.util;

import ch.graueenergie.energieclash.controller.EnergieClash;
import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRole;
import com.pi4j.catalog.components.base.PIN;
import com.pi4j.context.Context;
import com.pi4j.exception.Pi4JException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Buttonclass used for {@link EnergieClash}.
 */
public class EnergieClashButton extends Button {
    private static final String LOG_MESSAGE = "Button of: %1$s on PIN: %2$s was pressed!";
    private final EnergieClashRole buttonOwner;
    private final ButtonColor buttonColor;
    private static final Logger LOGGER = LogManager.getLogger(EnergieClashButton.class);

    /**
     * Creates a new instance.
     *
     * @param pi4j     the pi4j{@link Context}.
     * @param address  the {@link PIN}-address.
     * @param inverted whether the button should be inverted or not.
     * @param owner    the {@link EnergieClashRole} whose button it is.
     * @param color    the {@link ButtonColor} of the button.
     */
    public EnergieClashButton(Context pi4j, PIN address, boolean inverted, EnergieClashRole owner, ButtonColor color) {
        super(pi4j, address, inverted);
        this.buttonOwner = owner;
        this.buttonColor = color;
    }

    @Override
    public void onDown(Runnable task) {
        super.onDown(() -> logOnDown(task));
    }

    private void logOnDown(Runnable task) {
        LOGGER.info(String.format(LOG_MESSAGE, buttonOwner.getRoleName(), super.pinNumber()));
        task.run();
    }

    /**
     * @return a {@link EnergieClashRole}.
     */
    public EnergieClashRole getButtonOwner() {
        return buttonOwner;
    }

    public ButtonColor getButtonColor() {
        return buttonColor;
    }

    public static Pi4JException getButtonException() {
        return new Pi4JException("Button was not found.");
    }
}
