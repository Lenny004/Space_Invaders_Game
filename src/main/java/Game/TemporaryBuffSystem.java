package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Buffs temporales de partida (no reemplazan el crafteo permanente).
 */
public final class TemporaryBuffSystem {

    public static final int DESPAWN_Y = 620;

    private static final BuffDrop.Type[] COMBAT_BUFFS = {
            BuffDrop.Type.SHIELD,
            BuffDrop.Type.SLOW,
            BuffDrop.Type.DOUBLE_FIRE
    };

    private int shieldTicks;
    private int slowTicks;
    private int doubleFireTicks;
    private int pendingLives;

    public void reset() {
        shieldTicks = 0;
        slowTicks = 0;
        doubleFireTicks = 0;
        pendingLives = 0;
    }

    public void tick() {
        if (shieldTicks > 0) {
            shieldTicks--;
        }
        if (slowTicks > 0) {
            slowTicks--;
        }
        if (doubleFireTicks > 0) {
            doubleFireTicks--;
        }
    }

    public void apply(BuffDrop.Type type) {
        if (type == null) {
            return;
        }
        int duration = GameBalance.BUFF_DURATION_TICKS;
        switch (type) {
            case SHIELD -> shieldTicks = duration;
            case SLOW -> slowTicks = duration;
            case DOUBLE_FIRE -> doubleFireTicks = duration;
            case LIFE -> pendingLives++;
        }
    }

    /** Consume el escudo temporal al recibir un golpe. */
    public boolean tryAbsorbHit() {
        if (shieldTicks <= 0) {
            return false;
        }
        shieldTicks = 0;
        return true;
    }

    public boolean hasShield() {
        return shieldTicks > 0;
    }

    public boolean hasSlow() {
        return slowTicks > 0;
    }

    public boolean hasDoubleFire() {
        return doubleFireTicks > 0;
    }

    public int getShieldTicks() {
        return shieldTicks;
    }

    public int getSlowTicks() {
        return slowTicks;
    }

    public int getDoubleFireTicks() {
        return doubleFireTicks;
    }

    /** Nivel de balas efectivo: crafteo + buff de doble disparo. */
    public int effectiveBulletLevel(int craftedLevel) {
        int level = Math.max(0, craftedLevel);
        if (hasDoubleFire()) {
            level = Math.max(level, 1) + 1;
        }
        return Math.min(level, 6);
    }

    public static boolean shouldDrop(Random rng) {
        if (rng == null) {
            return false;
        }
        return rng.nextInt(100) < GameBalance.BUFF_DROP_CHANCE_PERCENT;
    }

    public static boolean shouldDropLife(Random rng) {
        return shouldDropLife(rng, GameBalance.LIFE_DROP_CHANCE_PERCENT);
    }

    public static boolean shouldDropLifeOnBoss(Random rng) {
        return shouldDropLife(rng, GameBalance.LIFE_DROP_CHANCE_BOSS_PERCENT);
    }

    public static boolean shouldDropLifeOnBonus(Random rng) {
        return shouldDropLife(rng, GameBalance.LIFE_DROP_CHANCE_BONUS_PERCENT);
    }

    public static boolean shouldDropLife(Random rng, int chancePercent) {
        if (rng == null) {
            return false;
        }
        return rng.nextInt(100) < Math.max(0, chancePercent);
    }

    /**
     * No spawnea si el jugador ya está en {@link GameBalance#MAX_LIVES}.
     * La exclusión del tutorial es responsabilidad de {@code GamePanel}.
     */
    public static boolean shouldSpawnLife(Random rng, int currentLives, int chancePercent) {
        if (currentLives >= GameBalance.MAX_LIVES) {
            return false;
        }
        return shouldDropLife(rng, chancePercent);
    }

    public static BuffDrop.Type rollType(Random rng) {
        if (rng == null) {
            return COMBAT_BUFFS[0];
        }
        return COMBAT_BUFFS[rng.nextInt(COMBAT_BUFFS.length)];
    }

    public int consumePendingLives() {
        int granted = pendingLives;
        pendingLives = 0;
        return granted;
    }

    /**
     * Avanza buffs cayendo; al recoger aplica el buff.
     *
     * @return etiqueta corta del último pickup, o {@code null}
     */
    public String updateDrops(List<BuffDrop> drops, Ship ship) {
        if (drops == null || drops.isEmpty()) {
            return null;
        }
        String feedback = null;
        for (int i = drops.size() - 1; i >= 0; i--) {
            BuffDrop drop = drops.get(i);
            if (drop == null) {
                drops.remove(i);
                continue;
            }
            drop.move();
            if (ship != null && drop.Colisionando(ship)) {
                apply(drop.getType());
                feedback = drop.getType() == BuffDrop.Type.LIFE
                        ? Messages.get("buff.life")
                        : "+" + drop.getType().getLabel();
                drops.remove(i);
            } else if (drop.getYPosition() > DESPAWN_Y) {
                drops.remove(i);
            }
        }
        return feedback;
    }

    public List<String> activeLabels() {
        List<String> labels = new ArrayList<>();
        if (hasShield()) {
            labels.add("S");
        }
        if (hasSlow()) {
            labels.add("Z");
        }
        if (hasDoubleFire()) {
            labels.add("D");
        }
        return labels;
    }
}
