
package Game;

import Controlador.KeyboardController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

/**
 *
 * @author Spartan Tech
 */
public class Ship extends ControlledGameObject {
     
    ImageIcon ship1 = new ImageIcon(getClass().getResource("/Imagenes/Ship.png"));

    Image me = ship1.getImage();
    Image me2 = me.getScaledInstance(50, 55, Image.SCALE_SMOOTH);
    ImageIcon ship = new ImageIcon(me2);
    
    ImageIcon bonusEnemy = new ImageIcon(getClass().getResource("/Imagenes/bonusEnemySkin.gif"));
    ImageIcon lifeCounterShip = new ImageIcon(getClass().getResource("/Imagenes/shipSkinSmall.gif"));

    // Constructor for all ship objects
    public Ship(int xPosition, int yPosition, Color color, KeyboardController control) {
        super(xPosition, yPosition, color, control);
    }

    // Draw bonus enemy ship
    public void bonusDraw(Graphics g) {

        bonusEnemy.paintIcon(null, g, this.getXPosition(), this.getYPosition());
    }

    // Draw ships for life counter
    public void lifeDraw(Graphics g) {

        lifeCounterShip.paintIcon(null, g, this.getXPosition(), this.getYPosition());
    }

    // Draw player controlled ship
    @Override
    public void draw(Graphics g) {
        ship.paintIcon(null, g, this.getXPosition(), this.getYPosition());

    }

    // Gets the hit box for all ship objects
    @Override
    public Rectangle getBounds() {
        Rectangle shipHitbox = new Rectangle(this.getXPosition(), this.getYPosition(), 50, 50);
        return shipHitbox;
    }

    // Used to move all ship objects
    @Override
    public void move() {
        // Left arrow key press
        if (control.getKeyStatus(37)) {
            xPos -= 5;
        }
        // Right arrow key press
        if (control.getKeyStatus(39)) {
            xPos += 5;
        }
        
        // Move from edge to edge without stopping
        if (xPos > 800) {
            xPos = -50;
        }
        if (xPos < -50) {
            xPos = 800;
        }
    }
}
