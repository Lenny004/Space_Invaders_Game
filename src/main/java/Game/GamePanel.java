package Game;

import Clases.Controlador;
import Controlador.KeyboardController;
import java.awt.Color;
import Tipografia.Fuente;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Lenny, Cesar, Arítides. Miguel
 */

public class GamePanel extends JPanel {
    
    // Componentes necesarios. ¡No quitar! 
    private Timer gameTimer;
    private Timer TiempoPowerUps;
    private Fuente TipoFuente = new Fuente();
    private static int Velocidad;
    public static int CantidadBalas;
    private Color AzulG = new Color(35,30,52);
    private Color RojoG = new Color(138,8,8);
    private Color Naranja = new Color (245, 80, 0);
    //
    // Valores necesarios para validar POWER UPS
    //POWER UP VELOCIDAD BALA
    private static int ValorO = 0;
    private static int ValorZ = 0;
    private static int ValorT = 0;
    private static int ValorU = 0;
    private static int ValorW = 0;
    private static int ValorF = 0;
    //POWER UP VELOCIDAD
    private static int ValorN = 0;
    private static int ValorS = 0;
    private static int ValorB = 0;
    private static int ValorA = 0;
    private static int ValorH = 0;
    private static int ValorP = 0;
    //
    private KeyboardController controladores;
    // Controla el tamaño de la ventana del juego y la velocidad de fotogramas. 
    private final int AnchoJuego = 1200;
    private final int AlturaJuego = 675;
    private final int Fotogramaporsegundo = 20;

    // Iniciar contadores
    Random randomDisparosE = new Random();
    Random randomElemento = new Random();
    private int Contador=0;
    private int score = 0;
    private int level = 1;
    private int numberOfLives = 3;
    private int highScore;
    private int markerX, markerY;
    private int bossHealth = 40;
    private int[] CantidadElemento = new int[12];
    File archivoPuntaje = new File("Highscore.txt");

    //Agregamos objectos de las clases
    private Ship NaveJugador; //Creamos objeto de de la Clase Ship
    private Ship singleLife; //Creamos objeto de la vida que tendra la nave de la Clase Ship 
    private Ship bonusEnemy; //
    private Enemy enemy; //Creamos objeto de la Clase Enemy
    private Shield shield;
//----------------------------------------------------
    private Bullet bullet;
    private Bullet2 bullet2;
    private Bullet3 bullet3;
    private Bullet4 bullet4;
    private Bullet5 bullet5;
    private Bullet6 bullet6;
    private Bullet7 bullet7;
//----------------------------------------------------
    private Beam beam, beam2, beam3;
    private ElementoDrop Elemento;
            
    // Agregamos Booleans
    private boolean newBulletCanFire = true;
    private boolean newBeamCanFire = true;
    private boolean newBonusEnemy = true;
    private boolean hitMarker = false; //Dar en el objetivo

    // Agregamos Array Lists
    private ArrayList<Ship> lifeList = new ArrayList();//Lista de vida
    private ArrayList<Ship> bonusEnemyList = new ArrayList();//Lista de Naves enemigas
    private ArrayList<Enemy> enemyList = new ArrayList();//Lista de enemigos
    private ArrayList<Shield> shieldList = new ArrayList();//Lista de los escudos
    private ArrayList<Beam> beamList = new ArrayList();
    private ArrayList<ElementoDrop> ElementoList = new ArrayList();// Lista de elementos
    //Compuestos
    private ImageIcon Agua = new ImageIcon(getClass().getResource("/Imagenes/Agua.png")); 
    private ImageIcon AcidoN = new ImageIcon(getClass().getResource("/Imagenes/Acido nitrico.png")); 
    private ImageIcon AcidoS = new ImageIcon(getClass().getResource("/Imagenes/Acido sulfurico.png")); 
    private ImageIcon Benceno = new ImageIcon(getClass().getResource("/Imagenes/Benceno.png")); 
    private ImageIcon Bronce = new ImageIcon(getClass().getResource("/Imagenes/Bronce.png")); 
    private ImageIcon Butano = new ImageIcon(getClass().getResource("/Imagenes/Butano.png")); 
    private ImageIcon Helio = new ImageIcon(getClass().getResource("/Imagenes/Helio.png")); 
    private ImageIcon Hierro = new ImageIcon(getClass().getResource("/Imagenes/Hierro.png")); 
    private ImageIcon Propano = new ImageIcon(getClass().getResource("/Imagenes/Propano.png")); 
    private ImageIcon TNT = new ImageIcon(getClass().getResource("/Imagenes/TNT.png")); 
    private ImageIcon Uranio = new ImageIcon(getClass().getResource("/Imagenes/Uranio.png")); 
    private ImageIcon Wolframio = new ImageIcon(getClass().getResource("/Imagenes/Wolframio.png")); 

    //Elementos
    private ImageIcon background = new ImageIcon(getClass().getResource("/Imagenes/Espacio.gif"));
    private ImageIcon background2 = new ImageIcon(getClass().getResource("/Imagenes/MaderaF.png"));
    private ImageIcon AsufreM = new ImageIcon(getClass().getResource("/Imagenes/AsufreSMini.png"));
    private ImageIcon CarbonoM = new ImageIcon(getClass().getResource("/Imagenes/CarbonoSMini.png"));
    private ImageIcon CobreM = new ImageIcon(getClass().getResource("/Imagenes/CobreSMini.png"));
    private ImageIcon HelioM = new ImageIcon(getClass().getResource("/Imagenes/HelioSMini.png"));
    private ImageIcon HidrogenoM = new ImageIcon(getClass().getResource("/Imagenes/HidrogenoSMini.png"));
    private ImageIcon HierroM = new ImageIcon(getClass().getResource("/Imagenes/HierroSMini.png"));
    private ImageIcon NitrogenoM = new ImageIcon(getClass().getResource("/Imagenes/NitrogenoSMini.png"));
    private ImageIcon OxigenoM = new ImageIcon(getClass().getResource("/Imagenes/OxigenoSMini.png"));
    private ImageIcon UranioM = new ImageIcon(getClass().getResource("/Imagenes/UranioSMini.png"));
    private ImageIcon WolframioM = new ImageIcon(getClass().getResource("/Imagenes/WolframioSMini.png"));
    private ImageIcon ZincM = new ImageIcon(getClass().getResource("/Imagenes/ZincSMini.png"));

    // Se agregaron archivos de audio y transmisiones
    private SoundEffect beamSoundAudio = SoundEffect.load("/Sonidos/alienBeam.wav");
    private SoundEffect bulletSoundAudio = SoundEffect.load("/Sonidos/bulletSound.wav");
    private SoundEffect levelUpSoundAudio = SoundEffect.load("/Sonidos/levelUpSound.wav");
    private SoundEffect deathSoundAudio = SoundEffect.load("/Sonidos/deathSound.wav");
    private SoundEffect hitSoundAudio = SoundEffect.load("/Sonidos/hitmarkerSound.wav");
    private SoundEffect shieldSoundAudio = SoundEffect.load("/Sonidos/shieldSound.wav");
    private SoundEffect bossSoundAudio = SoundEffect.load("/Sonidos/bossSound.wav");
    private SoundEffect bonusSoundAudio = SoundEffect.load("/Sonidos/bonusSound.wav");
    private SoundEffect damageSoundAudio = SoundEffect.load("/Sonidos/damageSound.wav");
    
    //Parametro de dificultad
    Configuracion Dificultad = new Configuracion(); //Creamos objeto de la Clase Configuración (La dificultad del juego)

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Metodos extra
    
    // Usado en la clase Enemy para ayudar con el método de dibujo para el jefe
    public int getBossHealth() {
        return bossHealth;
    }
    
    public static Quizz quizz = new Quizz();

    public void ReanudarJuego(){
        gameTimer.start();
        score+= quizz.bonus;
        quizz.bonus = 0;
        quizz.valor = 0;
    }

    public void PausarJuego(){
        gameTimer.stop();
    }

    public void Preguntar(){
        quizz.MetodoPreguntas();
        quizz.setVisible(true);
    }

    public void sumarBonus(){
        System.out.println(quizz.bonus);
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURAR JUEGO

    public final void ConfigurarJuego() {
        
        //Panel de victoria si gana
        if(level > 15){
            Controlador obj = new Controlador();
            obj.setUsername(FrmNombre.nombre);
            obj.setScoreU(score);
            obj.guardarScore();
            
            Victoria vic = new Victoria();
            vic.AsignarScore(score);
            vic.setVisible(true);
        }
        
        // Establece enemigos para niveles normales
        //Llamamos al método que reproduce la música po nivel
        if(level%3 <= 1){
            MusicaLevel();
        }
        
       //Validamos si el nivel es diferente de divisor de 3, que serían los normales
        if (level%3 != 0) {
            // 6 Filas
            for (int row = 0; row < 6; row++) {
                // 5 Columnas
                for (int column = 0; column < 5; column++) {
                    switch(Dificultad.getTipoDificultad()){
                        case 1://Dificil
                                //Lo mandamos a la clase Enemy con los siguientes parametros "(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level)"
                                enemy = new Enemy((120 + (row * 100)), (20 + (column * 60)), (1*level), 0, column, null, 40, 40, level); // La velocidad del enemigo aumentará en cada nivel
                                enemyList.add(enemy);
                            break;
                        case 2://Medio
                                //Lo mandamos a la clase Enemy con los siguientes parametros "(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level)"
                                enemy = new Enemy((120 + (row * 100)), (20 + (column * 60)), (4), 0, column, null, 40, 40, level); // La velocidad del enemigo aumentará en cada nivel
                                enemyList.add(enemy);
                            break;
                        case 3://Fácil
                                //Lo mandamos a la clase Enemy con los siguientes parametros "(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level)"
                                enemy = new Enemy((120 + (row * 100)), (20 + (column * 60)), (2  ), 0, column, null, 40, 40, level); // La velocidad del enemigo aumentará en cada nivel
                                enemyList.add(enemy);
                            break;
                        default:
                                //Lo mandamos a la clase Enemy con los siguientes parametros "(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level)"
                                enemy = new Enemy((120 + (row * 100)), (20 + (column * 60)), (1*level), 0, column, null, 40, 40, level); // La velocidad del enemigo aumentará en cada nivel
                                enemyList.add(enemy);
                            break;
                    }
                }
            }
        }
        // Sino si son divisores de 3, es el nivel del jefe
        else{
            switch(Dificultad.getTipoDificultad()){
                case 1://Dificil
                        // Establece enemigo para los niveles de jefe
                        bossSoundAudio.play(); // Reproduce el rugido del jefe
                        //Lo mandamos a la clase Enemy con los siguientes parametros "(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level)"
                        enemy = new Enemy(120, 20, (3 * (level / 3)), 0, 100, null, 150, 150, level);
                        enemyList.add(enemy);
                    break;
                case 2://Medio
                        // Establece enemigo para los niveles de jefe
                        bossSoundAudio.play(); // Reproduce el rugido del jefe
                        //Lo mandamos a la clase Enemy con los siguientes parametros "(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level)"
                        enemy = new Enemy(120, 20, (2 * (level / 3)), 0, 100, null, 150, 150, level);
                        enemyList.add(enemy);
                    break;
                case 3://Facil
                        // Establece enemigo para los niveles de jefe
                        bossSoundAudio.play(); // Reproduce el rugido del jefe
                        //Lo mandamos a la clase Enemy con los siguientes parametros "(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level)"
                        enemy = new Enemy(120, 20, (1 * (level / 3)), 0, 100, null, 150, 150, level);
                        enemyList.add(enemy);
                    break;
                default://Dificil
                        // Establece enemigo para los niveles de jefe
                        bossSoundAudio.play(); // Reproduce el rugido del jefe
                        //Lo mandamos a la clase Enemy con los siguientes parametros "(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level)"
                        enemy = new Enemy(120, 20, (3 * (level / 3)), 0, 100, null, 150, 150, level);
                        enemyList.add(enemy);
                    break;
            }
        }
        // Restablece todos los movimientos del controlador con "resetController"
        controladores.resetController();

        // Establece los valores de la nave del jugador
        // Le enviamos los siguientes parametros a la Clase Ship (int xPosition, int yPosition, Color color, KeyboardController control)
        NaveJugador = new Ship(500, 600, null, controladores);

        // Establece el contador de vidas.
        for (int column = 0; column < numberOfLives; column++) {
            singleLife = new Ship(48 + (column * 20), 10, Color.WHITE, null);
            lifeList.add(singleLife);
         }

        //Establece los valores para 3 filas y 3 columnas de escudos.
        for (int row = 0;
                row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                shield = new Shield(100 + (column * 333), 500 - (row * 10), 150, 10, Color.RED);
                shieldList.add(shield);
            }
        }
    }
    
    
    //Método para música en cada nivel
    public void MusicaLevel(){
        try{
            //Archivo de música a colocar
            String NombreArchivo = "Nivel";
            NombreArchivo+= Integer.toString( (level-1)/3 + 1 );
            if(level%3 == 0) NombreArchivo+= "Boss";
    
           //Cambiar el archivo de música
           
           MusicPlayer.IntentarMusica("src/Sonidos/" + NombreArchivo + ".wav");
        }
        catch(Exception e){
            System.out.println("Error: no se pudo encontrar el archivo de música ");
        }
    }
    
    public void PowerUpVelocidad(){
        switch(Velocidad){
            case 1:
                    NaveJugador.move2();
                break;
            case 2:
                    NaveJugador.move3();
                break;
            case 3:
                    NaveJugador.move4();
                break;
            case 4:
                    NaveJugador.move5();
                break;
            case 5:
                    NaveJugador.move6();
                break;
            case 6:
                    NaveJugador.move7();
                break;
            default:
                    NaveJugador.move();
                break;
        }
    }
    
    
    public void PowerUpBalas(){
        switch(CantidadBalas){
            
            case 0:
                bullet = new Bullet(NaveJugador.getXPosition() + 22, NaveJugador.getYPosition() - 20, 0,Naranja);
                newBulletCanFire = false;
                break;
            
            case 1:
                bullet = new Bullet(NaveJugador.getXPosition() + 22, NaveJugador.getYPosition() - 20, 0,Naranja);
                bullet2 = new Bullet2(NaveJugador.getXPosition() + 35, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                newBulletCanFire = false;
                break;
                
            case 2:
                bullet = new Bullet(NaveJugador.getXPosition() + 22, NaveJugador.getYPosition() - 20, 0, Color.RED);
                bullet2 = new Bullet2(NaveJugador.getXPosition() + 30, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                bullet3 = new Bullet3(NaveJugador.getXPosition() + 14, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                newBulletCanFire = false;
                break;
                
            case 3:
                bullet = new Bullet(NaveJugador.getXPosition() + 22, NaveJugador.getYPosition() - 20, 0, Color.RED);
                bullet2 = new Bullet2(NaveJugador.getXPosition() + 30, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                bullet3 = new Bullet3(NaveJugador.getXPosition() + 14, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                bullet4 = new Bullet4(NaveJugador.getXPosition() + 38, NaveJugador.getYPosition() - 20, 0, Color.CYAN);
                newBulletCanFire = false;
                break;
                
            case 4:
                bullet = new Bullet(NaveJugador.getXPosition() + 22, NaveJugador.getYPosition() - 20, 0, Color.RED);
                bullet2 = new Bullet2(NaveJugador.getXPosition() + 30, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                bullet3 = new Bullet3(NaveJugador.getXPosition() + 14, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                bullet4 = new Bullet4(NaveJugador.getXPosition() + 38, NaveJugador.getYPosition() - 20, 0, Color.CYAN);
                bullet5 = new Bullet5(NaveJugador.getXPosition() + 6, NaveJugador.getYPosition() - 20, 0, Color.CYAN);
                newBulletCanFire = false;
                break;
                
            case 5: 
                bullet = new Bullet(NaveJugador.getXPosition() + 22, NaveJugador.getYPosition() - 20, 0, Color.RED);
                bullet2 = new Bullet2(NaveJugador.getXPosition() + 30, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                bullet3 = new Bullet3(NaveJugador.getXPosition() + 14, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                bullet4 = new Bullet4(NaveJugador.getXPosition() + 38, NaveJugador.getYPosition() - 20, 0, Color.CYAN);
                bullet5 = new Bullet5(NaveJugador.getXPosition() + 6, NaveJugador.getYPosition() - 20, 0, Color.CYAN);
                bullet6 = new Bullet6(NaveJugador.getXPosition() + 46, NaveJugador.getYPosition() - 20, 0, Color.GREEN);
                newBulletCanFire = false;
                break;
                
            case 6:
                bullet = new Bullet(NaveJugador.getXPosition() + 22, NaveJugador.getYPosition() - 20, 0, Color.RED);
                bullet2 = new Bullet2(NaveJugador.getXPosition() + 30, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                bullet3 = new Bullet3(NaveJugador.getXPosition() + 14, NaveJugador.getYPosition() - 20, 0, Color.BLUE);
                bullet4 = new Bullet4(NaveJugador.getXPosition() + 38, NaveJugador.getYPosition() - 20, 0, Color.CYAN);
                bullet5 = new Bullet5(NaveJugador.getXPosition() + 6, NaveJugador.getYPosition() - 20, 0, Color.CYAN);
                bullet6 = new Bullet6(NaveJugador.getXPosition() + 46, NaveJugador.getYPosition() - 20, 0, Color.GREEN);
                bullet7 = new Bullet7(NaveJugador.getXPosition() - 2, NaveJugador.getYPosition() - 20, 0, Color.GREEN);
                newBulletCanFire = false;
                break;
                
            default:
                break;
        }
    }
    
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PAINT
    @Override
    public void paint(Graphics g) {//Clase paint del Methodo Graphics 

        // Configuración de imagen de fondo del juego
        //"paintIcon" Una implementación de la interfaz de iconos que pinta iconos a partir de imágenes. 
        //Las imágenes que se crean a partir de una URL,
        //bytes se cargan previamente con MediaTracker para monitorear el estado de carga de la imagen.
        background.paintIcon(this, g, WIDTH, WIDTH);
        background2.paintIcon(this, g, 1000, 1);
        
        g.drawString(TOOL_TIP_TEXT_KEY, WIDTH, WIDTH);

//---------------------------------------------------------------------------
        // hace una cadena que dice "+100" al golpear al enemigo
        if (bullet != null) {
            if (hitMarker) {
                g.setColor(Color.WHITE);
                if (level%3 != 0) {
                    g.drawString("+ 100", markerX + 20, markerY -= 1);
                } else {
                    g.drawString("- 1", markerX + 75, markerY += 1);
                }
            }
        }
        
        // hace una cadena que dice "+100" al golpear al enemigo
        if (bullet2 != null) {
            if (hitMarker) {
                g.setColor(Color.WHITE);
                if (level%3 != 0) {
                    g.drawString("+ 100", markerX + 20, markerY -= 1);
                } else {
                    g.drawString("- 1", markerX + 75, markerY += 1);
                }
            }
        }
        
        // hace una cadena que dice "+100" al golpear al enemigo
        if (bullet3 != null) {
            if (hitMarker) {
                g.setColor(Color.WHITE);
                if (level%3 != 0) {
                    g.drawString("+ 100", markerX + 20, markerY -= 1);
                } else {
                    g.drawString("- 1", markerX + 75, markerY += 1);
                }
            }
        }
        
        // hace una cadena que dice "+100" al golpear al enemigo
        if (bullet4 != null) {
            if (hitMarker) {
                g.setColor(Color.WHITE);
                if (level%3 != 0) {
                    g.drawString("+ 100", markerX + 20, markerY -= 1);
                } else {
                    g.drawString("- 1", markerX + 75, markerY += 1);
                }
            }
        }
        
        // hace una cadena que dice "+100" al golpear al enemigo
        if (bullet5 != null) {
            if (hitMarker) {
                g.setColor(Color.WHITE);
                if (level%3 != 0) {
                    g.drawString("+ 100", markerX + 20, markerY -= 1);
                } else {
                    g.drawString("- 1", markerX + 75, markerY += 1);
                }
            }
        }
        
        // hace una cadena que dice "+100" al golpear al enemigo
        if (bullet6 != null) {
            if (hitMarker) {
                g.setColor(Color.WHITE);
                if (level%3 != 0) {
                    g.drawString("+ 100", markerX + 20, markerY -= 1);
                } else {
                    g.drawString("- 1", markerX + 75, markerY += 1);
                }
            }
        }
        
        // hace una cadena que dice "+100" al golpear al enemigo
        if (bullet7 != null) {
            if (hitMarker) {
                g.setColor(Color.WHITE);
                if (level%3 != 0) {
                    g.drawString("+ 100", markerX + 20, markerY -= 1);
                } else {
                    g.drawString("- 1", markerX + 75, markerY += 1);
                }
            }
        }
//---------------------------------------------------------------------------
        
        // Dibuja la nave del jugador
        NaveJugador.draw(g);

        // Dibuja 3 escudos espaciados uniformemente 
        for (int index = 0; index < shieldList.size(); index++) {
            shieldList.get(index).draw(g);
        }

        // Dibuja 3 tipos diferentes de alienígenas
        try {
            for (int index = 0; index < enemyList.size(); index++) {
                enemyList.get(index).draw(g);
            }
        } 
        catch (IndexOutOfBoundsException e) {
            
        }
        
//        //Reanudar juego
//        if (controladores.getKeyStatus(32)){//(32) es la tecla espacio del teclado
//            try{
//                GameFrame.ReanudarJuego();
//            }
//            catch(Exception e){
//                
//            }
//        }

        // Dibuja una viñeta en la barra espaciadora presiona
        if (controladores.getKeyStatus(32)) {//KEYSTATUS(32) es la barra de espacio según el código ASCII
            if (newBulletCanFire) {
                newBeamCanFire = false;
                bulletSoundAudio.play();
                PowerUpBalas();
            }
        }
        
        // Si intenta sacar la bala después de presionar una tecla
        if (bullet != null) {
            bullet.draw(g);
        }
        
        if (bullet2 != null) {
            bullet2.draw(g);
        }
        
        if (bullet3 != null) {
            bullet3.draw(g);
        }
        
        if (bullet4 != null) {
            bullet4.draw(g);
        }
                
        if (bullet5 != null) {
            bullet5.draw(g);
        }        
        
        if (bullet6 != null) {
            bullet6.draw(g);
        }
                
        if (bullet7 != null) {
            bullet7.draw(g);
        }
        
        // Mueve elementos en niveles normales
        if (level%3 != 0) {
            if (beam != null) {
                for (int index = 0; index < ElementoList.size(); index++) {
                    ElementoList.get(index).setYPosition(ElementoList.get(index).getYPosition() + (4));
                    if (ElementoList.get(index).getYPosition() > 600) {
                        ElementoList.remove(index);
                    }
                }
            }
        }
        
        // Dibuja los elementos generados
        for (int index = 0; index < ElementoList.size(); index++) {
            ElementoList.get(index).draw(g);
        }
        
        
        // Genera rayos aleatorios disparados por enemigos.
        if (level%3 != 0) {
            if (newBeamCanFire) {
                for (int index = 0; index < enemyList.size(); index++) {
                    if (randomDisparosE.nextInt(30) == index) {
                        beam = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition(), 0, Color.YELLOW);
                        beamList.add(beam);
                        beamSoundAudio.play(); // Reproduce el sonido del rayo para enemigos normales.
                    }
                    newBeamCanFire = false;
                }
            }
        }
        
        // Genera vigas a un ritmo más rápido para el jefe.
        if (level%3 == 0) {
            if (newBeamCanFire) {
                for (int index = 0; index < enemyList.size(); index++) {
                    if (randomDisparosE.nextInt(5) == index) {
                        beam = new Beam(enemyList.get(index).getXPosition() + 75, enemyList.get(index).getYPosition() + 140, 0, Color.YELLOW);
                        beam2 = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition() + 110, 0, Color.YELLOW);
                        beam3 = new Beam(enemyList.get(index).getXPosition() + 150, enemyList.get(index).getYPosition() + 110, 0, Color.YELLOW);
                        beamList.add(beam);
                        beamList.add(beam2);
                        beamList.add(beam3);
                        beamSoundAudio.play(); // Reproduce sonido de haz para el jefe
                    }
                    newBeamCanFire = false;
                }
            }
        }
        
        // Dibuja los rayos generados
        for (int index = 0; index < beamList.size(); index++) {
            beamList.get(index).draw(g);
        }
        // Genera un enemigo de bonificación aleatorio
        if (newBonusEnemy && level%3 != 0) {
            if (randomDisparosE.nextInt(3000) == 1500) {
                bonusEnemy = new Ship(-50, 30, Color.RED, null);
                bonusEnemyList.add(bonusEnemy);
                newBonusEnemy = false;
            }
        }
        // Coloca enemigo bonus
        for (int index = 0; index < bonusEnemyList.size(); index++) {
            bonusEnemyList.get(index).bonusDraw(g);
        }
        
        //Teclas Presionadas para generar elementos
        //POWER UP BALAS NAVE
        //H2O
        if (controladores.getKeyStatus(79)) { //KEYSTATUS(79) es la O según el código ASCII
            if(ValorO < 1){
                if (CantidadElemento[5] >=2 && CantidadElemento[8] >=1 ){
                    CantidadElemento[5] = CantidadElemento[5]-2;
                    CantidadElemento[8]--;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("O", 500, 80);
                    this.CantidadBalas = Math.max(CantidadBalas, 3);
                    ValorO++;
                }    
            }  
        }
        
        //Cu3Zn2
        if (controladores.getKeyStatus(90)) { //KEYSTATUS(90) es la Z según el código ASCII
            if(ValorZ < 1){
                if (CantidadElemento[3] >=3 && CantidadElemento[11] >=2 ){
                    CantidadElemento[3] = CantidadElemento[3]-3;
                    CantidadElemento[11] = CantidadElemento[11]-2;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("Z", 500, 80);
                     this.CantidadBalas = Math.max(CantidadBalas, 5);
                    ValorZ++;
                }  
            }
        }
                
        //C7H5N3O6
        if (controladores.getKeyStatus(84)) { //KEYSTATUS(84) es la T según el código ASCII
            if(ValorT < 1){
                if (CantidadElemento[2] >=7 && CantidadElemento[5] >=5 && CantidadElemento[7] >=3 && CantidadElemento[8] >=6){
                    CantidadElemento[2] = CantidadElemento[2]-7;
                    CantidadElemento[5] = CantidadElemento[5]-5;
                    CantidadElemento[7] = CantidadElemento[7]-3;
                    CantidadElemento[8] = CantidadElemento[8]-6;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("T", 500, 80);
                     this.CantidadBalas = Math.max(CantidadBalas, 6);
                    ValorT++;
                }  
            }
        }
        
        //U
        if (controladores.getKeyStatus(85)) { //KEYSTATUS(85) es la U según el código ASCII
            if(ValorU<1){
                if (CantidadElemento[9] >=1 ){
                    CantidadElemento[9]--;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("U", 500, 80);
                     this.CantidadBalas = Math.max(CantidadBalas, 4);
                    ValorU++;
                }
            }    
        }
        
        //W
        if (controladores.getKeyStatus(87)) { //KEYSTATUS(87) es la W según el código ASCII
            if(ValorW<1){
                if (CantidadElemento[10] >=1 ){
                    CantidadElemento[10]--;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("W", 500, 80);
                     this.CantidadBalas = Math.max(CantidadBalas, 2);
                    ValorW++;
                }  
            }
        }
        
        //Fe
        if (controladores.getKeyStatus(70)) { //KEYSTATUS(70) es la F según el código ASCII
            if(ValorF<1){
                if (CantidadElemento[6] >=1 ){
                    CantidadElemento[6]--;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("F", 500, 80);
                     this.CantidadBalas = Math.max(CantidadBalas, 1);
                    ValorF++;
                }  
            }
        }
        
        
        //POWER UP VELOCIDAD NAVE
        //HNO3
        if (controladores.getKeyStatus(78)) { //KEYSTATUS(78) es la N según el código ASCII
            if(ValorN < 1){
                if(CantidadElemento[5] >=1 && CantidadElemento[7] >=1 && CantidadElemento[8] >=3){
                    CantidadElemento[5]--;
                    CantidadElemento[7]--;
                    CantidadElemento[8] = CantidadElemento[8]-3;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("N", 500, 80);
                    this.Velocidad = 2;
                    ValorN++;
                    PowerUpVelocidad();
                }
            }
        }
        
        //H2SO4 
        if (controladores.getKeyStatus(83)) { //KEYSTATUS(83) es la S según el código ASCII
            if(ValorS < 1){
                if (CantidadElemento[5] >=2 && CantidadElemento[1] >=1 && CantidadElemento[8] >=4 ){
                    CantidadElemento[5] = CantidadElemento[5]-2;
                    CantidadElemento[1]--;
                    CantidadElemento[8] = CantidadElemento[8]-4;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("S", 500, 80);
                    this.Velocidad = 3;
                    ValorS++;
                    PowerUpVelocidad();
                }  
            }
        }
        
        //C6H6
        if (controladores.getKeyStatus(66)) { //KEYSTATUS(66) es la B según el código ASCII
            if(ValorB < 1){
                if (CantidadElemento[2] >=6 && CantidadElemento[5] >=6 ){
                    CantidadElemento[2] = CantidadElemento[2]-6;
                    CantidadElemento[5] = CantidadElemento[5]-6;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("B", 500, 80);
                    this.Velocidad = 4;
                    ValorB++;
                    PowerUpVelocidad();
                }  
            }
        }
        
        //C4H10   
        if (controladores.getKeyStatus(65)) { //KEYSTATUS(65) es la A según el código ASCII
            if(ValorA < 1){
                if (CantidadElemento[2] >=4 && CantidadElemento[5] >=10){
                    CantidadElemento[2] = CantidadElemento[2]-4;
                    CantidadElemento[5] = CantidadElemento[5]-10;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("A", 500, 80);
                    this.Velocidad = 6;
                    ValorA++;
                    PowerUpVelocidad();
                }  
            }
        }
        
        //He
        if (controladores.getKeyStatus(72)) { //KEYSTATUS(72) es la H según el código ASCII
            if(ValorH<1){
                if (CantidadElemento[4] >=1 ){
                    CantidadElemento[4]--;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("H", 500, 80);
                    this.Velocidad = 1;
                    ValorH++;
                    PowerUpVelocidad();
                }  
            }
        }
        
        //C3H8
        if (controladores.getKeyStatus(80)) { //KEYSTATUS(80) es la P según el código ASCII
            if(ValorP < 1){
                if (CantidadElemento[2] >=3 && CantidadElemento[5] >=8){
                    CantidadElemento[2] = CantidadElemento[2]-3;
                    CantidadElemento[5] = CantidadElemento[5]-8;
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
                    g.drawString("P", 500, 80);
                    this.Velocidad = 5;
                    ValorP++;
                    PowerUpVelocidad();
                }  
            }
        }
        
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 12));
        // Establece la visualización de la puntuación
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 260, 20);

        // Configura la visualización del contador de vida
        g.setColor(Color.WHITE);
        g.drawString("Vidas:", 11, 20);
        for (int index = 0; index < lifeList.size(); index++) {
            lifeList.get(index).lifeDraw(g);
        }
        // Establece la visualización de nivel
        g.setColor(Color.WHITE);
        g.drawString("Nivel " + level, 750, 20);

        // Establece la visualización de Highscore
        g.setColor(Color.WHITE);
        g.drawString("High Score: " + highScore, 440, 20);

        // Dibuja una pantalla de salud para el nivel de jefe
        if (level%3 == 0) {
            g.setColor(Color.WHITE);
            g.drawString("Vida del Jefe: " + bossHealth, 500, 600);
        }
        
        //G le asignamos la tipografía personalizada y comenzará a tener ese valor de este punto en adelante
        g.setColor(Color.WHITE);
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 12));
        //Mensaje Press
        g.drawString("PRESS", 1010, 20);
        
        
        //G le asignamos la tipografía personalizada y comenzará a tener ese valor de este punto en adelante
        g.setColor(Color.BLACK);
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
        
        //Mensaje para avisar que tecla presionar
        g.drawString("F", 1015, 43);
        g.drawString("W", 1015, 78);
        g.drawString("O", 1015, 112);
        g.drawString("U", 1015, 146);
        g.drawString("Z", 1015, 180);
        g.drawString("T", 1015, 217);
        g.drawString("H", 1015, 253);
        g.drawString("N", 1015, 288);
        g.drawString("S", 1015, 322);
        g.drawString("B", 1015, 358);
        g.drawString("P", 1015, 392);
        g.drawString("A", 1015, 428);
        
        //Compuestos dibujados
        g.setColor(RojoG);
        Hierro.paintIcon(this, g, 1045, 20);
        g.drawString("Fe", 1085, 43);
        
        Wolframio.paintIcon(this, g, 1045, 55);
        g.drawString("W", 1085, 78);
        
        Agua.paintIcon(this, g, 1045, 90);
        g.drawString("H2O", 1085, 112);
        
        Uranio.paintIcon(this, g, 1045, 125);
        g.drawString("U", 1085, 146);
        
        Bronce.paintIcon(this, g, 1045, 160);
        g.drawString("Cu3Zn2", 1085, 180);
        
        TNT.paintIcon(this, g, 1045, 195);
        g.drawString("C7H5N3O6", 1085, 217);
        
        g.setColor(AzulG);
        Helio.paintIcon(this, g, 1045, 230);
        g.drawString("He", 1085, 253);
        
        AcidoN.paintIcon(this, g, 1045, 265);
        g.drawString("HNO3", 1085, 288);
        
        AcidoS.paintIcon(this, g, 1045, 300);
        g.drawString("H2SO4", 1085, 322);
        
        Benceno.paintIcon(this, g, 1045, 335);
        g.drawString("C6H6", 1085, 358);
        
        Propano.paintIcon(this, g, 1045, 370);
        g.drawString("C3H8", 1085, 392);
        
        Butano.paintIcon(this, g, 1045, 405);
        g.drawString("C4H10", 1085, 428);
        
        
        g.setColor(Color.BLACK);
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 24));
        //Elementos dibujados
        AsufreM.paintIcon(this, g, 1015, 480);
        g.drawString("- "+ CantidadElemento[1], 1045, 500);
        
        CarbonoM.paintIcon(this, g, 1115, 480);
        g.drawString("- "+ CantidadElemento[2], 1145, 500);
        
        CobreM.paintIcon(this, g, 1015, 510);
        g.drawString("- "+ CantidadElemento[3], 1045, 530);
        
        HelioM.paintIcon(this, g, 1115, 510);
        g.drawString("- "+ CantidadElemento[4], 1145, 530);
        
        HidrogenoM.paintIcon(this, g, 1015, 540);
        g.drawString("- "+ CantidadElemento[5], 1045, 560);
        
        HierroM.paintIcon(this, g, 1115, 540);
        g.drawString("- "+ CantidadElemento[6], 1145, 560);
        
        NitrogenoM.paintIcon(this, g, 1015, 570);
        g.drawString("- "+ CantidadElemento[7], 1045, 590);
        
        OxigenoM.paintIcon(this, g, 1115, 570);
        g.drawString("- "+ CantidadElemento[8], 1145, 590);
        
        UranioM.paintIcon(this, g, 1015, 600);
        g.drawString("- "+ CantidadElemento[9], 1045, 620);
        
        WolframioM.paintIcon(this, g, 1115, 600);
        g.drawString("- "+ CantidadElemento[10], 1145, 620);
        
        ZincM.paintIcon(this, g, 1015, 630);
        g.drawString("- "+ CantidadElemento[11], 1045, 650);
    }
    
    public void ColisionesBalas(int index){
        // Puntaje de actualizaciones para niveles normales
        if (level%3 != 0) {
            score += 100;
            hitMarker = true;
            markerX = enemyList.get(index).getXPosition(); // Obtiene posiciones de las que se genera el "+ 100"
            markerY = enemyList.get(index).getYPosition();
            enemyList.remove(index);
                        
        // Cuando se destruye una nave enemiga Dropea un elemento de la Tabla Periodica
        int ElementoDado = randomElemento.nextInt(22-1);
        //Manda a la Clase ElementoDrop "int xPosicion, int yPosicion, int xVelocity, int yVelocity, int Elemento, Color color, int width, int height"
        Elemento = new ElementoDrop(markerX, markerY, ElementoDado, 0, null);
        ElementoList.add(Elemento);
        Elemento.move();
        if (ElementoDado <= 11){
            CantidadElemento[ElementoDado]++;
            bonusSoundAudio.play(); // Reproduce sonido de bonus
            }
        }
        // Actualiza la puntuación para los niveles de jefe.
        if (level%3 == 0) {
            hitMarker = true;
            markerX = enemyList.get(index).getXPosition(); // Obtiene posiciones de las que se genera el "- 1"
            markerY = enemyList.get(index).getYPosition() + 165;
            bossHealth -= 1;
            if (bossHealth == 0) {
                enemyList.remove(index);
                score += 9000;// Puntaje de bonificación por derrotar al jefe
            }
        }
    }
    
    public void ColisionesEscudo(int index){
        // Cada declaración if cambia el color del escudo, lo que indica "fuerza"
        // FUERTE
        if (shieldList.get(index).getColor() == Color.RED) {
            shieldList.get(index).setColor(Color.ORANGE);
            shieldSoundAudio.play(); // Plays sound if shield takes damage
            newBulletCanFire = true;
        // BIEN
        } else if (shieldList.get(index).getColor() == Color.ORANGE) {
            shieldList.get(index).setColor(Color.YELLOW);
            shieldSoundAudio.play();
            newBulletCanFire = true;
        // OKAY
        } else if (shieldList.get(index).getColor() == Color.YELLOW) {
            shieldList.get(index).setColor(Color.WHITE);
            shieldSoundAudio.play();
            newBulletCanFire = true;
        // DÉBIL, SE ROMPE AL GOLPEAR
        } else if (shieldList.get(index).getColor() == Color.WHITE) {
            shieldList.remove(index);
            shieldSoundAudio.play();
            newBulletCanFire = true;
        }
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ACTUALIZAR ESTADO DEL JUEGO
    
    public void ActualizarEstadoJuego(int frameNumber) {
        //MusicaLevel();
        // Permite al jugador moverse hacia la izquierda y hacia la derecha
        PowerUpVelocidad();
        // Actualiza highscore
        try {
            Scanner fileScan = new Scanner(archivoPuntaje);
            while (fileScan.hasNextInt()) {
                String nextLine = fileScan.nextLine();
                Scanner lineScan = new Scanner(nextLine);
                highScore = lineScan.nextInt();
            }
        } catch (FileNotFoundException e) {
            
        }
        
//        //Pausar Juego
//        if(controladores.getKeyStatus2(KeyEvent.VK_ESCAPE)){//VK_ESCAPE(ESC) es la tecla escape del teclado
//            try{
//                GameFrame.PausarJuego();
//                MenuEmergente emergente = new MenuEmergente();
//                emergente.setVisible(true);
//            }
//            catch(Exception e){
//                
//            }
//        }
        
        // Agrega la opción para restablecer el puntaje alto
        if (controladores.getKeyStatus(82)) { // KEYSTATUS(82) es la tecla R según el código ASCII
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Te gustaría reiniciar el Highscore?", ":)", 0);
            //Llama a la Clase KeyboardController
            
            controladores.resetController();
            if (respuesta == 0) {
                try {
                    String scoreString = Integer.toString(0);
                    //La clase Java PrintWriter es la implementación de la clase Writer. 
                    //Se utiliza para imprimir la representación formateada de objetos en el flujo de salida de texto.
                    
                    //Crea un archivo OutputStream para escribir en el archivo con el nombre especificado. 
                    //Si el segundo argumento es verdadero, los bytes se escribirán al final del archivo 
                    //en lugar de al principio.
                    PrintWriter pw = new PrintWriter(new FileOutputStream(archivoPuntaje, false));
                    pw.write(scoreString);
                    pw.close();
                } 
                catch (FileNotFoundException e) {
                    
                }
            }
        }
        // Actualiza el archivo de texto de la puntuación más alta si su puntuación supera la puntuación más alta anterior
        try {
            if (score > highScore) {
                String scoreString = Integer.toString(score);
                PrintWriter pw = new PrintWriter(new FileOutputStream(archivoPuntaje, false));
                pw.write(scoreString);
                pw.close();
            }
        } 
        catch (FileNotFoundException e) {
            
        }

        // Hace que los enemigos se muevan y cambien de dirección en las fronteras.
        if(level%3 != 0){
            if ((enemyList.get(enemyList.size() - 1).getXPosition() + enemyList.get(enemyList.size() - 1).getXVelocity()) > 950 || (enemyList.get(0).getXPosition() + enemyList.get(0).getXVelocity()) < 50) {
                for (int index = 0; index < enemyList.size(); index++) {
                    enemyList.get(index).setXVelocity(enemyList.get(index).getXVelocity() * -1);
                    enemyList.get(index).setYPosition(enemyList.get(index).getYPosition() + 10);
                }
            } else {
                for (int index = 0; index < enemyList.size(); index++) {
                    enemyList.get(index).move();
                }
            }
        }
        if(level%3 == 0){
            if ((enemyList.get(enemyList.size() - 1).getXPosition() + enemyList.get(enemyList.size() - 1).getXVelocity()) > 830 || (enemyList.get(0).getXPosition() + enemyList.get(0).getXVelocity()) < 50) {
                for (int index = 0; index < enemyList.size(); index++) {
                    enemyList.get(index).setXVelocity(enemyList.get(index).getXVelocity() * -1);
                    enemyList.get(index).setYPosition(enemyList.get(index).getYPosition() + 10);
                }
            } else {
                for (int index = 0; index < enemyList.size(); index++) {
                    enemyList.get(index).move();
                }
            }
        }

        // Mover bala
        if (bullet != null) {
            bullet.setYPosition(bullet.getYPosition() - 15);
            if (bullet.getYPosition() < 0) {
                newBulletCanFire = true;
            }

            // Comprueba si hay colisiones con enemigos normales.
            for (int index = 0; index < enemyList.size(); index++) {
                //Se lo manda a la clase en GameObject para verificar si esta colisionando
                if (bullet.Colisionando(enemyList.get(index))) {
                    hitSoundAudio.play(); // Reproduce un sonido de hitmarker si golpeas a un enemigo
                    //Le manda parametros a la clase Bullet (int xPosition, int yPosition, int diameter, Color color)
                    bullet = new Bullet(0, 0, 0, null);
                    newBulletCanFire = true;
                    ColisionesBalas(index);
                }
            }
            // Comprueba si hay colisiones con escudo y balas.
            for (int index = 0; index < shieldList.size(); index++) {
                //Se lo manda a la Clase GameObject
                if (bullet.Colisionando(shieldList.get(index))){
                    bullet = new Bullet(0, 0, 0, null);
                    ColisionesEscudo(index);
                }
            }
        }
        
//--------// Mover bala2
        if (bullet2 != null) {
            bullet2.setYPosition(bullet2.getYPosition() - 15);
            if (bullet2.getYPosition() < 0) {
                newBulletCanFire = true;
            }
            // Comprueba si hay colisiones con enemigos normales.
            for (int index = 0; index < enemyList.size(); index++) {
                //Se lo manda a la clase en GameObject para verificar si esta colisionando
                if (bullet2.Colisionando(enemyList.get(index))) {
                    hitSoundAudio.play(); // Reproduce un sonido de hitmarker si golpeas a un enemigo
                    //Le manda parametros a la clase Bullet (int xPosition, int yPosition, int diameter, Color color)
                    bullet2 = new Bullet2(0, 0, 0, null);
                    newBulletCanFire = true;
                    ColisionesBalas(index);        
                }
            }
            // Comprueba si hay colisiones con escudo y balas.
            for (int index = 0; index < shieldList.size(); index++) {
                //Se lo manda a la Clase GameObject
                if (bullet2.Colisionando(shieldList.get(index))){
                    bullet2 = new Bullet2(0, 0, 0, null);
                    ColisionesEscudo(index);
                }
            }
        }
//--------// Mover bala 3
        if (bullet3 != null) {
            bullet3.setYPosition(bullet3.getYPosition() - 15);
            if (bullet3.getYPosition() < 0) {
                newBulletCanFire = true;
            }
            // Comprueba si hay colisiones con enemigos normales.
            for (int index = 0; index < enemyList.size(); index++) {
                //Se lo manda a la clase en GameObject para verificar si esta colisionando
                if (bullet3.Colisionando(enemyList.get(index))) {
                    hitSoundAudio.play(); // Reproduce un sonido de hitmarker si golpeas a un enemigo
                    //Le manda parametros a la clase Bullet (int xPosition, int yPosition, int diameter, Color color)
                    bullet3 = new Bullet3(0, 0, 0, null);
                    newBulletCanFire = true;
                    ColisionesBalas(index);        
                }
            }
            // Comprueba si hay colisiones con escudo y balas.
            for (int index = 0; index < shieldList.size(); index++) {
                //Se lo manda a la Clase GameObject
                if (bullet3.Colisionando(shieldList.get(index))){
                    bullet3 = new Bullet3(0, 0, 0, null);
                    ColisionesEscudo(index);
                }
            }
        }
//--------// Mover bala 4
        if (bullet4 != null) {
            bullet4.setYPosition(bullet4.getYPosition() - 15);
            if (bullet4.getYPosition() < 0) {
                newBulletCanFire = true;
            }
            // Comprueba si hay colisiones con enemigos normales.
            for (int index = 0; index < enemyList.size(); index++) {
                //Se lo manda a la clase en GameObject para verificar si esta colisionando
                if (bullet4.Colisionando(enemyList.get(index))) {
                    hitSoundAudio.play(); // Reproduce un sonido de hitmarker si golpeas a un enemigo
                    //Le manda parametros a la clase Bullet (int xPosition, int yPosition, int diameter, Color color)
                    bullet4 = new Bullet4(0, 0, 0, null);
                    newBulletCanFire = true;
                    ColisionesBalas(index);        
                }
            }
            // Comprueba si hay colisiones con escudo y balas.
            for (int index = 0; index < shieldList.size(); index++) {
                //Se lo manda a la Clase GameObject
                if (bullet4.Colisionando(shieldList.get(index))){
                    bullet4 = new Bullet4(0, 0, 0, null);
                    ColisionesEscudo(index);
                }
            }
        }
        
 //--------// Mover bala 5
        if (bullet5 != null) {
            bullet5.setYPosition(bullet5.getYPosition() - 15);
            if (bullet5.getYPosition() < 0) {
                newBulletCanFire = true;
            }
            // Comprueba si hay colisiones con enemigos normales.
            for (int index = 0; index < enemyList.size(); index++) {
                //Se lo manda a la clase en GameObject para verificar si esta colisionando
                if (bullet5.Colisionando(enemyList.get(index))) {
                    hitSoundAudio.play(); // Reproduce un sonido de hitmarker si golpeas a un enemigo
                    //Le manda parametros a la clase Bullet (int xPosition, int yPosition, int diameter, Color color)
                    bullet5 = new Bullet5(0, 0, 0, null);
                    newBulletCanFire = true;
                    ColisionesBalas(index);        
                }
            }
            // Comprueba si hay colisiones con escudo y balas.
            for (int index = 0; index < shieldList.size(); index++) {
                //Se lo manda a la Clase GameObject
                if (bullet5.Colisionando(shieldList.get(index))){
                    bullet5 = new Bullet5(0, 0, 0, null);
                    ColisionesEscudo(index);
                }
            }
        }       

//--------// Mover bala 6
        if (bullet6 != null) {
            bullet6.setYPosition(bullet6.getYPosition() - 15);
            if (bullet6.getYPosition() < 0) {
                newBulletCanFire = true;
            }
            // Comprueba si hay colisiones con enemigos normales.
            for (int index = 0; index < enemyList.size(); index++) {
                //Se lo manda a la clase en GameObject para verificar si esta colisionando
                if (bullet6.Colisionando(enemyList.get(index))) {
                    hitSoundAudio.play(); // Reproduce un sonido de hitmarker si golpeas a un enemigo
                    //Le manda parametros a la clase Bullet (int xPosition, int yPosition, int diameter, Color color)
                    bullet6 = new Bullet6(0, 0, 0, null);
                    newBulletCanFire = true;
                    ColisionesBalas(index);        
                }
            }
            // Comprueba si hay colisiones con escudo y balas.
            for (int index = 0; index < shieldList.size(); index++) {
                //Se lo manda a la Clase GameObject
                if (bullet6.Colisionando(shieldList.get(index))){
                    bullet6 = new Bullet6(0, 0, 0, null);
                    ColisionesEscudo(index);
                }
            }
        }       
        
//--------// Mover bala 7
        if (bullet7 != null) {
            bullet7.setYPosition(bullet7.getYPosition() - 15);
            if (bullet7.getYPosition() < 0) {
                newBulletCanFire = true;
            }
            // Comprueba si hay colisiones con enemigos normales.
            for (int index = 0; index < enemyList.size(); index++) {
                //Se lo manda a la clase en GameObject para verificar si esta colisionando
                if (bullet7.Colisionando(enemyList.get(index))) {
                    hitSoundAudio.play(); // Reproduce un sonido de hitmarker si golpeas a un enemigo
                    //Le manda parametros a la clase Bullet (int xPosition, int yPosition, int diameter, Color color)
                    bullet7 = new Bullet7(0, 0, 0, null);
                    newBulletCanFire = true;
                    ColisionesBalas(index);        
                }
            }
            // Comprueba si hay colisiones con escudo y balas.
            for (int index = 0; index < shieldList.size(); index++) {
                //Se lo manda a la Clase GameObject
                if (bullet7.Colisionando(shieldList.get(index))){
                    bullet7 = new Bullet7(0, 0, 0, null);
                    ColisionesEscudo(index);
                }
            }
        }       
        
        
        // Mueve el enemigo de bonificación
        if (!bonusEnemyList.isEmpty()) {
            for (int index = 0; index < bonusEnemyList.size(); index++) {
                bonusEnemyList.get(index).setXPosition(bonusEnemyList.get(index).getXPosition() + (2));
                if (bonusEnemyList.get(index).getXPosition() > 950) {
                    bonusEnemyList.remove(index);
                    newBonusEnemy = true;
                }
            }
            // bonus enemigo y colisión de balas
            for (int index = 0; index < bonusEnemyList.size(); index++) {
                if (bullet != null) {
                    //Mandamos parametros a la Clase GameObject
                    if (bonusEnemyList.get(index).Colisionando(bullet)) {
                        bonusEnemyList.remove(index);
                        bullet = new Bullet(0, 0, 0, null);
                        newBulletCanFire = true;
                        newBonusEnemy = true;
                        bonusSoundAudio.play(); // Reproduce sonido si el jugador golpea a un enemigo adicional
                        score += 5000; // agrega bonificación para anotar al golpear
                    }
                }
            }
        }

        // Mueve rayos en niveles normales
        if (level%3 != 0) {
            if (beam != null) {
                for (int index = 0; index < beamList.size(); index++) {
                    beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (4));
                    if (beamList.get(index).getYPosition() > 800) {
                        beamList.remove(index);
                    }
                }
            }
        }
        // Mueve rayos a una velocidad más rápida para el jefe
        if (level%3 == 0) {
            if (beam != null) {
                for (int index = 0; index < beamList.size(); index++) {
                    beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (5)); // La velocidad del rayo del jefe aumentará en cada nivel
                    if (beamList.get(index).getYPosition() > 800) {
                        beamList.remove(index);
                    }
                }
            }
        }

        // Comprueba si hay colisiones entre haces y escudos
        try {
            for (int j = 0; j < shieldList.size(); j++) {
                for (int index = 0; index < beamList.size(); index++) {
                    // Se manda a llamar la Clase GameObject 
                    if (beamList.get(index).Colisionando(shieldList.get(j))) {
                        // FUERTE
                        if (shieldList.get(j).getColor() == Color.RED) {
                            shieldList.get(j).setColor(Color.ORANGE);
                            shieldSoundAudio.play(); // Plays sound if shield takes damage
                            beamList.remove(index);
                        // BIEN
                        } else if (shieldList.get(j).getColor() == Color.ORANGE) {
                            shieldList.get(j).setColor(Color.YELLOW);
                            shieldSoundAudio.play();
                            beamList.remove(index);
                        // OKAY
                        } else if (shieldList.get(j).getColor() == Color.YELLOW) {
                            shieldList.get(j).setColor(Color.WHITE);
                            shieldSoundAudio.play();
                            beamList.remove(index);
                        // DEBIL, BREAKS ON HIT
                        } else if (shieldList.get(j).getColor() == Color.WHITE) {
                            shieldList.remove(j);
                            shieldSoundAudio.play();
                            beamList.remove(index);
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }

        // Comprueba si hay colisiones entre balas y jugadores
        for (int index = 0; index < beamList.size(); index++) {
            //Se manda a llamar la Clase GameObject
            if (beamList.get(index).Colisionando(NaveJugador)) {
                beamList.remove(index);
                damageSoundAudio.play(); // Reproduce sonido de daño
                lifeList.remove(lifeList.size() - 1); // Elimina la vida si es alcanzado por una bala
            }
        }

        // Acelera el disparo del rayo permitiendo solo que se disparen nuevos rayos una vez que todos los rayos antiguos están fuera de la pantalla o han chocado
        if (beamList.isEmpty()) {
            newBeamCanFire = true;
        }

        /// Destruye escudos si los alienígenas chocan con ellos
        for (int input = 0; input < enemyList.size(); input++) {
            for (int j = 0; j < shieldList.size(); j++) {
                //Se manda a llamar la Clase GameObject
                if (enemyList.get(input).Colisionando(shieldList.get(j))) {
                    shieldList.remove(j);
                }
            }
            // Si los extraterrestres superan esta posición X, restableces el nivel y pierdes una vida.
            if (enemyList.get(input).getYPosition() + 50 >= 600) {
                enemyList.clear();
                shieldList.clear();
                lifeList.clear();
                beamList.clear();
                ElementoList.clear();
                bossHealth = 30;
                numberOfLives -= 1;
                deathSoundAudio.play(); // Reproduce un sonido de muerte cuando los enemigos llegan al fondo
                ConfigurarJuego();
            }
        }

        // Actualiza la pantalla del contador de vida
        if (NaveJugador.estaColisionando) {
            int index = lifeList.size() - 1;
            lifeList.remove(index);
        } 
        
        // Termina el juego si el jugador se queda sin vidas
        else if (lifeList.isEmpty()) {
            deathSoundAudio.play(); // Reproduce el sonido de la muerte cuando te quedas sin vidas
            // Le da al jugador la opción de volver a jugar o salir
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Te gustaría jugar de nuevo?", "Tu perdiste el juego con " + score + " puntos", 0);
            // Si eligen jugar de nuevo, esto reinicia todos los elementos del juego.
            if (respuesta == 0) {
                ResetearValores();
                ConfigurarJuego();
            }
            // Si eligen no volver a jugar, se cierra el juego.
            if (respuesta == 1) {
                Controlador obj = new Controlador();
                obj.setUsername(FrmNombre.nombre);
                obj.setScoreU(score);
                obj.guardarScore();
                GameOver fin = new GameOver();
                fin.AsignarScore(score);
                fin.setVisible(true);
            }
        }
        

        // Pasa al siguiente nivel, restablece todas las listas, 
        // configura todos los contadores a los valores correctos
        if (enemyList.isEmpty()){
            beamList.clear();
            shieldList.clear();
            ElementoList.clear();
            if(level%3 == 0) bonusEnemyList.clear();
            lifeList.clear();
            level += 1;
            bossHealth = 30;
            ConfigurarJuego();
            levelUpSoundAudio.play(); // Plays level up sound 
        }

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PANEL DE JUEGO    
    
    public GamePanel() {
    }
    
    public void ResetearValores(){
        lifeList.clear();
        enemyList.clear();
        shieldList.clear();
        beamList.clear();
        bonusEnemyList.clear();
        ElementoList.clear();
        score = 0;
        level = 1;
        bossHealth = 45;
        numberOfLives = 3;
        CantidadBalas = 0;
        newBulletCanFire = true;
        newBeamCanFire = true;
        newBonusEnemy = true;
        Quizz.ReiniciarQuizz();
                
        for(int i = 1; i <= 11; i++){
            CantidadElemento[i] = 0;
        }
    }
        
    public void IniciarJuego(){
        // Establecer el tamaño del Panel
        this.setSize(AnchoJuego, AlturaJuego);
        this.setPreferredSize(new Dimension(AnchoJuego, AlturaJuego));
        this.setBackground(Color.BLACK);

        // Registrar KeyboardController como KeyListener
        controladores = new KeyboardController();
        this.addKeyListener(controladores);

        // Llama a setupGame para inicializar campos
        ResetearValores();
        ConfigurarJuego();
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
      * Método para iniciar el temporizador que impulsa la animación del juego.
      * No es necesario que modifique este código a menos que sea necesario para
      * agregar alguna funcionalidad.
    **/
    public void start() {
                
        // Configure un nuevo temporizador para que se repita cada 20 milisegundos (50 FPS)
        gameTimer = new Timer(100 / Fotogramaporsegundo, new ActionListener() {

            // Realiza un seguimiento del número de fotogramas que se han producido.
            // Puede ser útil para limitar las tasas de acción
            private int frameNumber = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualiza el estado del juego y repinta la pantalla
                ActualizarEstadoJuego(frameNumber++);
                revalidate();
                repaint();
            }
        });
        Timer gameTimerHitMarker = new Timer(100, new ActionListener() {

            // Realiza un seguimiento del número de fotogramas que se han producido.
            // Puede ser útil para limitar las tasas de acción
            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualiza el estado del juego y repinta la pantalla
                hitMarker = false;
                revalidate();
                repaint();
            }
        });
        revalidate();
        repaint();
        gameTimer.setRepeats(true);
        gameTimer.start();
        gameTimerHitMarker.setRepeats(true);
        gameTimerHitMarker.start();

    }
    
    public void stop() {
        gameTimer.stop();
    }
}
