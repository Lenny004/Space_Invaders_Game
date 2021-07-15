
package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @authors Lenny, César, Arístides, Miguel
 */
public class Enemy extends MovingGameObject {

    ImageIcon alien1 = new ImageIcon(getClass().getResource("/Imagenes/alien1Skin.gif"));
    ImageIcon alien2 = new ImageIcon(getClass().getResource("/Imagenes/alien2Skin.gif"));
    ImageIcon alien3 = new ImageIcon(getClass().getResource("/Imagenes/alien3Skin.gif"));
    ImageIcon alienBoss1 = new ImageIcon(getClass().getResource("/Imagenes/boss1.gif"));
    ImageIcon alienBoss2 = new ImageIcon(getClass().getResource("/Imagenes/Boss2.gif"));
    ImageIcon alienBoss3 = new ImageIcon(getClass().getResource("/Imagenes/Boss3.gif"));
    ImageIcon alienBoss4 = new ImageIcon(getClass().getResource("/Imagenes/Boss4.gif"));
    ImageIcon alienBoss5 = new ImageIcon(getClass().getResource("/Imagenes/Boss5.gif"));

    private int enemytype, width, height, level;

    
    // Constructor para un enemigo
    public Enemy(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level) {
        super(xPosition, yPosition, xVelocity, yVelocity, color);
        this.enemytype = enemyType;
        this.width = width;
        this.height = height;
        this.level = level;
    }
    
    @Override
    // Dibuja alien
    public void draw(Graphics g) {
        // Variante 1
        if (this.enemytype % 3 == 0) {
            alien1.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Variante 2
        } else if (this.enemytype % 3 == 1 && this.enemytype != 100) {
            alien2.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Variante 3
        } else if (this.enemytype % 3 == 2) {
            alien3.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Enemigo Boss
        } 
        if (this.enemytype == 100){
            switch(level){
                case 3:
                    alienBoss1.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                    break;
                case 6:
                    alienBoss2.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                    break;
                case 9:
                    alienBoss3.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                    break;
                case 12:
                    alienBoss4.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                    break;
                case 15:
                    alienBoss5.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                    break;
            }
        }
    }

    // Obtiene el hitbox para enemigos normales.
    @Override
    public Rectangle getBounds() {
        Rectangle enemyHitBox = new Rectangle(this.getXPosition(), this.getYPosition(), width, height);
        return enemyHitBox;
    }

    // Se usa para mover a todos los enemigos.
    @Override
    public void move() {
        xPos += xVel;
    }

}
