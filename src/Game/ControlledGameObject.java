package Game;

import Controlador.KeyboardController;
import java.awt.Color;

/**
 *
 * @author Lenny, César, Miguel, Arístides
 */
public abstract class ControlledGameObject extends GameObject implements Moveable {
    
    KeyboardController control;
    
    // Constructor para cualquier objeto controlable
    public ControlledGameObject(int xPosition, int yPosition, Color color, KeyboardController control)
    {
        super(xPosition, yPosition, color);
        this.control = control;
    }
}
