
package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @author Lenny
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
    private boolean zigzag;
    private int zigzagPhase;

    // Constructor para un enemigo
    public Enemy(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level) {
        super(xPosition, yPosition, xVelocity, yVelocity, color);
        this.enemytype = enemyType;
        this.width = width;
        this.height = height;
        this.level = level;
    }

    public void setZigzag(boolean zigzag) {
        this.zigzag = zigzag;
    }

    public boolean isZigzag() {
        return zigzag;
    }

    public boolean isBoss() {
        return enemytype == 100;
    }

    public int getLevel() {
        return level;
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
        if (this.enemytype == 100) {
            int skin = ((Math.max(1, level / 3) - 1) % 5) + 1;
            switch (skin) {
                case 1 -> alienBoss1.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                case 2 -> alienBoss2.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                case 3 -> alienBoss3.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                case 4 -> alienBoss4.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                default -> alienBoss5.paintIcon(null, g, this.getXPosition(), this.getYPosition());
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
        if (zigzag) {
            zigzagPhase++;
            yPos += (zigzagPhase % 20 < 10) ? 1 : -1;
            if (yPos < 10) {
                yPos = 10;
            }
        }
    }

}
