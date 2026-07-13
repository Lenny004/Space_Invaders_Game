
package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @author Lenny, César, Miguel, Arístides
 */
public class Bullet7 extends MovingGameObject {

    // ¡Mi nave de jugador dispara balas!
    int diameter;
    int yVelocity;

    
    // Constructor para las balas
    public Bullet7(int xPosition, int yPosition, int diameter, Color color) {
        super(xPosition, yPosition, 0, 0, color);
        this.diameter = diameter;
    }

    // Obtiene el diámetro de la bala
    public int getDiameter() {
        return diameter;
    }

    // Usada para dibujar la bala
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(this.getXPosition(), this.getYPosition(), 5, 20);
    }

    @Override
    public Rectangle getBounds() {
        Rectangle bulletHitbox = new Rectangle(xPos, yPos, 5, 20);
        return bulletHitbox;
    }
}

