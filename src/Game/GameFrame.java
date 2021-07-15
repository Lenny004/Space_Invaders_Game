package Game;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Lenny, César, Miguel, Arístides
 */
public class GameFrame extends JFrame{
    private GamePanel game;
    //Creando objeto de la clase
    
    public GameFrame()
    {
        // Agregar texto a la barra de título
        super("Space Chemistry");
        
        // Asegúrese de que el programa salga cuando se hace clic en el botón de cierre
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        // Cree una instancia de la clase Game y active el almacenamiento en búfer doble
        // para asegurar una animación fluida
        game = new GamePanel();
        game.setDoubleBuffered(true);
        
        // Agregue la instancia de Breakout al panel de contenido de este marco para mostrarlo
        this.getContentPane().add(game); 
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        
        // Empezar el juego
        game.start();  
    }
    
    public static void main(String[] args) 
    {
         java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame().setVisible(true);
            }
        });
    }
}
