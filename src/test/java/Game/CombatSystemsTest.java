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
}
