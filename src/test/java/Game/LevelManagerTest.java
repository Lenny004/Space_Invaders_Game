package Game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelManagerTest {

    @Test
    void bossEveryThirdLevel() {
        LevelManager levels = new LevelManager(1);
        assertFalse(levels.isBossLevel(1));
        assertFalse(levels.isBossLevel(2));
        assertTrue(levels.isBossLevel(3));
        assertTrue(levels.isBossLevel(6));
        assertTrue(levels.isBossLevel(15));
    }

    @Test
    void victoryAfterLevel15() {
        LevelManager levels = new LevelManager(1);
        assertFalse(levels.isVictory(15));
        assertTrue(levels.isVictory(16));
    }

    @Test
    void normalWaveHasThirtyEnemies() {
        LevelManager levels = new LevelManager(1);
        List<Enemy> enemies = levels.createEnemies(1);
        assertEquals(30, enemies.size());
    }

    @Test
    void bossLevelCreatesSingleBoss() {
        LevelManager levels = new LevelManager(1);
        List<Enemy> enemies = levels.createEnemies(3);
        assertEquals(1, enemies.size());
        assertEquals(150, enemies.get(0).getBounds().width);
        assertEquals(150, enemies.get(0).getBounds().height);
    }

    @Test
    void easyDifficultyUsesSlowVelocity() {
        LevelManager easy = new LevelManager(1);
        Enemy sample = easy.createEnemies(2).get(0);
        int before = sample.getXPosition();
        sample.move();
        assertEquals(before + GameBalance.EASY_ENEMY_SPEED, sample.getXPosition());
    }

    @Test
    void mediumDifficultyUsesFixedVelocity() {
        LevelManager medium = new LevelManager(2);
        Enemy sample = medium.createEnemies(5).get(0);
        int before = sample.getXPosition();
        sample.move();
        assertEquals(before + GameBalance.MEDIUM_ENEMY_SPEED, sample.getXPosition());
    }

    @Test
    void hardDifficultyScalesVelocityWithLevel() {
        LevelManager hard = new LevelManager(3);
        Enemy sample = hard.createEnemies(2).get(0);
        int before = sample.getXPosition();
        sample.move();
        assertEquals(before + 2, sample.getXPosition());
    }

    @Test
    void createTutorialWaveHasFourSlowEnemies() {
        LevelManager levels = new LevelManager(3);
        List<Enemy> enemies = levels.createTutorialWave();
        assertEquals(4, enemies.size());
        int before = enemies.get(0).getXPosition();
        enemies.get(0).move();
        assertEquals(before + GameBalance.EASY_ENEMY_SPEED, enemies.get(0).getXPosition());
    }

    @Test
    void createShieldsBuildsNineBlocks() {
        assertEquals(9, new LevelManager(1).createShields().size());
    }

    @Test
    void createLifeIconsMatchesCount() {
        assertEquals(3, new LevelManager(1).createLifeIcons(3).size());
        assertEquals(0, new LevelManager(1).createLifeIcons(0).size());
    }

    @Test
    void invalidDifficultyDefaultsToEasy() {
        assertEquals(1, new LevelManager(0).getTipoDificultad());
        assertEquals(1, new LevelManager(-3).getTipoDificultad());
    }
}
