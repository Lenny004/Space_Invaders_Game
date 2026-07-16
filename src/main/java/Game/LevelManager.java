package Game;

import Controlador.KeyboardController;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import persistence.RunEntry;

/**
 * Configuración de oleadas, jefes, nave, vidas y escudos.
 * Dificultad alineada con {@link RunEntry}: 1=fácil, 2=medio, 3=difícil.
 */
public class LevelManager {

    public enum Formation {
        GRID,
        V,
        STAGGERED
    }

    private final int tipoDificultad;

    public LevelManager(Configuracion dificultad) {
        this(dificultad != null ? dificultad.getTipoDificultad() : RunEntry.DIFFICULTY_HARD);
    }

    public LevelManager(int tipoDificultad) {
        this.tipoDificultad = RunEntry.normalizeDifficulty(tipoDificultad);
    }

    public int getTipoDificultad() {
        return tipoDificultad;
    }

    public boolean isBossLevel(int level) {
        return level % 3 == 0;
    }

    public boolean isVictory(int level) {
        return level > GameBalance.VICTORY_AFTER_LEVEL;
    }

    /** Victoria de campaña; en endless nunca aplica. */
    public boolean isVictory(int level, boolean endlessMode) {
        return !endlessMode && isVictory(level);
    }

    public Formation formationFor(int level) {
        if (isBossLevel(level)) {
            return Formation.GRID;
        }
        return switch (Math.floorMod(level, 5)) {
            case 2 -> Formation.V;
            case 4 -> Formation.STAGGERED;
            default -> Formation.GRID;
        };
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
        Formation formation = formationFor(level);
        return switch (formation) {
            case V -> createVWave(level);
            case STAGGERED -> createStaggeredWave(level);
            default -> createGridWave(level);
        };
    }

    private int baseXVelocity(int level) {
        return switch (tipoDificultad) {
            case RunEntry.DIFFICULTY_EASY -> GameBalance.EASY_ENEMY_SPEED;
            case RunEntry.DIFFICULTY_MEDIUM -> GameBalance.MEDIUM_ENEMY_SPEED;
            default -> Math.max(1, level);
        };
    }

    private List<Enemy> createGridWave(int level) {
        List<Enemy> enemies = new ArrayList<>();
        int xVel = baseXVelocity(level);
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 5; column++) {
                enemies.add(new Enemy(
                        120 + (row * 100), 20 + (column * 60),
                        xVel, 0, column, null, 40, 40, level));
            }
        }
        return enemies;
    }

    private List<Enemy> createVWave(int level) {
        List<Enemy> enemies = new ArrayList<>();
        int xVel = baseXVelocity(level);
        int[][] slots = {
                {500, 20},
                {420, 70}, {580, 70},
                {340, 120}, {500, 120}, {660, 120},
                {260, 170}, {420, 170}, {580, 170}, {740, 170},
                {180, 220}, {340, 220}, {500, 220}, {660, 220}, {820, 220},
                {260, 270}, {420, 270}, {580, 270}, {740, 270},
                {340, 320}, {500, 320}, {660, 320},
                {420, 370}, {580, 370},
                {500, 420}
        };
        for (int i = 0; i < slots.length; i++) {
            Enemy enemy = new Enemy(
                    slots[i][0], slots[i][1],
                    xVel, 0, i % 3, null, 40, 40, level);
            if (i % 7 == 0) {
                enemy.setZigzag(true);
            }
            enemies.add(enemy);
        }
        return enemies;
    }

    private List<Enemy> createStaggeredWave(int level) {
        List<Enemy> enemies = new ArrayList<>();
        int xVel = baseXVelocity(level);
        for (int column = 0; column < 5; column++) {
            for (int row = 0; row < 6; row++) {
                int offset = (column % 2 == 0) ? 0 : 40;
                Enemy enemy = new Enemy(
                        100 + (row * 110) + offset,
                        30 + (column * 55),
                        xVel, 0, column, null, 40, 40, level);
                if (row == 0 || row == 5) {
                    enemy.setZigzag(true);
                }
                enemies.add(enemy);
            }
        }
        return enemies;
    }

    /** Oleada mínima para el tutorial (pocos aliens lentos, sin jefe). */
    public List<Enemy> createTutorialWave() {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            enemies.add(new Enemy(
                    220 + (i * 140), 100,
                    GameBalance.EASY_ENEMY_SPEED, 0, 0, null, 40, 40, 1));
        }
        return enemies;
    }

    /** Dos minions que acompañan la fase desesperada del jefe. */
    public List<Enemy> createBossMinions(int level) {
        List<Enemy> minions = new ArrayList<>();
        int xVel = Math.max(1, baseXVelocity(level) / 2);
        minions.add(new Enemy(80, 80, xVel, 0, 0, null, 40, 40, level));
        minions.add(new Enemy(820, 80, xVel, 0, 1, null, 40, 40, level));
        return minions;
    }

    public Enemy createBoss(int level) {
        int wave = Math.max(1, level / 3);
        int xVel = switch (tipoDificultad) {
            case RunEntry.DIFFICULTY_EASY -> Math.max(1, wave);
            case RunEntry.DIFFICULTY_MEDIUM -> 2 * wave;
            default -> 3 * wave;
        };
        return new Enemy(120, 20, xVel, 0, 100, null, 150, 150, level);
    }
}
