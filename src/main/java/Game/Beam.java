package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Lenny, César, Miguel, Arístides
 */
public class Beam extends MovingGameObject {

    // Los enemigos disparan rayos
    // constructor para viga
    public Beam(int xPosition, int yPosition, int diameter, Color color) {
        //Llamando a la Clase Padre MovingGameObject
        super(xPosition, yPosition, 0, 0, color);
    }
    
    // Usada para dibujar una viga
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(this.getXPosition(), this.getYPosition(), 5, 20);
    }
    
    // Se usa para obtener el cuadro de impacto de una viga
    @Override
    public Rectangle getBounds() {
        Rectangle beamHitbox = new Rectangle(xPos, yPos, 5, 20);
        return beamHitbox;
    }
}
