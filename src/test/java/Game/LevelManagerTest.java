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
    void hardIsFasterThanMediumFromLevelOne() {
        LevelManager easy = new LevelManager(1);
        LevelManager medium = new LevelManager(2);
        LevelManager hard = new LevelManager(3);
        int easyDelta = moveDelta(easy.createEnemies(1).get(0));
        int mediumDelta = moveDelta(medium.createEnemies(1).get(0));
        int hardDelta = moveDelta(hard.createEnemies(1).get(0));
        assertTrue(easyDelta < mediumDelta);
        assertTrue(mediumDelta < hardDelta);
        assertEquals(GameBalance.HARD_ENEMY_SPEED, hardDelta);
    }

    @Test
    void hardDifficultyScalesVelocityWithLevel() {
        LevelManager hard = new LevelManager(3);
        Enemy sample = hard.createEnemies(2).get(0);
        int before = sample.getXPosition();
        sample.move();
        assertEquals(before + GameBalance.enemySpeed(3, 2), sample.getXPosition());
        assertTrue(GameBalance.enemySpeed(3, 8) > GameBalance.enemySpeed(3, 1));
        assertTrue(GameBalance.enemySpeed(3, 1) > GameBalance.enemySpeed(2, 1));
    }

    @Test
    void bossSpeedsStayRankedAcrossDifficulties() {
        assertTrue(GameBalance.bossSpeed(1, 3) < GameBalance.bossSpeed(2, 3));
        assertTrue(GameBalance.bossSpeed(2, 3) < GameBalance.bossSpeed(3, 3));
        assertTrue(GameBalance.bossSpeed(1, 15) < GameBalance.bossSpeed(2, 15));
        assertTrue(GameBalance.bossSpeed(2, 15) < GameBalance.bossSpeed(3, 15));
        Enemy easyBoss = new LevelManager(1).createBoss(15);
        Enemy hardBoss = new LevelManager(3).createBoss(15);
        assertTrue(Math.abs(easyBoss.getXVelocity()) < Math.abs(hardBoss.getXVelocity()));
        assertTrue(Math.abs(hardBoss.getXVelocity()) <= GameBalance.BOSS_MAX_SPEED);
    }

    @Test
    void startingLivesFollowDifficulty() {
        assertEquals(GameBalance.LIVES_EASY, GameBalance.startingLives(1));
        assertEquals(GameBalance.LIVES_MEDIUM, GameBalance.startingLives(2));
        assertEquals(GameBalance.LIVES_HARD, GameBalance.startingLives(3));
    }

    private static int moveDelta(Enemy enemy) {
        int before = enemy.getXPosition();
        enemy.move();
        return enemy.getXPosition() - before;
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
    void bossMinionsMatchBossDirection() {
        LevelManager levels = new LevelManager(3);
        Enemy boss = levels.createBoss(9);
        boss.setXVelocity(-Math.abs(boss.getXVelocity()));
        boss.setXPosition(400);
        List<Enemy> minions = levels.createBossMinions(9, boss);
        assertEquals(2, minions.size());
        assertTrue(minions.get(0).getXVelocity() < 0);
        assertEquals(minions.get(0).getXVelocity(), minions.get(1).getXVelocity());
        assertTrue(minions.get(0).getXPosition() >= EnemyFormation.LEFT_BOUND);
        assertTrue(minions.get(1).getXPosition() + 40 <= EnemyFormation.RIGHT_BOUND);
    }

    @Test
    void hardBossSpeedIsCapped() {
        LevelManager hard = new LevelManager(3);
        Enemy boss = hard.createBoss(15);
        assertTrue(Math.abs(boss.getXVelocity()) <= GameBalance.BOSS_MAX_SPEED);
        assertTrue(Math.abs(boss.getXVelocity()) >= 1);
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
        assertEquals(GameBalance.MAX_LIVES, new LevelManager(1).createLifeIcons(GameBalance.MAX_LIVES).size());
    }

    @Test
    void invalidDifficultyDefaultsToEasy() {
        assertEquals(1, new LevelManager(0).getTipoDificultad());
        assertEquals(1, new LevelManager(-3).getTipoDificultad());
    }
}
