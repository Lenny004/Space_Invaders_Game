package Game;

import Controlador.KeyboardController;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuración de oleadas, jefes, nave, vidas y escudos.
 */
public class LevelManager {

    private final Configuracion dificultad;

    public LevelManager(Configuracion dificultad) {
        this.dificultad = dificultad;
    }

    public boolean isBossLevel(int level) {
        return level % 3 == 0;
    }

    public boolean isVictory(int level) {
        return level > 15;
    }

    public List<Enemy> createEnemies(int level) {
        List<Enemy> enemies = new ArrayList<>();
        if (isBossLevel(level)) {
            enemies.add(createBoss(level));
        } else {
            enemies.addAll(createNormalWave(level));
        }
        return enemies;
    }

    public Ship createPlayer(KeyboardController controller) {
        return new Ship(500, 600, null, controller);
    }

    public List<Ship> createLifeIcons(int lives) {
        List<Ship> icons = new ArrayList<>();
        for (int column = 0; column < lives; column++) {
            icons.add(new Ship(48 + (column * 20), 10, Color.WHITE, null));
        }
        return icons;
    }

    public List<Shield> createShields() {
        List<Shield> shields = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                shields.add(new Shield(100 + (column * 333), 500 - (row * 10), 150, 10, Color.RED));
            }
        }
        return shields;
    }

    private List<Enemy> createNormalWave(int level) {
        List<Enemy> enemies = new ArrayList<>();
        int tipo = dificultad.getTipoDificultad();
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 5; column++) {
                int xVel = switch (tipo) {
                    case 2 -> 4;
                    case 3 -> 2;
                    default -> 1 * level;
                };
                enemies.add(new Enemy(
                        120 + (row * 100), 20 + (column * 60),
                        xVel, 0, column, null, 40, 40, level));
            }
        }
        return enemies;
    }

    private Enemy createBoss(int level) {
        int tipo = dificultad.getTipoDificultad();
        int xVel = switch (tipo) {
            case 2 -> 2 * (level / 3);
            case 3 -> Math.max(1, level / 3);
            default -> 3 * (level / 3);
        };
        return new Enemy(120, 20, xVel, 0, 100, null, 150, 150, level);
    }
}
