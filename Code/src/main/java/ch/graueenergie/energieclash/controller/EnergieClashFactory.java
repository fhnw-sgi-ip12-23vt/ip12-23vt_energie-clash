package ch.graueenergie.energieclash.controller;

import ch.graueenergie.energieclash.model.EnergieClashModel;

import java.util.Properties;

/**
 * Abstract Factory of EnergieClash.
 */
public abstract class EnergieClashFactory extends GameFactory {

    /**
     * Creates a new instance with a new {@link EnergieClashModel}.
     *
     * @param appProps the {@link Properties} containing the needed information to create the right {@link EnergieClash}
     */
    public EnergieClashFactory(Properties appProps) {
        super(appProps);
    }
}
