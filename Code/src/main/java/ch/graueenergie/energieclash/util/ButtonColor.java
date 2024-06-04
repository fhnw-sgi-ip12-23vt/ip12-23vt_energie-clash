package ch.graueenergie.energieclash.util;

import javafx.scene.paint.Color;

public enum ButtonColor {

    RED('■', Color.RED),
    GREEN('●', Color.GREEN),
    YELLOW('✖', Color.YELLOW),
    GREY('★', Color.GREY),
    BLUE('▲', Color.BLUE);
    private final char symbol;
    private final Color color;

    ButtonColor(char symbol, Color color) {
        this.symbol = symbol;
        this.color = color;
    }

    public char getSymbol() {
        return symbol;
    }

    public Color getColor() {
        return color;
    }
}
