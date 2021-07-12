
package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Spartan Tech
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

    
    // Constuctor for any enemy
    public Enemy(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level) {
        super(xPosition, yPosition, xVelocity, yVelocity, color);
        this.enemytype = enemyType;
        this.width = width;
        this.height = height;
        this.level = level;
    }
    
    @Override
    // Draws alien
    public void draw(Graphics g) {
        // Varient 1
        if (this.enemytype % 3 == 0) {
            alien1.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Varient 2
        } else if (this.enemytype % 3 == 1 && this.enemytype != 100) {
            alien2.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Varient 3
        } else if (this.enemytype % 3 == 2) {
            alien3.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Boss Enemy
        } if (this.enemytype == 100)
        {
            if(level == 3){
                alienBoss1.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
            else if(level == 6){
                alienBoss2.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
            else if(level == 9){
                alienBoss3.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
            else if(level == 12){
                alienBoss4.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
            else if(level == 15){
                alienBoss5.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
        }
    }

    // Gets the hitbox for normal eneimes
    @Override
    public Rectangle getBounds() {
        Rectangle enemyHitBox = new Rectangle(this.getXPosition(), this.getYPosition(), width, height);
        return enemyHitBox;
    }

    // Used to move all enemies
    @Override
    public void move() {
        xPos += xVel;
    }

}
