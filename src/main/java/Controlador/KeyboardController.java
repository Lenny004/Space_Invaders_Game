
package Controlador;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Lenny
 */
public class KeyboardController implements KeyListener
{
    private boolean[] keyStatus; 
    private boolean valor;
    
    public KeyboardController()
    {
        keyStatus = new boolean[256]; 
    }
    
    public boolean getKeyStatus(int keyCode)
    {
        if(keyCode < 0 || keyCode > 255)
        {
            return false; 
        }
        else
        {
            return keyStatus[keyCode]; 
        }
    }
    
    public boolean getKeyStatus2(int VK_ESCAPE)
    {
        if(VK_ESCAPE < 0 || VK_ESCAPE > 255)
        {
            return false; 
        }
        else
        {
            return keyStatus[VK_ESCAPE]; 
        }
    }
        
    public void resetController()
    {
        //No existe codigo ASCII 256 así que dará false
        keyStatus = new boolean[256]; 
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        keyStatus[ke.getKeyCode()] = true; 
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        keyStatus[ke.getKeyCode()] = false; 
    }
}
