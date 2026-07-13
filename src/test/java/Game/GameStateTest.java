package Game;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void bulletAndSpeedLevelsAreClamped() {
        GameState state = new GameState();
        state.setBulletLevel(99);
        state.setSpeedLevel(-5);
        assertEquals(6, state.getBulletLevel());
        assertEquals(0, state.getSpeedLevel());
    }

    @Test
    void raiseLevelsNeverDecrease() {
        GameState state = new GameState();
        state.setBulletLevel(3);
        state.raiseBulletLevel(2);
        assertEquals(3, state.getBulletLevel());
        state.raiseBulletLevel(5);
        assertEquals(5, state.getBulletLevel());
    }

    @Test
    void addScoreAndElements() {
        GameState state = new GameState();
        state.addScore(10);
        state.addScore(5);
        assertEquals(15, state.getScore());

        state.addElement(0, 2);
        state.addElement(11, 1);
        state.addElement(99, 5); // índice inválido, ignorado
        assertEquals(2, state.getElementCount(0));
        assertEquals(1, state.getElementCount(11));
    }

    @Test
    void resetForNewGameClearsProgress() {
        GameState state = new GameState();
        state.setScore(500);
        state.setLevel(8);
        state.setBulletLevel(4);
        state.setSpeedLevel(3);
        state.addElement(1, 9);
        state.setCraftUsed(1, true);
        state.getProjectiles().add(new Projectile(1, 1, Color.RED));
        state.getEnemies().add(new Enemy(0, 0, 1, 0, 0, null, 40, 40, 1));

        state.resetForNewGame();

        assertEquals(0, state.getScore());
        assertEquals(1, state.getLevel());
        assertEquals(0, state.getBulletLevel());
        assertEquals(0, state.getSpeedLevel());
        assertEquals(0, state.getElementCount(1));
        assertFalse(state.isCraftUsed(1));
        assertTrue(state.getProjectiles().isEmpty());
        assertTrue(state.getEnemies().isEmpty());
        assertEquals(3, state.getLivesCount());
    }

    @Test
    void activeProjectilesFiltersInactive() {
        GameState state = new GameState();
        Projectile live = new Projectile(10, 50, Color.ORANGE);
        Projectile dead = new Projectile(10, 50, Color.BLUE);
        dead.deactivate();
        state.getProjectiles().add(live);
        state.getProjectiles().add(dead);

        assertEquals(1, state.activeProjectiles().size());
        assertTrue(state.activeProjectiles().get(0).isActive());
    }

    @Test
    void hitMarkerStoresCoordinates() {
        GameState state = new GameState();
        state.setHitMarkerAt(100, 200);
        assertTrue(state.isHitMarker());
        assertEquals(100, state.getMarkerX());
        assertEquals(200, state.getMarkerY());
    }

    @Test
    void nextLevelIncrements() {
        GameState state = new GameState();
        state.nextLevel();
        assertEquals(2, state.getLevel());
    }
}
