
package Game;

import java.awt.Color;

/**
 *
 * @author Lenny, Cesar, Arístides, Miguel
 */
public abstract class MovingGameObject extends GameObject implements Moveable{
    
    int xVel;
    int yVel;
    
    // Constructor para cualquier objeto no controlable
    // Super hace referencia al Constructor padre GameObject
    public MovingGameObject(int xPosition, int yPosition, int xVelocity, int yVelocity, Color color)
    {
        super(xPosition, yPosition, color);
        this.xVel = xVelocity;
        this.yVel = yVelocity;
    
    }
    
    // Accesores y mutadores para cada parte del constructor MovingGameObject
    public int getXVelocity()
    {
        return xVel;
    }
    public int getYVelocity()
    {
        return yVel;
    }
    public void setXVelocity(int xVelocity)
    {
        this.xVel = xVelocity;
    }
    public void setYVelocity(int yVelocity)
    {
        this.yVel = yVelocity;
    }
    @Override
    // Se usa para mover objetos no controlables
    public void move()
    {
        this.xPos += xVel;
        this.yPos += yVel;
    }
}
