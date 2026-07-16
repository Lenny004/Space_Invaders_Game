package Game;

/**
 * Rachas de kills: multiplica el score de aliens hasta un tope.
 * Se reinicia al recibir daño o al expirar el temporizador.
 */
public final class ComboSystem {

    private int comboCount;
    private int timerTicks;

    public void reset() {
        comboCount = 0;
        timerTicks = 0;
    }

    /** Registra un kill de alien y devuelve el multiplicador aplicado (1..max). */
    public int registerKill() {
        comboCount++;
        timerTicks = GameBalance.COMBO_TIMEOUT_TICKS;
        return getMultiplier();
    }

    public void onPlayerHit() {
        reset();
    }

    public void tick() {
        if (comboCount <= 0) {
            return;
        }
        timerTicks--;
        if (timerTicks <= 0) {
            reset();
        }
    }

    public int getComboCount() {
        return comboCount;
    }

    public int getMultiplier() {
        return GameBalance.comboMultiplier(comboCount);
    }

    public boolean isActive() {
        return comboCount > 0;
    }

    public int scoreForAlien() {
        return GameBalance.SCORE_ALIEN * getMultiplier();
    }
}
