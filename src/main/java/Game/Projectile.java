package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Proyectil unificado del jugador (reemplaza Bullet…Bullet7).
 */
public class Projectile extends MovingGameObject {

    public static final int WIDTH = 5;
    public static final int HEIGHT = 20;
    public static final int SPEED = 15;

    private final int damage;
    private boolean active;

    public Projectile(int xPosition, int yPosition, Color color) {
        this(xPosition, yPosition, color, 1);
    }

    public Projectile(int xPosition, int yPosition, Color color, int damage) {
        super(xPosition, yPosition, 0, -SPEED, color);
        this.damage = Math.max(1, damage);
        this.active = color != null;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isActive() {
        return active && color != null;
    }

    public void deactivate() {
        this.active = false;
        this.color = null;
        setXPosition(0);
        setYPosition(0);
    }

    public void advance() {
        if (!isActive()) {
            return;
        }
        setYPosition(getYPosition() - SPEED);
        if (getYPosition() < 0) {
            deactivate();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (!isActive()) {
            return;
        }
        g.setColor(color);
        g.fillRect(getXPosition(), getYPosition(), WIDTH, HEIGHT);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(xPos, yPos, WIDTH, HEIGHT);
    }
}
