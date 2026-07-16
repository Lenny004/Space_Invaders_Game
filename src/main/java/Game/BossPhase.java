package Game;

/**
 * Fase de combate del jefe según % de vida restante.
 */
public enum BossPhase {
    /** Disparo simple. */
    OPENING,
    /** Triple beam. */
    RAGE,
    /** Barrido / más proyectiles. */
    DESPERATE;

    public static BossPhase fromHealth(int health, int maxHealth) {
        if (maxHealth <= 0) {
            return DESPERATE;
        }
        double ratio = (double) health / maxHealth;
        if (ratio > 0.66) {
            return OPENING;
        }
        if (ratio > 0.33) {
            return RAGE;
        }
        return DESPERATE;
    }
}
