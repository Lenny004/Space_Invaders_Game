package Game;

import Controlador.KeyboardController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import sun.audio.*;
import java.io.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Lenny, Cesar, Arítides. Miguel
 */
public class GamePanel extends JPanel {

    // Componentes necesarios. ¡No quitar! 
    private Timer gameTimer;
    //
    private KeyboardController controladores;
    // Controla el tamaño de la ventana del juego y la velocidad de fotogramas. 
    private final int AnchoJuego = 1000;
    private final int AlturaJuego = 675;
    private final int Fotogramaporsegundo = 20;

    // Iniciar contadores
    Random randomDisparosE = new Random();
    private int score = 0;
    private int level = 14;
    private int numberOfLives = 3;
    private int highScore;
    private int markerX, markerY;
    private static int bossHealth = 30;
    File archivoPuntaje = new File("Highscore.txt");

    //Agregamos objectos de las clases
    private Ship NaveJugador; //Creamos objeto de de la Clase Ship
    private Ship singleLife; //Creamos objeto de la vida que tendra la nave de la Clase Ship 
    private Ship bonusEnemy;
    private Enemy enemy;
    private Shield shield;
    private Bullet bullet;
    private Beam beam, beam2, beam3;

    // Agregamos Booleans
    private boolean newBulletCanFire = true;
    private boolean newBeamCanFire = true;
    private boolean newBonusEnemy = true;
    private boolean hitMarker = false;

    // Agregamos Array Lists
    private ArrayList<Ship> lifeList = new ArrayList();//Lista de vida
    private ArrayList<Ship> bonusEnemyList = new ArrayList();//Lista de Naves enemigas
    private ArrayList<Enemy> enemyList = new ArrayList();//Lista de enemigos
    private ArrayList<Shield> shieldList = new ArrayList();//Lista de los escudos
    private ArrayList<Beam> beamList = new ArrayList();
    private ImageIcon background = new ImageIcon(getClass().getResource("/imagenes/Espacio.gif"));

    // Se agregaron archivos de audio y transmisiones
    private File beamSound = new File("src/Sonidos/alienBeam.wav");
    private File bulletSound = new File("src/Sonidos/bulletSound.wav");
    private File levelUpSound = new File("src/Sonidos/levelUpSound.wav");
    private File deathSound = new File("src/Sonidos/deathSound.wav");
    private File hitmarkerSound = new File("src/Sonidos/hitmarkerSound.wav");
    private File shieldSound = new File("src/Sonidos/shieldSound.wav");
    private File bossSound = new File("src/Sonidos/bossSound.wav");
    private File bonusSound = new File("src/Sonidos/bonusSound.wav");
    private File damageSound = new File("src/Sonidos/damageSound.wav");
    private AudioStream beamSoundAudio;
    private InputStream beamSoundInput;
    private AudioStream bulletSoundAudio;
    private InputStream bulletSoundInput;
    private AudioStream levelUpSoundAudio;
    private InputStream levelUpSoundInput;
    private AudioStream deathSoundAudio;
    private InputStream deathSoundInput;
    private AudioStream hitSoundAudio;//Sonido de impacto
    private InputStream hitSoundInput;
    private AudioStream shieldSoundAudio;
    private InputStream shieldSoundInput;
    private AudioStream bossSoundAudio;
    private InputStream bossSoundInput;
    private AudioStream bonusSoundAudio;
    private InputStream bonusSoundInput;
    private AudioStream damageSoundAudio;
    private InputStream damageSoundInput;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Metodos extra
    
    // Usado en la clase Enemy para ayudar con el método de dibujo para el jefe
    public static int getBossHealth() {
        return bossHealth;
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURAR JUEGO

    public final void ConfigurarJuego() {

        // Establece enemigos para niveles normales
        if (level%3 != 0) {
            // 6 Filas
            for (int row = 0; row < 6; row++) {
                // 5 Columnas
                for (int column = 0; column < 5; column++) {
                    //Lo mandamos a la clase Enemy con los siguientes parametros "(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height, int level)"
                    enemy = new Enemy((20 + (row * 100)), (20 + (column * 60)), 5, 0, column, null, 40, 40, level); // La velocidad del enemigo aumentará en cada nivel
                    enemyList.add(enemy);
                }
            }
        }
        else{
            // Establece enemigo para los niveles de jefe
            AudioPlayer.player.start(bossSoundAudio); // Reproduce el rugido del jefe
            enemy = new Enemy(20, 20, 3, 0, 100, null, 150, 150, level);
            enemyList.add(enemy);
        }
        
        // Restablece todos los movimientos del controlador con "resetController"
        controladores.resetController();

        // Establece los valores de la nave del jugador
        // Le enviamos los siguientes parametros a la Clase Ship (int xPosition, int yPosition, Color color, KeyboardController control)
        NaveJugador = new Ship(375, 600, null, controladores);

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
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PAINT
    @Override
    public void paint(Graphics g) {//Clase paint del Methodo Graphics 

        // Configuración de imagen de fondo del juego
        
        //"paintIcon" Una implementación de la interfaz de iconos que pinta iconos a partir de imágenes. 
        //Las imágenes que se crean a partir de una URL,
        //bytes se cargan previamente con MediaTracker para monitorear el estado de carga de la imagen.
        background.paintIcon(this, g, WIDTH, WIDTH);

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

        // Dibuja una viñeta en la barra espaciadora presiona
        if (controladores.getKeyStatus(32)) { //KEYSTATUS(32) es la barra de espacio según el código ASCII
            if (newBulletCanFire) {
                bullet = new Bullet(NaveJugador.getXPosition() + 22, NaveJugador.getYPosition() - 20, 0, Color.RED);
                AudioPlayer.player.start(bulletSoundAudio); // Plays bullet sound
                newBulletCanFire = false;
            }
        }
        // Si intenta sacar la bala después de presionar una tecla
        if (bullet != null) {
            bullet.draw(g);
        }

        // Genera rayos aleatorios disparados por enemigos.
        if (level%3 != 0) {
            if (newBeamCanFire) {
                for (int index = 0; index < enemyList.size(); index++) {
                    if (randomDisparosE.nextInt(30) == index) {
                        beam = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition(), 0, Color.YELLOW);
                        beamList.add(beam);
                        AudioPlayer.player.start(beamSoundAudio); // Reproduce el sonido del rayo para enemigos normales.
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
                        AudioPlayer.player.start(beamSoundAudio); // Reproduce sonido de haz para el jefe
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
        if (newBonusEnemy) {
            if (randomDisparosE.nextInt(3000) == 1500) {
                bonusEnemy = new Ship(-50, 30, Color.RED, null);
                bonusEnemyList.add(bonusEnemy);
                newBonusEnemy = false;
            }
        }
        // Atrae enemigo adicional
        for (int index = 0; index < bonusEnemyList.size(); index++) {
            bonusEnemyList.get(index).bonusDraw(g);
        }

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
        g.drawString("Highscore: " + highScore, 440, 20);

        // Dibuja una pantalla de salud para el nivel de jefe
        if (level%3 == 0) {
            g.setColor(Color.WHITE);
            g.drawString("Boss Health: " + bossHealth, 500, 600);
        }
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ACTUALIZAR ESTADO DEL JUEGO
    
    public void ActualizarEstadoJuego(int frameNumber) {

        // Permite al jugador moverse hacia la izquierda y hacia la derecha
        NaveJugador.move();

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
        if ((enemyList.get(enemyList.size() - 1).getXPosition() + enemyList.get(enemyList.size() - 1).getXVelocity()) > 760 || (enemyList.get(0).getXPosition() + enemyList.get(0).getXVelocity()) < 0) {
            for (int index = 0; index < enemyList.size(); index++) {
                enemyList.get(index).setXVelocity(enemyList.get(index).getXVelocity() * -1);
                enemyList.get(index).setYPosition(enemyList.get(index).getYPosition() + 10);
            }
        } else {
            for (int index = 0; index < enemyList.size(); index++) {
                enemyList.get(index).move();
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
                    AudioPlayer.player.start(hitSoundAudio); // Reproduce un sonido de hitmarker si golpeas a un enemigo
                    //Le manda parametros a la clase Bullet (int xPosition, int yPosition, int diameter, Color color)
                    bullet = new Bullet(0, 0, 0, null);
                    newBulletCanFire = true;
                    // Puntaje de actualizaciones para niveles normales
                    if (level%3 != 0) {
                        score += 100;
                        hitMarker = true;
                        markerX = enemyList.get(index).getXPosition(); // Gets positions that the "+ 100" spawns off of
                        markerY = enemyList.get(index).getYPosition();
                        enemyList.remove(index);

                    }
                    // Actualiza la puntuación para los niveles de jefe.
                    if (level%3 == 0) {
                        hitMarker = true;
                        markerX = enemyList.get(index).getXPosition(); // Gets positions that the "- 1" spawns off of
                        markerY = enemyList.get(index).getYPosition() + 165;
                        bossHealth -= 1;
                        if (bossHealth == 0) {
                            enemyList.remove(index);
                            score += 9000;// Bonus score for defeating boss
                        }
                    }
                }
            }

            // Comprueba si hay colisiones con escudo y balas.
            for (int index = 0; index < shieldList.size(); index++) {
                //Se lo manda a la Clase GameObject
                if (bullet.Colisionando(shieldList.get(index))){
                    // Cada declaración if cambia el color del escudo, lo que indica "fuerza"
                    // FUERTE
                    if (shieldList.get(index).getColor() == Color.RED) {
                        shieldList.get(index).setColor(Color.ORANGE);
                        AudioPlayer.player.start(shieldSoundAudio); // Plays sound if shield takes damage
                        bullet = new Bullet(0, 0, 0, null);
                        newBulletCanFire = true;
                    // BIEN
                    } else if (shieldList.get(index).getColor() == Color.ORANGE) {
                        shieldList.get(index).setColor(Color.YELLOW);
                        AudioPlayer.player.start(shieldSoundAudio);
                        bullet = new Bullet(0, 0, 0, null);
                        newBulletCanFire = true;
                    // OKAY
                    } else if (shieldList.get(index).getColor() == Color.YELLOW) {
                        shieldList.get(index).setColor(Color.WHITE);
                        AudioPlayer.player.start(shieldSoundAudio);
                        bullet = new Bullet(0, 0, 0, null);
                        newBulletCanFire = true;
                    // DÉBIL, SE ROMPE AL GOLPEAR
                    } else if (shieldList.get(index).getColor() == Color.WHITE) {
                        shieldList.remove(index);
                        AudioPlayer.player.start(shieldSoundAudio);
                        bullet = new Bullet(0, 0, 0, null);
                        newBulletCanFire = true;
                    }
                }
            }
        }
        // Moves bonus enemy
        if (!bonusEnemyList.isEmpty()) {
            for (int index = 0; index < bonusEnemyList.size(); index++) {
                bonusEnemyList.get(index).setXPosition(bonusEnemyList.get(index).getXPosition() + (2));
                if (bonusEnemyList.get(index).getXPosition() > 800) {
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
                        AudioPlayer.player.start(bonusSoundAudio); // Plays sound if player hits a bonus enemy
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
                        // STRONG
                        if (shieldList.get(j).getColor() == Color.RED) {
                            shieldList.get(j).setColor(Color.ORANGE);
                            AudioPlayer.player.start(shieldSoundAudio); // Plays sound if shield takes damage
                            beamList.remove(index);
                        // GOOD
                        } else if (shieldList.get(j).getColor() == Color.ORANGE) {
                            shieldList.get(j).setColor(Color.YELLOW);
                            AudioPlayer.player.start(shieldSoundAudio);
                            beamList.remove(index);
                        // OKAY
                        } else if (shieldList.get(j).getColor() == Color.YELLOW) {
                            shieldList.get(j).setColor(Color.WHITE);
                            AudioPlayer.player.start(shieldSoundAudio);
                            beamList.remove(index);
                        // WEAK, BREAKS ON HIT
                        } else if (shieldList.get(j).getColor() == Color.WHITE) {
                            shieldList.remove(j);
                            AudioPlayer.player.start(shieldSoundAudio);
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
                AudioPlayer.player.start(damageSoundAudio); // Plays damage sound
                lifeList.remove(lifeList.size() - 1); // Removes life if hit by bullet
            }
        }

        // Acelera el disparo del rayo permitiendo solo que se disparen nuevos rayos una vez que todos los rayos antiguos están fuera de la pantalla o han chocado
        if (beamList.isEmpty()) {
            newBeamCanFire = true;
        }

        //Destroys shields if aliens collide with them
        for (int input = 0; input < enemyList.size(); input++) {
            for (int j = 0; j < shieldList.size(); j++) {
                //Se manda a llamar la Clase GameObject
                if (enemyList.get(input).Colisionando(shieldList.get(j))) {
                    shieldList.remove(j);
                }
            }
            // If aliens exceed this X Position, you reset the level and lose a life
            if (enemyList.get(input).getYPosition() + 50 >= 675) {
                enemyList.clear();
                shieldList.clear();
                lifeList.clear();
                beamList.clear();
                bossHealth = 30;
                numberOfLives -= 1;
                AudioPlayer.player.start(deathSoundAudio); // Plays death sound when enemies reach bottom
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
            AudioPlayer.player.start(deathSoundAudio); // Reproduce el sonido de la muerte cuando te quedas sin vidas
            // Le da al jugador la opción de volver a jugar o salir
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Te gustaría jugar de nuevo?", "Tu perdiste el juego con " + score + " puntos", 0);
            // Si eligen jugar de nuevo, esto reinicia todos los elementos del juego.
            if (respuesta == 0) {
                lifeList.clear();
                enemyList.clear();
                shieldList.clear();
                beamList.clear();
                bonusEnemyList.clear();
                score = 0;
                level = 1;
                bossHealth = 30;
                numberOfLives = 3;
                newBulletCanFire = true;
                newBeamCanFire = true;
                newBonusEnemy = true;
                ConfigurarJuego();
            }
            // Si eligen no volver a jugar, se cierra el juego.
            if (respuesta == 1) {
                System.exit(0);
            }
        }

        // Pasa al siguiente nivel, restablece todas las listas, 
        // configura todos los contadores a los valores correctos
        if (enemyList.isEmpty()){
            beamList.clear();
            shieldList.clear();
            bonusEnemyList.clear();
            lifeList.clear();
            level += 1;
            bossHealth = 30;
            ConfigurarJuego();
            AudioPlayer.player.start(levelUpSoundAudio); // Plays level up sound
        }
        
        // Todas las transmisiones necesarias para cada sonido del juego
        try {
            beamSoundInput = new FileInputStream(beamSound);
            beamSoundAudio = new AudioStream(beamSoundInput);
            bulletSoundInput = new FileInputStream(bulletSound);
            bulletSoundAudio = new AudioStream(bulletSoundInput);
            levelUpSoundInput = new FileInputStream(levelUpSound);
            levelUpSoundAudio = new AudioStream(levelUpSoundInput);
            deathSoundInput = new FileInputStream(deathSound);
            deathSoundAudio = new AudioStream(deathSoundInput);
            hitSoundInput = new FileInputStream(hitmarkerSound);
            hitSoundAudio = new AudioStream(hitSoundInput);
            shieldSoundInput = new FileInputStream(shieldSound);
            shieldSoundAudio = new AudioStream(shieldSoundInput);
            bossSoundInput = new FileInputStream(bossSound);
            bossSoundAudio = new AudioStream(bossSoundInput);
            bonusSoundInput = new FileInputStream(bonusSound);
            bonusSoundAudio = new AudioStream(bonusSoundInput);
            damageSoundInput = new FileInputStream(damageSound);
            damageSoundAudio = new AudioStream(damageSoundInput);
        } catch (IOException e) {
            
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GAME PANEL    
    
    public GamePanel() {
        // Set the size of the Panel
        this.setSize(AnchoJuego, AlturaJuego);
        this.setPreferredSize(new Dimension(AnchoJuego, AlturaJuego));
        this.setBackground(Color.BLACK);

        // Registrar KeyboardController como KeyListener
        controladores = new KeyboardController();
        this.addKeyListener(controladores);

        // Llama a setupGame para inicializar campos
        this.ConfigurarJuego();
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

}
