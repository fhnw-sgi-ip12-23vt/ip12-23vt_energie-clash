package ch.graueenergie.energieclash.view.util.i2c;

import com.pi4j.exception.Pi4JException;
import com.pi4j.io.i2c.I2C;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class I2CLEDController {
    private static final Logger LOGGER = LogManager.getLogger(I2CLEDController.class);

    public static void handleLeds(List<I2CLED> leds) {
        Map<I2C, List<I2CLED>> map = leds.stream().collect(groupingBy(I2CLED::getI2c));
        for (Map.Entry<I2C, List<I2CLED>> entry : map.entrySet()) {
            I2C i2c = entry.getKey();
            List<I2CLED> list = entry.getValue();
            int i2cCommand = 255 - list.stream()
                .filter(I2CLED::isOn)
                .map(I2CLED::getAddress)
                .map(I2CAddress::getAddress)
                .reduce(0, Integer::sum);
            try {
                LOGGER.info(String.format("Attemping to write command %s1 to device %s2", i2cCommand, i2c));
                i2c.write(i2cCommand);
                LOGGER.info(String.format("Successfully wrote command %s1 to device %s2", i2cCommand, i2c));
            } catch (Pi4JException e) {
                LOGGER.error(String.format("Failed to write command %s1 to device %s2", i2cCommand, i2c));
                LOGGER.error(e);
            }
        }
    }
}
