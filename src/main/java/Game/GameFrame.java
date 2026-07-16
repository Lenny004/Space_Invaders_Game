package Game;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author Lenny
 */

public class GameFrame extends JFrame{
    
    private static GamePanel game = new GamePanel();
    
    public static void ReanudarJuego(){
    game.ReanudarJuego();
    }

    public static void PausarJuego(){
    game.PausarJuego();
    }
    
    public void ComenzarJuego(){
        game.setTutorialMode(false);
        game.setEndlessMode(false);
        startPanel();
    }

    public void ComenzarTutorial(){
        game.setTutorialMode(true);
        game.setEndlessMode(false);
        startPanel();
    }

    public void ComenzarEndless(){
        game.setTutorialMode(false);
        game.setEndlessMode(true);
        startPanel();
    }

    private void startPanel() {
        game.IniciarJuego();
        game.setDoubleBuffered(true);

        this.getContentPane().removeAll();
        this.getContentPane().add(game);
        this.pack();
        this.setResizable(false);

        if (GameConfig.getInstance().isFullscreen()) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            this.setExtendedState(JFrame.NORMAL);
            this.setLocationRelativeTo(null);
        }

        game.start();
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
        super("Space Chemistry");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        try {
            setIconImage(new ImageIcon(getClass().getResource("/Imagenes/SpaceChemistryIcon.png")).getImage());
        } catch (Exception ignored) {
        }
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
