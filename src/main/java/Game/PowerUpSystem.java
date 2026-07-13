package Game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Disparo y velocidad de la nave (power-ups de combate).
 */
public class PowerUpSystem {

    private static final Color ORANGE = new Color(245, 80, 0);

    private static final int[][] OFFSETS = {
            {22},
            {22, 35},
            {22, 30, 14},
            {22, 30, 14, 38},
            {22, 30, 14, 38, 6},
            {22, 30, 14, 38, 6, 46},
            {22, 30, 14, 38, 6, 46, -2}
    };

    private static final Color[][] COLORS = {
            {ORANGE},
            {ORANGE, Color.BLUE},
            {Color.RED, Color.BLUE, Color.BLUE},
            {Color.RED, Color.BLUE, Color.BLUE, Color.CYAN},
            {Color.RED, Color.BLUE, Color.BLUE, Color.CYAN, Color.CYAN},
            {Color.RED, Color.BLUE, Color.BLUE, Color.CYAN, Color.CYAN, Color.GREEN},
            {Color.RED, Color.BLUE, Color.BLUE, Color.CYAN, Color.CYAN, Color.GREEN, Color.GREEN}
    };

    private PowerUpSystem() {
    }

    public static void applyPlayerSpeed(Ship player, int speedLevel) {
        if (player == null) {
            return;
        }
        switch (speedLevel) {
            case 1 -> player.move2();
            case 2 -> player.move3();
            case 3 -> player.move4();
            case 4 -> player.move5();
            case 5 -> player.move6();
            case 6 -> player.move7();
            default -> player.move();
        }
    }

    /**
     * Crea el abanico de proyectiles según el nivel de arma (0..6).
     */
    public static List<Projectile> createBurst(Ship player, int bulletLevel) {
        List<Projectile> burst = new ArrayList<>();
        if (player == null) {
            return burst;
        }
        int level = Math.min(Math.max(bulletLevel, 0), OFFSETS.length - 1);
        int[] offsets = OFFSETS[level];
        Color[] colors = COLORS[level];
        for (int i = 0; i < offsets.length; i++) {
            burst.add(new Projectile(
                    player.getXPosition() + offsets[i],
                    player.getYPosition() - 20,
                    colors[i]));
        }
        return burst;
    }
}
