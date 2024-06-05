//open module com.pi4j.mvc {
open module ch.graueenergie {

    // Pi4J Modules
    requires pi4jlib;
    requires com.pi4j;
    requires com.pi4j.library.pigpio;
    requires com.pi4j.library.linuxfs;
    requires com.pi4j.plugin.pigpio;
    requires com.pi4j.plugin.raspberrypi;
    requires com.pi4j.plugin.mock;
    requires com.pi4j.plugin.linuxfs;
    uses com.pi4j.extension.Extension;
    uses com.pi4j.provider.Provider;

    // for logging
    requires org.apache.logging.log4j;

    // JavaFX
    requires javafx.base;
    requires javafx.controls;
    requires java.desktop;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires javafx.fxml;

    //

    // Module Exports
}
