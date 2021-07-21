package Game;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Lenny, César, Miguel, Arístides
 */
public class GameFrame extends JFrame{
    
    private GamePanel game;
    
    //Creando objeto de la clase
    private static File Nivel1 = new File("src/Sonidos/Nivel1.wav");    //Musica de niveles
    private static AudioStream Nivel1Audio;
    private static InputStream Nivel1SoundInput;
    
    
    
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
