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

    public static final int SCORE_ALIEN = 100;
    public static final int SCORE_BOSS = 9000;
    public static final int SCORE_BONUS = 5000;

    /** Vida del jefe al entrar en un nivel boss (siempre la misma). */
    public static final int BOSS_HEALTH = 40;

    /** @deprecated usar {@link #BOSS_HEALTH} */
    public static final int BOSS_HEALTH_INITIAL = BOSS_HEALTH;

    /** @deprecated usar {@link #BOSS_HEALTH} */
    public static final int BOSS_HEALTH_RESET = BOSS_HEALTH;

    public static final int EASY_ENEMY_SPEED = 2;
    public static final int MEDIUM_ENEMY_SPEED = 4;
    public static final int VICTORY_AFTER_LEVEL = 15;
}
