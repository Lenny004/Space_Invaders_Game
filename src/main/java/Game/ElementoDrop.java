package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @author lenny
 */
//Decimos con "extends" que es una clase Hija
public class ElementoDrop extends MovingGameObject{
    int diameter;
    int yVelocity;
    
    ImageIcon AsufreM = new ImageIcon(getClass().getResource("/Imagenes/AsufreSMini.png"));
    ImageIcon CarbonoM = new ImageIcon(getClass().getResource("/Imagenes/CarbonoSMini.png"));
    ImageIcon CobreM = new ImageIcon(getClass().getResource("/Imagenes/CobreSMini.png"));
    ImageIcon HelioM = new ImageIcon(getClass().getResource("/Imagenes/HelioSMini.png"));
    ImageIcon HidrogenoM = new ImageIcon(getClass().getResource("/Imagenes/HidrogenoSMini.png"));
    ImageIcon HierroM = new ImageIcon(getClass().getResource("/Imagenes/HierroSMini.png"));
    ImageIcon NitrogenoM = new ImageIcon(getClass().getResource("/Imagenes/NitrogenoSMini.png"));
    ImageIcon OxigenoM = new ImageIcon(getClass().getResource("/Imagenes/OxigenoSMini.png"));
    ImageIcon UranioM = new ImageIcon(getClass().getResource("/Imagenes/UranioSMini.png"));
    ImageIcon WolframioM = new ImageIcon(getClass().getResource("/Imagenes/WolframioSMini.png"));
    ImageIcon ZincM = new ImageIcon(getClass().getResource("/Imagenes/ZincSMini.png"));
    
    private int TipoElemento = 0;
    
    //Los Enemigos sueltan Drop de elementos
    public ElementoDrop(int xPosicion, int yPosicion, int Elemento, int diameter, Color color){
        //Llamando a la Clase Padre MovingGameObject
        super(xPosicion, yPosicion, 0, 0, null);
        this.TipoElemento = Elemento;
    }
    
    // Obtiene el diámetro de la bala
    public int getDiameter() {
        return diameter;
    }
    
    @Override
    // Dibuja Simbolo cayendo
    public void draw(Graphics g) {
        switch(TipoElemento){
            case 1:
                AsufreM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 2:
                CarbonoM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 3:
                CobreM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 4:
                HelioM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 5:
                HidrogenoM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 6:
                HierroM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 7:
                NitrogenoM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 8:
                OxigenoM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 9:
                UranioM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 10:
                WolframioM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            case 11:
                ZincM.paintIcon(null, g, this.getXPosition(), this.getYPosition());
                break;
            default:
                break;
        }
    }

    // Se usa para obtener el cuadro de impacto de una viga
    @Override
    public Rectangle getBounds() {
        Rectangle beamHitbox = new Rectangle(xPos, yPos, 5, 20);
        return beamHitbox;
    }
    
    @Override
    // Se usa para mover objetos no controlables
    public void move()
    {
        this.xPos += xVel;
        this.yPos += yVel;
    }
}    
