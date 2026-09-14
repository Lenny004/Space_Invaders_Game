package Game;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComboSystemTest {

    @Test
    void multiplierScalesWithComboAndCaps() {
        ComboSystem combo = new ComboSystem();
        assertEquals(1, combo.registerKill());
        assertEquals(1, combo.getMultiplier());
        combo.registerKill(); // 2 -> mult 1
        assertEquals(1, combo.getMultiplier());
        combo.registerKill(); // 3 -> mult 2
        assertEquals(2, combo.getMultiplier());
        for (int i = 0; i < 20; i++) {
            combo.registerKill();
        }
        assertEquals(GameBalance.COMBO_MAX_MULTIPLIER, combo.getMultiplier());
    }

    @Test
    void playerHitResetsCombo() {
        ComboSystem combo = new ComboSystem();
        combo.registerKill();
        combo.registerKill();
        combo.registerKill();
        assertTrue(combo.isActive());
        combo.onPlayerHit();
        assertFalse(combo.isActive());
        assertEquals(1, combo.getMultiplier());
    }

    @Test
    void timeoutExpiresCombo() {
        ComboSystem combo = new ComboSystem();
        combo.registerKill();
        for (int i = 0; i < GameBalance.COMBO_TIMEOUT_TICKS; i++) {
            combo.tick();
        }
        assertFalse(combo.isActive());
    }

    @Test
    void scoreForAlienUsesMultiplier() {
        ComboSystem combo = new ComboSystem();
        combo.registerKill();
        combo.registerKill();
        combo.registerKill();
        assertEquals(GameBalance.SCORE_ALIEN * 2, combo.scoreForAlien());
    }
}

class TemporaryBuffSystemTest {

    @Test
    void shieldAbsorbsOneHit() {
        TemporaryBuffSystem buffs = new TemporaryBuffSystem();
        buffs.apply(BuffDrop.Type.SHIELD);
        assertTrue(buffs.hasShield());
        assertTrue(buffs.tryAbsorbHit());
        assertFalse(buffs.hasShield());
        assertFalse(buffs.tryAbsorbHit());
    }

    @Test
    void doubleFireRaisesBulletLevel() {
        TemporaryBuffSystem buffs = new TemporaryBuffSystem();
        assertEquals(0, buffs.effectiveBulletLevel(0));
        buffs.apply(BuffDrop.Type.DOUBLE_FIRE);
        assertEquals(2, buffs.effectiveBulletLevel(0));
        assertEquals(3, buffs.effectiveBulletLevel(1));
    }

    @Test
    void collectingBuffDropAppliesBuff() {
        TemporaryBuffSystem buffs = new TemporaryBuffSystem();
        List<BuffDrop> drops = new ArrayList<>();
        Ship ship = new Ship(100, 100, Color.YELLOW, null);
        drops.add(new BuffDrop(100, 100, BuffDrop.Type.SLOW));
        String feedback = buffs.updateDrops(drops, ship);
        assertEquals("+Z", feedback);
        assertTrue(buffs.hasSlow());
        assertTrue(drops.isEmpty());
    }

    @Test
    void buffExpiresAfterDuration() {
        TemporaryBuffSystem buffs = new TemporaryBuffSystem();
        buffs.apply(BuffDrop.Type.SLOW);
        for (int i = 0; i < GameBalance.BUFF_DURATION_TICKS; i++) {
            buffs.tick();
        }
        assertFalse(buffs.hasSlow());
    }

    @Test
    void collectingLifeDropGrantsPendingLifeWithoutTimedBuff() {
        TemporaryBuffSystem buffs = new TemporaryBuffSystem();
        List<BuffDrop> drops = new ArrayList<>();
        Ship ship = new Ship(100, 100, Color.YELLOW, null);
        drops.add(new BuffDrop(100, 100, BuffDrop.Type.LIFE));
        String feedback = buffs.updateDrops(drops, ship);
        assertEquals(Messages.get("buff.life"), feedback);
        assertEquals(1, buffs.consumePendingLives());
        assertEquals(0, buffs.consumePendingLives());
        assertFalse(buffs.hasShield());
        assertFalse(buffs.hasSlow());
        assertFalse(buffs.hasDoubleFire());
        assertTrue(drops.isEmpty());
    }

    @Test
    void rollTypeNeverReturnsLife() {
        java.util.Random rng = new java.util.Random(7);
        for (int i = 0; i < 80; i++) {
            assertNotEquals(BuffDrop.Type.LIFE, TemporaryBuffSystem.rollType(rng));
        }
    }

    @Test
    void shouldDropLifeRespectsChancePercent() {
        java.util.Random alwaysLow = new java.util.Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };
        java.util.Random alwaysHigh = new java.util.Random() {
            @Override
            public int nextInt(int bound) {
                return GameBalance.LIFE_DROP_CHANCE_PERCENT;
            }
        };
        assertTrue(TemporaryBuffSystem.shouldDropLife(alwaysLow));
        assertFalse(TemporaryBuffSystem.shouldDropLife(alwaysHigh));
        assertFalse(TemporaryBuffSystem.shouldDropLife(null));
    }

    @Test
    void shouldDropLifeOnBossRespectsChancePercent() {
        java.util.Random alwaysLow = new java.util.Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };
        java.util.Random alwaysHigh = new java.util.Random() {
            @Override
            public int nextInt(int bound) {
                return GameBalance.LIFE_DROP_CHANCE_BOSS_PERCENT;
            }
        };
        assertTrue(TemporaryBuffSystem.shouldDropLifeOnBoss(alwaysLow));
        assertFalse(TemporaryBuffSystem.shouldDropLifeOnBoss(alwaysHigh));
        assertFalse(TemporaryBuffSystem.shouldDropLifeOnBoss(null));
    }

    @Test
    void shouldDropLifeOnBonusRespectsChancePercent() {
        java.util.Random alwaysLow = new java.util.Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };
        java.util.Random alwaysHigh = new java.util.Random() {
            @Override
            public int nextInt(int bound) {
                return GameBalance.LIFE_DROP_CHANCE_BONUS_PERCENT;
            }
        };
        assertTrue(TemporaryBuffSystem.shouldDropLifeOnBonus(alwaysLow));
        assertFalse(TemporaryBuffSystem.shouldDropLifeOnBonus(alwaysHigh));
        assertFalse(TemporaryBuffSystem.shouldDropLifeOnBonus(null));
    }

    @Test
    void shouldSpawnLifeSkipsAtMaxLivesEvenIfRollSucceeds() {
        java.util.Random alwaysLow = new java.util.Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };
        assertFalse(TemporaryBuffSystem.shouldSpawnLife(
                alwaysLow, GameBalance.MAX_LIVES, 100));
        assertFalse(TemporaryBuffSystem.shouldSpawnLife(
                alwaysLow, GameBalance.MAX_LIVES + 1, GameBalance.LIFE_DROP_CHANCE_BOSS_PERCENT));
        assertTrue(TemporaryBuffSystem.shouldSpawnLife(
                alwaysLow, GameBalance.MAX_LIVES - 1, 100));
        assertTrue(TemporaryBuffSystem.shouldSpawnLife(
                alwaysLow, 0, GameBalance.LIFE_DROP_CHANCE_PERCENT));
    }

    @Test
    void bossAndBonusLifeDropCanSucceedWhenNormalWouldFail() {
        java.util.Random atNormalThreshold = new java.util.Random() {
            @Override
            public int nextInt(int bound) {
                return GameBalance.LIFE_DROP_CHANCE_PERCENT;
            }
        };
        assertFalse(TemporaryBuffSystem.shouldDropLife(atNormalThreshold));
        assertTrue(TemporaryBuffSystem.shouldDropLifeOnBonus(atNormalThreshold));
        assertTrue(TemporaryBuffSystem.shouldDropLifeOnBoss(atNormalThreshold));
    }

    @Test
    void lifeDropChancesStayRareAndBossIsHighest() {
        assertTrue(GameBalance.LIFE_DROP_CHANCE_PERCENT > 0);
        assertTrue(GameBalance.LIFE_DROP_CHANCE_PERCENT <= GameBalance.LIFE_DROP_CHANCE_BONUS_PERCENT);
        assertTrue(GameBalance.LIFE_DROP_CHANCE_BONUS_PERCENT <= GameBalance.LIFE_DROP_CHANCE_BOSS_PERCENT);
        assertTrue(GameBalance.LIFE_DROP_CHANCE_BOSS_PERCENT < GameBalance.BUFF_DROP_CHANCE_PERCENT);
    }
}

class BossPhaseTest {

    @Test
    void phasesFollowHealthThresholds() {
        assertEquals(BossPhase.OPENING, BossPhase.fromHealth(80, 100));
        assertEquals(BossPhase.RAGE, BossPhase.fromHealth(50, 100));
        assertEquals(BossPhase.DESPERATE, BossPhase.fromHealth(20, 100));
    }

    @Test
    void bossMaxHealthScalesByCampaignBoss() {
        assertEquals(30, GameBalance.bossMaxHealth(3));
        assertEquals(40, GameBalance.bossMaxHealth(6));
        assertEquals(85, GameBalance.bossMaxHealth(15));
        assertTrue(GameBalance.bossMaxHealth(18) >= GameBalance.bossMaxHealth(15));
    }

    @Test
    void bossHealthAndFireRateScaleWithDifficulty() {
        assertTrue(GameBalance.bossMaxHealth(6, 1) < GameBalance.bossMaxHealth(6, 2));
        assertTrue(GameBalance.bossMaxHealth(6, 2) < GameBalance.bossMaxHealth(6, 3));
        assertTrue(GameBalance.normalBeamChance(1) > GameBalance.normalBeamChance(2));
        assertTrue(GameBalance.normalBeamChance(2) > GameBalance.normalBeamChance(3));
        assertTrue(GameBalance.bossBeamChance(1) > GameBalance.bossBeamChance(3));
    }
}

class EnemyFormationTest {

    @Test
    void bossDoesNotEscapeWhenFasterThanMinions() {
        List<Enemy> enemies = new ArrayList<>();
        Enemy boss = new Enemy(820, 20, 12, 0, 100, null, 150, 150, 9);
        Enemy leftMinion = new Enemy(80, 80, 4, 0, 0, null, 40, 40, 9);
        Enemy rightMinion = new Enemy(400, 80, 4, 0, 1, null, 40, 40, 9);
        enemies.add(boss);
        enemies.add(leftMinion);
        enemies.add(rightMinion);

        for (int i = 0; i < 80; i++) {
            EnemyFormation.advance(enemies, true);
            assertTrue(boss.getXPosition() >= EnemyFormation.LEFT_BOUND, "boss escaped left on tick " + i);
            assertTrue(boss.getXPosition() + 150 <= EnemyFormation.RIGHT_BOUND, "boss escaped right on tick " + i);
            assertTrue(boss.getYPosition() <= EnemyFormation.BOSS_MAX_Y);
        }
        assertTrue(boss.getXVelocity() < 0, "should have bounced left after hitting the right wall");
    }

    @Test
    void offscreenBossIsClampedBackIntoArena() {
        Enemy boss = new Enemy(1400, 20, 15, 0, 100, null, 150, 150, 15);
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(boss);
        EnemyFormation.advance(enemies, true);
        assertTrue(boss.getXPosition() >= EnemyFormation.LEFT_BOUND);
        assertTrue(boss.getXPosition() + 150 <= EnemyFormation.RIGHT_BOUND);
        assertTrue(boss.getXVelocity() < 0);
    }

    @Test
    void stuckPastEdgeDoesNotOscillateWithoutMoving() {
        Enemy boss = new Enemy(900, 20, 10, 0, 100, null, 150, 150, 6);
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(boss);
        int firstX = boss.getXPosition();
        EnemyFormation.advance(enemies, true);
        EnemyFormation.advance(enemies, true);
        assertNotEquals(firstX, boss.getXPosition());
        assertTrue(boss.getXPosition() + 150 <= EnemyFormation.RIGHT_BOUND);
    }
}
