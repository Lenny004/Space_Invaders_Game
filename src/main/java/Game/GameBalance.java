package Game;

/**
 * Constantes de balance jugable (spawns, velocidades, puntuación).
 */
public final class GameBalance {

    private GameBalance() {
    }

    /** Probabilidad de disparo enemigo normal: 1/{@value}. */
    public static final int NORMAL_BEAM_CHANCE = 30;

    /** Probabilidad de disparo de jefe: 1/{@value}. */
    public static final int BOSS_BEAM_CHANCE = 5;

    /** Probabilidad de nave bonus: frame aleatorio == {@link #BONUS_SPAWN_HIT} de 0..{@value}-1. */
    public static final int BONUS_SPAWN_RANGE = 3000;
    public static final int BONUS_SPAWN_HIT = 1500;

    public static final int NORMAL_BEAM_SPEED = 4;
    public static final int BOSS_BEAM_SPEED = 5;
    public static final int ELEMENT_FALL_SPEED = 4;

    /** Probabilidad (%) de soltar un elemento al destruir un alien normal. */
    public static final int ELEMENT_DROP_CHANCE_PERCENT = 50;

    /** Si {@code true}, derrotar al jefe siempre suelta un elemento. */
    public static final boolean ELEMENT_DROP_ON_BOSS = true;

    public static final int SCORE_ALIEN = 100;
    public static final int SCORE_BOSS = 9000;
    public static final int SCORE_BONUS = 5000;

    /** Vida base del jefe (campaña nivel 6 / fallback). */
    public static final int BOSS_HEALTH = 40;

    /** @deprecated usar {@link #bossMaxHealth(int)} */
    public static final int BOSS_HEALTH_INITIAL = BOSS_HEALTH;

    /** @deprecated usar {@link #bossMaxHealth(int)} */
    public static final int BOSS_HEALTH_RESET = BOSS_HEALTH;

    public static final int EASY_ENEMY_SPEED = 2;
    public static final int MEDIUM_ENEMY_SPEED = 4;
    public static final int VICTORY_AFTER_LEVEL = 15;

    /** Ticks (~50 ms) sin kill antes de perder el combo. */
    public static final int COMBO_TIMEOUT_TICKS = 50;
    public static final int COMBO_MAX_MULTIPLIER = 5;

    /** Probabilidad (%) de soltar un buff temporal al matar un alien. */
    public static final int BUFF_DROP_CHANCE_PERCENT = 12;
    public static final int BUFF_DURATION_TICKS = 200;
    public static final int BUFF_FALL_SPEED = 3;

    public static int bossMaxHealth(int level) {
        return switch (level) {
            case 3 -> 30;
            case 6 -> 40;
            case 9 -> 55;
            case 12 -> 70;
            case 15 -> 85;
            default -> {
                int wave = Math.max(1, level / 3);
                yield Math.min(120, 30 + wave * 12);
            }
        };
    }

    /** Multiplicador de score por racha de combo (1..{@link #COMBO_MAX_MULTIPLIER}). */
    public static int comboMultiplier(int comboCount) {
        if (comboCount <= 0) {
            return 1;
        }
        return Math.min(COMBO_MAX_MULTIPLIER, 1 + (comboCount - 1) / 2);
    }
}
