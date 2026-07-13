package Game;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpSystemTest {

    @Test
    void createBurstScalesWithBulletLevel() {
        Ship ship = new Ship(100, 200, Color.YELLOW, null);
        assertEquals(1, PowerUpSystem.createBurst(ship, 0).size());
        assertEquals(2, PowerUpSystem.createBurst(ship, 1).size());
        assertEquals(7, PowerUpSystem.createBurst(ship, 6).size());
        assertEquals(7, PowerUpSystem.createBurst(ship, 99).size());
    }

    @Test
    void projectilesSpawnAboveShip() {
        Ship ship = new Ship(100, 200, Color.YELLOW, null);
        List<Projectile> burst = PowerUpSystem.createBurst(ship, 0);
        assertEquals(1, burst.size());
        assertEquals(122, burst.get(0).getXPosition()); // 100 + 22
        assertEquals(180, burst.get(0).getYPosition()); // 200 - 20
        assertTrue(burst.get(0).isActive());
    }
}

class CollisionSystemTest {

    @Test
    void deactivateAllowsRefire() {
        List<Projectile> projectiles = new ArrayList<>();
        Projectile p = new Projectile(10, 5, Color.RED);
        projectiles.add(p);

        boolean canFire = CollisionSystem.updatePlayerProjectiles(
                projectiles,
                List.of(),
                List.of(),
                List.of(),
                null);

        // Tras varios advances sale de pantalla
        for (int i = 0; i < 20 && p.isActive(); i++) {
            canFire = CollisionSystem.updatePlayerProjectiles(
                    projectiles, List.of(), List.of(), List.of(), null);
        }
        assertTrue(canFire);
        assertFalse(p.isActive());
    }

    @Test
    void degradeShieldCyclesColors() {
        List<Shield> shields = new ArrayList<>();
        shields.add(new Shield(0, 0, 150, 10, Color.RED));
        CollisionSystem.degradeShield(shields, 0);
        assertEquals(Color.ORANGE, shields.get(0).getColor());
        CollisionSystem.degradeShield(shields, 0);
        assertEquals(Color.YELLOW, shields.get(0).getColor());
        CollisionSystem.degradeShield(shields, 0);
        assertEquals(Color.WHITE, shields.get(0).getColor());
        CollisionSystem.degradeShield(shields, 0);
        assertTrue(shields.isEmpty());
    }

    @Test
    void projectileHitsEnemyAndNotifiesListener() {
        List<Projectile> projectiles = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        Projectile p = new Projectile(100, 40, Color.RED);
        enemies.add(new Enemy(100, 20, 0, 0, 0, null, 40, 40, 1));
        projectiles.add(p);

        int[] hitIndex = {-1};
        CollisionSystem.Listener listener = new CollisionSystem.Listener() {
            @Override
            public void onEnemyHit(int enemyIndex) {
                hitIndex[0] = enemyIndex;
            }

            @Override
            public void onShieldHit(int shieldIndex) {
            }

            @Override
            public void onBonusHit(int bonusIndex) {
            }
        };

        boolean canFire = CollisionSystem.updatePlayerProjectiles(
                projectiles, enemies, List.of(), List.of(), listener);

        assertEquals(0, hitIndex[0]);
        assertFalse(p.isActive());
        assertTrue(canFire);
    }

    @Test
    void projectileHitsShieldBeforeBonus() {
        List<Projectile> projectiles = new ArrayList<>();
        List<Shield> shields = new ArrayList<>();
        Projectile p = new Projectile(100, 40, Color.CYAN);
        shields.add(new Shield(100, 20, 150, 10, Color.RED));
        projectiles.add(p);

        int[] shieldHit = {-1};
        int[] bonusHit = {-1};
        CollisionSystem.updatePlayerProjectiles(
                projectiles,
                List.of(),
                shields,
                List.of(),
                new CollisionSystem.Listener() {
                    @Override
                    public void onEnemyHit(int enemyIndex) {
                    }

                    @Override
                    public void onShieldHit(int shieldIndex) {
                        shieldHit[0] = shieldIndex;
                    }

                    @Override
                    public void onBonusHit(int bonusIndex) {
                        bonusHit[0] = bonusIndex;
                    }
                });

        assertEquals(0, shieldHit[0]);
        assertEquals(-1, bonusHit[0]);
        assertFalse(p.isActive());
    }
}

class ProjectileTest {

    @Test
    void damageDefaultsToOneAndNeverBelowOne() {
        assertEquals(1, new Projectile(0, 0, Color.RED).getDamage());
        assertEquals(1, new Projectile(0, 0, Color.RED, 0).getDamage());
        assertEquals(3, new Projectile(0, 0, Color.RED, 3).getDamage());
    }

    @Test
    void advanceMovesUpAndDeactivatesOffScreen() {
        Projectile p = new Projectile(10, 10, Color.ORANGE);
        p.advance();
        assertFalse(p.isActive());
    }
}
