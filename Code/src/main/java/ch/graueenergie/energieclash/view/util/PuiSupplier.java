package ch.graueenergie.energieclash.view.util;

import ch.graueenergie.energieclash.model.gamelogic.EnergieClashRole;
import ch.graueenergie.energieclash.util.ButtonColor;
import ch.graueenergie.energieclash.util.EnergieClashButton;
import ch.graueenergie.energieclash.view.util.i2c.I2CAddress;
import ch.graueenergie.energieclash.view.util.i2c.I2CLED;
import ch.graueenergie.energieclash.view.util.i2c.I2CLEDColor;
import ch.graueenergie.energieclash.view.util.i2c.I2CLEDPlacement;
import com.pi4j.Pi4J;
import com.pi4j.catalog.components.base.PIN;
import com.pi4j.catalog.components.util.Pi4JContext;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The supplier of PUI-components.
 */
public class PuiSupplier {
    private static final Logger LOGGER = LogManager.getLogger(PuiSupplier.class);

    /**
     * Creates {@link EnergieClashButton}s and returns them.
     *
     * @param role The {@link EnergieClashRole} whose buttons need to be returned.
     * @return a {@link List} of {@link EnergieClashButton}s
     */
    public static List<EnergieClashButton> getButtons(EnergieClashRole role) {
        return createButtons(Pi4JContext.createContext()).stream()
            .filter(button -> button.getButtonOwner() == role)
            .collect(Collectors.toList());
    }

    private static List<EnergieClashButton> createButtons(Context pi4j) {
        List<EnergieClashButton> buttons = new ArrayList<>();
        buttons.add(new EnergieClashButton(pi4j, PIN.D4, Boolean.TRUE, EnergieClashRole.WASTER, ButtonColor.GREEN));
        buttons.add(new EnergieClashButton(pi4j, PIN.TXD, Boolean.TRUE, EnergieClashRole.WASTER, ButtonColor.BLUE));
        buttons.add(new EnergieClashButton(pi4j, PIN.RXD, Boolean.TRUE, EnergieClashRole.WASTER, ButtonColor.GREY));
        buttons.add(new EnergieClashButton(pi4j, PIN.D17, Boolean.TRUE, EnergieClashRole.WASTER, ButtonColor.YELLOW));
        buttons.add(new EnergieClashButton(pi4j, PIN.PWM18, Boolean.TRUE, EnergieClashRole.WASTER, ButtonColor.RED));

        buttons.add(new EnergieClashButton(pi4j, PIN.D27, Boolean.TRUE, EnergieClashRole.SAVER, ButtonColor.GREEN));
        buttons.add(new EnergieClashButton(pi4j, PIN.D22, Boolean.TRUE, EnergieClashRole.SAVER, ButtonColor.BLUE));
        buttons.add(new EnergieClashButton(pi4j, PIN.D23, Boolean.TRUE, EnergieClashRole.SAVER, ButtonColor.GREY));
        buttons.add(new EnergieClashButton(pi4j, PIN.D24, Boolean.TRUE, EnergieClashRole.SAVER, ButtonColor.YELLOW));
        buttons.add(new EnergieClashButton(pi4j, PIN.MOSI, Boolean.TRUE, EnergieClashRole.SAVER, ButtonColor.RED));
        return buttons;
    }

    public static List<I2CLED> getI2CLEDs() {
        List<I2CLED> leds = new ArrayList<>();
        try {
            //Create LEDs for first I2C
            I2C i2c = createI2C("PCF8574", 0x20);
            //Regular
            leds.add(new I2CLED(i2c, I2CAddress.I2C1, I2CLEDPlacement.SAVER_SCORE));
            leds.add(new I2CLED(i2c, I2CAddress.I2C2, I2CLEDPlacement.SAVER_SCORE));
            leds.add(new I2CLED(i2c, I2CAddress.I2C3, I2CLEDPlacement.SAVER_SCORE));
            leds.add(new I2CLED(i2c, I2CAddress.I2C4, I2CLEDPlacement.SAVER_SCORE));
            leds.add(new I2CLED(i2c, I2CAddress.I2C5, I2CLEDPlacement.SAVER_SCORE));
            leds.add(new I2CLED(i2c, I2CAddress.I2C6, I2CLEDPlacement.WASTER_SCORE));
            leds.add(new I2CLED(i2c, I2CAddress.I2C7, I2CLEDPlacement.WASTER_SCORE));
            leds.add(new I2CLED(i2c, I2CAddress.I2C8, I2CLEDPlacement.WASTER_SCORE));

            I2C i3c = createI2C("PCF8574", 0x21);
            //Green
            leds.add(new I2CLED(i3c, I2CAddress.I2C1, I2CLEDPlacement.WASTER_SCORE));
            leds.add(new I2CLED(i3c, I2CAddress.I2C2, I2CLEDPlacement.WASTER_SCORE));
            leds.add(new I2CLED(i3c, I2CAddress.I2C3, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));
            leds.add(new I2CLED(i3c, I2CAddress.I2C4, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));
            leds.add(new I2CLED(i3c, I2CAddress.I2C5, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));
            leds.add(new I2CLED(i3c, I2CAddress.I2C6, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));
            leds.add(new I2CLED(i3c, I2CAddress.I2C7, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));
            leds.add(new I2CLED(i3c, I2CAddress.I2C8, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));

            I2C i4c = createI2C("PCF8574", 0x22);
            leds.add(new I2CLED(i4c, I2CAddress.I2C1, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));
            leds.add(new I2CLED(i4c, I2CAddress.I2C2, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));
            leds.add(new I2CLED(i4c, I2CAddress.I2C3, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));
            leds.add(new I2CLED(i4c, I2CAddress.I2C4, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.WASTER));
            //Red
            leds.add(new I2CLED(i4c, I2CAddress.I2C5, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            leds.add(new I2CLED(i4c, I2CAddress.I2C6, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            leds.add(new I2CLED(i4c, I2CAddress.I2C7, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            leds.add(new I2CLED(i4c, I2CAddress.I2C8, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            I2C i5c = createI2C("PCF8574", 0x23);
            //Red
            leds.add(new I2CLED(i5c, I2CAddress.I2C1, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            leds.add(new I2CLED(i5c, I2CAddress.I2C2, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            leds.add(new I2CLED(i5c, I2CAddress.I2C3, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            leds.add(new I2CLED(i5c, I2CAddress.I2C4, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            leds.add(new I2CLED(i5c, I2CAddress.I2C5, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            leds.add(new I2CLED(i5c, I2CAddress.I2C6, I2CLEDPlacement.TOTAL_SCORE, I2CLEDColor.SAVER));
            LOGGER.info("Successfully created I2CLEDs");
        } catch (Exception e) {
            LOGGER.error("Failed to create I2C LEDs");
            LOGGER.error(e);
        }
        return leds;
    }

    private static I2C createI2C(String id, int device) {
        Context pi4j = Pi4J.newAutoContext();
        I2CProvider i2CProvider = pi4j.provider("linuxfs-i2c");
        I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j).id(id).bus(1).device(device).build();
        return i2CProvider.create(i2cConfig);
    }
}
