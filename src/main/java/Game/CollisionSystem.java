package Game;

import java.awt.Color;
import java.util.List;

/**
 * Movimiento y colisiones de proyectiles del jugador.
 */
public class CollisionSystem {

    public interface Listener {
        void onEnemyHit(int enemyIndex);

        void onShieldHit(int shieldIndex);

        void onBonusHit(int bonusIndex);
    }

    private CollisionSystem() {
    }

    /**
     * Avanza proyectiles y resuelve colisiones.
     *
     * @return {@code true} si el jugador puede volver a disparar (ningún proyectil activo)
     */
    public static boolean updatePlayerProjectiles(
            List<Projectile> projectiles,
            List<Enemy> enemies,
            List<Shield> shields,
            List<Ship> bonusEnemies,
            Listener listener) {

        boolean anyActive = false;

        for (Projectile projectile : projectiles) {
            if (projectile == null || !projectile.isActive()) {
                continue;
            }

            projectile.advance();
            if (!projectile.isActive()) {
                continue;
            }
            anyActive = true;

            boolean hit = false;
            for (int index = 0; index < enemies.size(); index++) {
                if (projectile.Colisionando(enemies.get(index))) {
                    projectile.deactivate();
                    if (listener != null) {
                        listener.onEnemyHit(index);
                    }
                    hit = true;
                    break;
                }
            }
            if (hit || !projectile.isActive()) {
                anyActive = anyActive && projectile.isActive();
                continue;
            }

            for (int index = 0; index < shields.size(); index++) {
                if (projectile.Colisionando(shields.get(index))) {
                    projectile.deactivate();
                    if (listener != null) {
                        listener.onShieldHit(index);
                    }
                    hit = true;
                    break;
                }
            }
            if (hit || !projectile.isActive()) {
                continue;
            }

            for (int index = 0; index < bonusEnemies.size(); index++) {
                if (projectile.Colisionando(bonusEnemies.get(index))) {
                    projectile.deactivate();
                    if (listener != null) {
                        listener.onBonusHit(index);
                    }
                    break;
                }
            }
        }

        for (Projectile projectile : projectiles) {
            if (projectile != null && projectile.isActive()) {
                return false;
            }
        }
        return true;
    }

    public static void degradeShield(List<Shield> shields, int index) {
        if (index < 0 || index >= shields.size()) {
            return;
        }
        Shield shield = shields.get(index);
        Color color = shield.getColor();
        if (color == Color.RED) {
            shield.setColor(Color.ORANGE);
        } else if (color == Color.ORANGE) {
            shield.setColor(Color.YELLOW);
        } else if (color == Color.YELLOW) {
            shield.setColor(Color.WHITE);
        } else if (color == Color.WHITE) {
            shields.remove(index);
        }
    }
}
