package Game;

import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Temporizadores del juego (tick principal + hitmarker).
 */
public class GameLoop {

    private final int framesPerSecond;
    private Timer gameTimer;
    private Timer hitMarkerTimer;

    public GameLoop(int framesPerSecond) {
        this.framesPerSecond = Math.max(1, framesPerSecond);
    }

    public void start(ActionListener onTick, ActionListener onHitMarkerReset) {
        stop();
        int delayMs = Math.max(1, 1000 / (framesPerSecond * 5)); // mantiene ~50fps con FPS=20 como el original (100/20)
        // El original usaba: new Timer(100 / Fotogramaporsegundo, ...) con FPS=20 → 5ms
        delayMs = Math.max(1, 100 / framesPerSecond);

        gameTimer = new Timer(delayMs, onTick);
        gameTimer.setRepeats(true);
        gameTimer.start();

        hitMarkerTimer = new Timer(100, onHitMarkerReset);
        hitMarkerTimer.setRepeats(true);
        hitMarkerTimer.start();
    }

    public void pause() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    public void resume() {
        if (gameTimer != null) {
            gameTimer.start();
        }
    }

    public void stop() {
        if (gameTimer != null) {
            gameTimer.stop();
            gameTimer = null;
        }
        if (hitMarkerTimer != null) {
            hitMarkerTimer.stop();
            hitMarkerTimer = null;
        }
    }

    public boolean isRunning() {
        return gameTimer != null && gameTimer.isRunning();
    }
}
