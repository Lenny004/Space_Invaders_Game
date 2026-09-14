package Game;

import persistence.RunEntry;

/**
 * Constantes de balance jugable (spawns, velocidades, puntuación).
 */
public final class GameBalance {

    private GameBalance() {
    }

    /** Probabilidad de disparo enemigo normal en dificultad media: 1/{@value}. */
    public static final int NORMAL_BEAM_CHANCE = 30;

    /** Probabilidad de disparo de jefe en dificultad media: 1/{@value}. */
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
    /** Velocidad base de difíciles: siempre mayor que medio desde el nivel 1. */
    public static final int HARD_ENEMY_SPEED = 5;
    public static final int ENEMY_MAX_SPEED = 8;
    /** Tope de velocidad horizontal del jefe para que no se salga de la arena. */
    public static final int BOSS_MAX_SPEED = 8;
    public static final int BOSS_MAX_SPEED_EASY = 4;
    public static final int BOSS_MAX_SPEED_MEDIUM = 6;
    public static final int LIVES_EASY = 5;
    public static final int LIVES_MEDIUM = 3;
    public static final int LIVES_HARD = 2;
    public static final int VICTORY_AFTER_LEVEL = 15;

    /** Ticks (~50 ms) sin kill antes de perder el combo. */
    public static final int COMBO_TIMEOUT_TICKS = 50;
    public static final int COMBO_MAX_MULTIPLIER = 5;

    /** Probabilidad (%) de soltar un buff temporal al matar un alien. */
    public static final int BUFF_DROP_CHANCE_PERCENT = 12;
    public static final int BUFF_DURATION_TICKS = 200;
    public static final int BUFF_FALL_SPEED = 3;

    /** Probabilidad (%) de vida extra al matar un alien normal o un minion de jefe. */
    public static final int LIFE_DROP_CHANCE_PERCENT = 2;
    /**
     * Probabilidad (%) de vida extra al derrotar al jefe.
     * Un poco más alta que el alien: el jefe es un hito, no un kill rutinario.
     */
    public static final int LIFE_DROP_CHANCE_BOSS_PERCENT = 8;
    /**
     * Probabilidad (%) de vida extra al destruir la nave bonus / meteorito.
     * Entre alien normal y jefe: el spawn ya es raro.
     */
    public static final int LIFE_DROP_CHANCE_BONUS_PERCENT = 5;
    /** Tope de iconos de vida para que no se solapen con el HUD. */
    public static final int MAX_LIVES = 8;

    public static int bossMaxHealth(int level) {
        return bossMaxHealth(level, RunEntry.DIFFICULTY_MEDIUM);
    }

    public static int bossMaxHealth(int level, int difficulty) {
        int base = switch (level) {
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
        return switch (RunEntry.normalizeDifficulty(difficulty)) {
            case RunEntry.DIFFICULTY_EASY -> Math.max(16, base * 3 / 4);
            case RunEntry.DIFFICULTY_HARD -> Math.max(base + 8, base * 5 / 4);
            default -> base;
        };
    }

    public static int enemySpeed(int difficulty, int level) {
        int lvl = Math.max(1, level);
        return switch (RunEntry.normalizeDifficulty(difficulty)) {
            case RunEntry.DIFFICULTY_EASY -> EASY_ENEMY_SPEED;
            case RunEntry.DIFFICULTY_MEDIUM -> MEDIUM_ENEMY_SPEED;
            default -> Math.min(ENEMY_MAX_SPEED, HARD_ENEMY_SPEED + (lvl - 1) / 2);
        };
    }

    public static int bossSpeed(int difficulty, int level) {
        int wave = Math.max(1, level / 3);
        return switch (RunEntry.normalizeDifficulty(difficulty)) {
            case RunEntry.DIFFICULTY_EASY ->
                    Math.min(BOSS_MAX_SPEED_EASY, Math.max(1, wave));
            case RunEntry.DIFFICULTY_MEDIUM ->
                    Math.min(BOSS_MAX_SPEED_MEDIUM, Math.max(2, 2 * wave));
            default ->
                    Math.min(BOSS_MAX_SPEED, Math.max(4, 3 * wave));
        };
    }

    public static int startingLives(int difficulty) {
        return switch (RunEntry.normalizeDifficulty(difficulty)) {
            case RunEntry.DIFFICULTY_EASY -> LIVES_EASY;
            case RunEntry.DIFFICULTY_HARD -> LIVES_HARD;
            default -> LIVES_MEDIUM;
        };
    }

    /** Valor más alto = disparos menos frecuentes. */
    public static int normalBeamChance(int difficulty) {
        return switch (RunEntry.normalizeDifficulty(difficulty)) {
            case RunEntry.DIFFICULTY_EASY -> 42;
            case RunEntry.DIFFICULTY_HARD -> 16;
            default -> NORMAL_BEAM_CHANCE;
        };
    }

    public static int bossBeamChance(int difficulty) {
        return switch (RunEntry.normalizeDifficulty(difficulty)) {
            case RunEntry.DIFFICULTY_EASY -> 8;
            case RunEntry.DIFFICULTY_HARD -> 3;
            default -> BOSS_BEAM_CHANCE;
        };
    }

    public static String difficultyMessageKey(int difficulty) {
        return switch (RunEntry.normalizeDifficulty(difficulty)) {
            case RunEntry.DIFFICULTY_MEDIUM -> "diff.medium";
            case RunEntry.DIFFICULTY_HARD -> "diff.hard";
            default -> "diff.easy";
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
