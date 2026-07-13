package Game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Lenny, César, Miguel, Arístides
 */

public class GameFrame extends JFrame{
    
    private static GamePanel game = new GamePanel();
    private int Contador = 0;
    
    
    public static void ReanudarJuego(){
    game.ReanudarJuego();
    }

    public static void PausarJuego(){
    game.PausarJuego();
    }
    
    public void ComenzarJuego(){
        /*Utilizamos new ImageIcon(URL location).getImage() donde el argumento
        *location lo obtenemos a través del método getClass().getResource(String name)
        *que nos devuelve un URL absoluto del recurso que especificamos como String.
        */

        // Cree una instancia de la clase Game y active el almacenamiento en búfer doble
        // para asegurar una animación fluida
        game.IniciarJuego();
        game.setDoubleBuffered(true);

        // Agregue la instancia de Breakout al panel de contenido de este marco para mostrarlo
        this.getContentPane().removeAll();
        this.getContentPane().add(game);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        game.start();

        //Realiza una serie de preguntas
        game.Preguntar();
        game.quizz.setVisible(false);
        game.quizz.setVisible(true);
    }
    
    public void DetenerJuego(){
        this.setVisible(false);
        game.stop();
    }
    
    public void PararJuego(){
        this.setVisible(false);
        DetenerJuego();
    }

    public GameFrame(){
        // Agregar texto a la barra de título
        super("Space Chemistry");

        // Asegúrese de que el programa salga cuando se hace clic en el botón de cierre
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
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
