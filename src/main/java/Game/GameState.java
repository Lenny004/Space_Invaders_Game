package Game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estado mutable compartido del combate. Evita estáticos dispersos y da contexto a los sistemas.
 */
public class GameState {

    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 675;

    private final List<Projectile> projectiles = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Shield> shields = new ArrayList<>();
    private final List<Beam> beams = new ArrayList<>();
    private final List<Ship> lives = new ArrayList<>();
    private final List<Ship> bonusEnemies = new ArrayList<>();
    private final List<ElementoDrop> elementDrops = new ArrayList<>();

    private Ship player;
    private int score;
    private int level = 1;
    private int livesCount = 3; // valor de arranque; la partida usa GameBalance.startingLives
    private int highScore;
    private int bossHealth = 40;
    private int bulletLevel; // CantidadBalas 0..6
    private int speedLevel;  // Velocidad 0..6
    private boolean canFire = true;
    private boolean canEnemyFire = true;
    private boolean canSpawnBonus = true;
    private boolean hitMarker;
    private int markerX;
    private int markerY;
    private final int[] elementCounts = new int[12];

    // Flags de crafteo (antes ValorO, ValorZ, ...)
    private final boolean[] craftUsed = new boolean[12];

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Shield> getShields() {
        return shields;
    }

    public List<Beam> getBeams() {
        return beams;
    }

    public List<Ship> getLives() {
        return lives;
    }

    public List<Ship> getBonusEnemies() {
        return bonusEnemies;
    }

    public List<ElementoDrop> getElementDrops() {
        return elementDrops;
    }

    public Ship getPlayer() {
        return player;
    }

    public void setPlayer(Ship player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void nextLevel() {
        this.level += 1;
    }

    public int getLivesCount() {
        return livesCount;
    }

    public void setLivesCount(int livesCount) {
        this.livesCount = livesCount;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getBossHealth() {
        return bossHealth;
    }

    public void setBossHealth(int bossHealth) {
        this.bossHealth = bossHealth;
    }

    public void damageBoss(int amount) {
        this.bossHealth -= amount;
    }

    public int getBulletLevel() {
        return bulletLevel;
    }

    public void setBulletLevel(int bulletLevel) {
        this.bulletLevel = Math.max(0, Math.min(6, bulletLevel));
    }

    public void raiseBulletLevel(int minimum) {
        this.bulletLevel = Math.max(this.bulletLevel, Math.min(6, minimum));
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int speedLevel) {
        this.speedLevel = Math.max(0, Math.min(6, speedLevel));
    }

    public void raiseSpeedLevel(int minimum) {
        this.speedLevel = Math.max(this.speedLevel, Math.min(6, minimum));
    }

    public boolean canFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public boolean canEnemyFire() {
        return canEnemyFire;
    }

    public void setCanEnemyFire(boolean canEnemyFire) {
        this.canEnemyFire = canEnemyFire;
    }

    public boolean canSpawnBonus() {
        return canSpawnBonus;
    }

    public void setCanSpawnBonus(boolean canSpawnBonus) {
        this.canSpawnBonus = canSpawnBonus;
    }

    public boolean isHitMarker() {
        return hitMarker;
    }

    public void setHitMarker(boolean hitMarker) {
        this.hitMarker = hitMarker;
    }

    public int getMarkerX() {
        return markerX;
    }

    public int getMarkerY() {
        return markerY;
    }

    public void setHitMarkerAt(int x, int y) {
        this.markerX = x;
        this.markerY = y;
        this.hitMarker = true;
    }

    public int[] getElementCounts() {
        return elementCounts;
    }

    public int getElementCount(int index) {
        return elementCounts[index];
    }

    public void addElement(int index, int amount) {
        if (index >= 0 && index < elementCounts.length) {
            elementCounts[index] += amount;
        }
    }

    public boolean isCraftUsed(int index) {
        return craftUsed[index];
    }

    public void setCraftUsed(int index, boolean used) {
        if (index >= 0 && index < craftUsed.length) {
            craftUsed[index] = used;
        }
    }

    public void clearCombatLists() {
        projectiles.clear();
        enemies.clear();
        shields.clear();
        beams.clear();
        lives.clear();
        bonusEnemies.clear();
        elementDrops.clear();
    }

    public void resetForNewGame() {
        clearCombatLists();
        score = 0;
        level = 1;
        bossHealth = GameBalance.BOSS_HEALTH;
        livesCount = 3;
        bulletLevel = 0;
        speedLevel = 0;
        canFire = true;
        canEnemyFire = true;
        canSpawnBonus = true;
        hitMarker = false;
        for (int i = 0; i < elementCounts.length; i++) {
            elementCounts[i] = 0;
        }
        for (int i = 0; i < craftUsed.length; i++) {
            craftUsed[i] = false;
        }
    }

    public List<Projectile> activeProjectiles() {
        List<Projectile> active = new ArrayList<>();
        for (Projectile p : projectiles) {
            if (p != null && p.isActive()) {
                active.add(p);
            }
        }
        return Collections.unmodifiableList(active);
    }
}
