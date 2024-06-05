package ch.graueenergie.energieclash.model.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnergieClashPlayerTest {
    private EnergieClashPlayer player;

    @BeforeEach
    void setUp() {
        player = new EnergieClashPlayer(EnergieClashRole.SAVER);
    }

    @Test
    void getRole() {
        assertEquals(EnergieClashRole.SAVER, player.getRole());
    }

    @Test
    void getPoints() {
        assertEquals(0, player.getPoints());
    }

    @Test
    void addPoints() {
        int points = 20;
        player.addPoints(points);
        assertEquals(points, player.getPoints());
    }
}
