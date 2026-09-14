package Game;

import java.awt.Rectangle;
import java.util.List;

/**
 * Movimiento de la oleada: rebote por hitbox real (no por índice de lista)
 * y clamp para que nadie —sobre todo el jefe— se escape de la pantalla.
 */
public final class EnemyFormation {

    public static final int LEFT_BOUND = 50;
    public static final int RIGHT_BOUND = 1000;
    public static final int NORMAL_DESCENT = 10;
    public static final int BOSS_DESCENT = 4;
    public static final int BOSS_MAX_Y = 220;
    public static final int BOSS_MIN_Y = 16;

    private EnemyFormation() {
    }

    public static void advance(List<Enemy> enemies, boolean bossLevel) {
        if (enemies == null || enemies.isEmpty()) {
            return;
        }
        if (wouldLeaveArena(enemies)) {
            reverseHorizontal(enemies);
            descend(enemies, bossLevel ? BOSS_DESCENT : NORMAL_DESCENT, bossLevel);
        }
        for (Enemy enemy : enemies) {
            enemy.move();
            clampHorizontal(enemy);
            if (bossLevel && enemy.isBoss()) {
                clampBossVertical(enemy);
            }
        }
    }

    static boolean wouldLeaveArena(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            Rectangle box = enemy.getBounds();
            int vel = enemy.getXVelocity();
            int nextX = box.x + vel;
            int nextRight = nextX + box.width;
            // Solo rebota si sigue saliéndose; si ya está fuera e intenta volver, el clamp lo mete.
            if (vel < 0 && nextX < LEFT_BOUND) {
                return true;
            }
            if (vel > 0 && nextRight > RIGHT_BOUND) {
                return true;
            }
        }
        return false;
    }

    static void reverseHorizontal(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            enemy.setXVelocity(-enemy.getXVelocity());
        }
    }

    static void descend(List<Enemy> enemies, int amount, boolean bossLevel) {
        for (Enemy enemy : enemies) {
            if (bossLevel && enemy.isBoss()) {
                int nextY = Math.min(BOSS_MAX_Y, enemy.getYPosition() + amount);
                enemy.setYPosition(nextY);
            } else {
                enemy.setYPosition(enemy.getYPosition() + amount);
            }
        }
    }

    static void clampHorizontal(Enemy enemy) {
        Rectangle box = enemy.getBounds();
        int maxX = Math.max(LEFT_BOUND, RIGHT_BOUND - Math.max(1, box.width));
        int x = enemy.getXPosition();
        if (x < LEFT_BOUND) {
            enemy.setXPosition(LEFT_BOUND);
        } else if (x > maxX) {
            enemy.setXPosition(maxX);
        }
    }

    static void clampBossVertical(Enemy enemy) {
        int y = enemy.getYPosition();
        if (y < BOSS_MIN_Y) {
            enemy.setYPosition(BOSS_MIN_Y);
        } else if (y > BOSS_MAX_Y) {
            enemy.setYPosition(BOSS_MAX_Y);
        }
    }
}
