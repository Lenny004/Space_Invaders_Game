package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Pickup que cae tras un kill (escudo / slow / doble disparo / vida extra).
 */
public class BuffDrop extends MovingGameObject {

    public static final int SIZE = 28;

    public enum Type {
        SHIELD(new Color(80, 200, 255), "S"),
        SLOW(new Color(180, 120, 255), "Z"),
        DOUBLE_FIRE(new Color(255, 180, 40), "D"),
        LIFE(new Color(255, 70, 90), "+1");

        private final Color color;
        private final String label;

        Type(Color color, String label) {
            this.color = color;
            this.label = label;
        }

        public Color getColor() {
            return color;
        }

        public String getLabel() {
            return label;
        }
    }

    private final Type type;

    public BuffDrop(int x, int y, Type type) {
        super(x, y, 0, GameBalance.BUFF_FALL_SPEED, type != null ? type.getColor() : Color.WHITE);
        this.type = type != null ? type : Type.SHIELD;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void draw(Graphics g) {
        if (type == Type.LIFE) {
            g.setColor(type.getColor());
            g.fillOval(getXPosition(), getYPosition(), SIZE, SIZE);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString(type.getLabel(), getXPosition() + 5, getYPosition() + 19);
            return;
        }
        g.setColor(type.getColor());
        g.fillRoundRect(getXPosition(), getYPosition(), SIZE, SIZE, 8, 8);
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString(type.getLabel(), getXPosition() + 8, getYPosition() + 19);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(getXPosition(), getYPosition(), SIZE, SIZE);
    }

    @Override
    public void move() {
        setYPosition(getYPosition() + GameBalance.BUFF_FALL_SPEED);
    }
}
