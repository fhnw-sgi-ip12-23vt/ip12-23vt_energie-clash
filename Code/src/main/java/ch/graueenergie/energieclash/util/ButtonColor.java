package ch.graueenergie.energieclash.util;

import javafx.scene.image.Image;

public enum ButtonColor {

    RED("/images/btn_red.png"),
    GREEN("/images/btn_green.png"),
    YELLOW("/images/btn_yellow.png"),
    GREY("/images/btn_grey.png"),
    BLUE("/images/btn_blue.png");
    private final String buttonImage;

    ButtonColor(String buttonImagePath) {
        this.buttonImage = buttonImagePath;
    }

    public Image getButtonImage() {
        return new Image(buttonImage);
    }
}
