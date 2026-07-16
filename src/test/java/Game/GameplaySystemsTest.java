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
}
