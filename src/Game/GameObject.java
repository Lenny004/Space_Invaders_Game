package Game;

import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author Lenny, César, Miguel, Arístides
 */
public abstract class GameObject implements Drawable {

    int xPos;
    int yPos;
    Color color;
    boolean estaColisionando ;
    
    public GameObject(){};
    
    // Constructor para cualquier Gameobject
    public GameObject(int xPosition, int yPosition, Color color) {
        this.xPos = xPosition;
        this.yPos = yPosition;
        this.color = color;
    }

    public abstract Rectangle getBounds();

    // Obtiene la posición X de cualquier objeto
    public int getXPosition() {
        return xPos;
    }

    // Obtiene la posición Y de cualquier objeto
    public int getYPosition() {
        return yPos;
    }

    // Obtiene el color de cualquier objeto
    public Color getColor() {
        return color;
    }

    // Establece la posición X de cualquier objeto
    public void setXPosition(int xPosition) {
        this.xPos = xPosition;
    }

    // Establece la posición Y de cualquier objeto
    public void setYPosition(int yPosition) {
        this.yPos = yPosition;
    }

    // Establece el color de cualquier objeto
    public void setColor(Color color) {
        this.color = color;
    }

    // Comprueba si los hitboxes de dos objetos cualesquiera se cruzan 
    public boolean Colisionando(GameObject Otro) {
        //".intersects" Es un método que sirve para detectar si la imagen recibe una colisión usando los puntos de localización
        //Retorna true si detecta que dos imagenes colisionan
        estaColisionando = Otro.getBounds().intersects(this.getBounds());
        return estaColisionando;
    }
}
