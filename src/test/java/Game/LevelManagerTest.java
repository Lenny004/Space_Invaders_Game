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
    void normalWaveHasEnemiesMatchingFormation() {
        LevelManager levels = new LevelManager(1);
        assertEquals(30, levels.createEnemies(1).size());
        assertEquals(25, levels.createEnemies(2).size());
        assertEquals(30, levels.createEnemies(4).size());
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
    void formationsDifferAcrossNormalLevels() {
        LevelManager levels = new LevelManager(1);
        assertEquals(LevelManager.Formation.GRID, levels.formationFor(1));
        assertEquals(LevelManager.Formation.V, levels.formationFor(2));
        assertEquals(LevelManager.Formation.STAGGERED, levels.formationFor(4));

        List<Enemy> grid = levels.createEnemies(1);
        List<Enemy> v = levels.createEnemies(2);
        List<Enemy> staggered = levels.createEnemies(4);

        assertEquals(30, grid.size());
        assertNotEquals(grid.get(0).getXPosition(), v.get(0).getXPosition());
        assertNotEquals(grid.get(0).getYPosition(), staggered.get(0).getYPosition());
        assertTrue(v.stream().anyMatch(Enemy::isZigzag) || staggered.stream().anyMatch(Enemy::isZigzag));
    }

    @Test
    void endlessSkipsCampaignVictory() {
        LevelManager levels = new LevelManager(1);
        assertTrue(levels.isVictory(16));
        assertFalse(levels.isVictory(16, true));
        assertTrue(levels.isVictory(16, false));
    }

    @Test
    void bossMinionsAreTwoSmallEnemies() {
        LevelManager levels = new LevelManager(1);
        List<Enemy> minions = levels.createBossMinions(3);
        assertEquals(2, minions.size());
        assertFalse(minions.get(0).isBoss());
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
