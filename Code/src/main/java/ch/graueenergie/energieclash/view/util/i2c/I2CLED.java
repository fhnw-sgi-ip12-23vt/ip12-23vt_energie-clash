package ch.graueenergie.energieclash.view.util.i2c;

import com.pi4j.io.i2c.I2C;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class I2CLED {
    private final I2CAddress address;
    private final I2CLEDPlacement placement;
    private final I2CLEDColor color;
    private boolean isOn = false;
    private final I2C i2c;
    private final Logger logger = LogManager.getLogger(getClass());

    public I2CLED(I2C i2c, I2CAddress address, I2CLEDPlacement placement) {
        this.i2c = i2c;
        this.address = address;
        this.placement = placement;
        this.color = I2CLEDColor.REGULAR;
    }

    public I2CLED(I2C i2c, I2CAddress address, I2CLEDPlacement placement, I2CLEDColor color) {
        this.i2c = i2c;
        this.address = address;
        this.placement = placement;
        this.color = color;
    }

    public I2CAddress getAddress() {
        return address;
    }

    public I2C getI2c() {
        return i2c;
    }

    public boolean isOn() {
        return isOn;
    }

    public I2CLEDPlacement getPlacement() {
        return placement;
    }

    public I2CLEDColor getColor() {
        return color;
    }

    public void toggle() {
        logger.info(String.format("Toggling: %s", address.toString()));
        isOn = !isOn;
    }

    public void turnOn() {
        logger.info(String.format("Turning on: %s", address.toString()));
        isOn = true;
    }

    public void turnOff() {
        logger.info(String.format("Turning off: %s", address.toString()));
        isOn = false;
    }
}
