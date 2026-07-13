package Game;

import Controlador.KeyboardController;
import Tipografia.Fuente;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import persistence.RunEntry;
import persistence.ScoreService;

/**
 *
 * @author Lenny, Cesar, Arítides. Miguel
 */

public class GamePanel extends JPanel {
    
    // Componentes necesarios. ¡No quitar! 
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
    private final ScoreService scoreService = ScoreService.getInstance();

    //Agregamos objectos de las clases
    private Ship NaveJugador; //Creamos objeto de de la Clase Ship
    private Ship singleLife; //Creamos objeto de la vida que tendra la nave de la Clase Ship 
    private Ship bonusEnemy; //
    private Enemy enemy; //Creamos objeto de la Clase Enemy
    private Shield shield;
//----------------------------------------------------
    private final java.util.List<Projectile> projectiles = new ArrayList<>();
    private LevelManager levelManager;
    private final GameLoop gameLoop = new GameLoop(Fotogramaporsegundo);
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
        gameLoop.resume();
        score+= quizz.bonus;
        quizz.bonus = 0;
        quizz.valor = 0;
    }

    public void PausarJuego(){
        gameLoop.pause();
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
        if (levelManager == null) {
            levelManager = new LevelManager(Dificultad);
        }

        // Panel de victoria si gana
        if (levelManager.isVictory(level)) {
            saveCurrentScore(true);
            Victoria vic = new Victoria();
            vic.AsignarScore(score);
            vic.setVisible(true);
            return;
        }

        if (level % 3 <= 1) {
            MusicaLevel();
        }

        if (levelManager.isBossLevel(level)) {
            bossSoundAudio.play();
        }

        enemyList.clear();
        enemyList.addAll(levelManager.createEnemies(level));

        controladores.resetController();
        NaveJugador = levelManager.createPlayer(controladores);

        lifeList.clear();
        lifeList.addAll(levelManager.createLifeIcons(numberOfLives));

        shieldList.clear();
        shieldList.addAll(levelManager.createShields());

        projectiles.clear();
        newBulletCanFire = true;
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
        PowerUpSystem.applyPlayerSpeed(NaveJugador, Velocidad);
    }
    
    
    public void PowerUpBalas(){
        projectiles.clear();
        projectiles.addAll(PowerUpSystem.createBurst(NaveJugador, CantidadBalas));
        newBulletCanFire = false;
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
        // Marcador de impacto
        if (hitMarker) {
            g.setColor(Color.WHITE);
            if (level%3 != 0) {
                g.drawString("+ 100", markerX + 20, markerY -= 1);
            } else {
                g.drawString("- 1", markerX + 75, markerY += 1);
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
        
        // Dibuja proyectiles del jugador
        for (Projectile projectile : projectiles) {
            if (projectile != null) {
                projectile.draw(g);
            }
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
        CollisionSystem.degradeShield(shieldList, index);
        shieldSoundAudio.play();
        newBulletCanFire = true;
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ACTUALIZAR ESTADO DEL JUEGO
    
    public void ActualizarEstadoJuego(int frameNumber) {
        //MusicaLevel();
        // Permite al jugador moverse hacia la izquierda y hacia la derecha
        PowerUpVelocidad();

        // High score en memoria (sin I/O por frame)
        if (score > highScore) {
            highScore = score;
        }

        // Agrega la opción para restablecer el puntaje alto
        if (controladores.getKeyStatus(82)) { // KEYSTATUS(82) es la tecla R según el código ASCII
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Te gustaría reiniciar el Highscore?", ":)", 0);
            controladores.resetController();
            if (respuesta == 0) {
                scoreService.clearHighScores();
                highScore = 0;
            }
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

        // Proyectiles del jugador (movimiento + colisiones)
        newBulletCanFire = CollisionSystem.updatePlayerProjectiles(
                projectiles, enemyList, shieldList, bonusEnemyList,
                new CollisionSystem.Listener() {
                    @Override
                    public void onEnemyHit(int enemyIndex) {
                        hitSoundAudio.play();
                        ColisionesBalas(enemyIndex);
                    }
                    @Override
                    public void onShieldHit(int shieldIndex) {
                        ColisionesEscudo(shieldIndex);
                    }
                    @Override
                    public void onBonusHit(int bonusIndex) {
                        if (bonusIndex >= 0 && bonusIndex < bonusEnemyList.size()) {
                            bonusEnemyList.remove(bonusIndex);
                            newBonusEnemy = true;
                            bonusSoundAudio.play();
                            score += 5000;
                        }
                    }
                });

        // Mueve el enemigo de bonificación
        if (!bonusEnemyList.isEmpty()) {
            for (int index = 0; index < bonusEnemyList.size(); index++) {
                bonusEnemyList.get(index).setXPosition(bonusEnemyList.get(index).getXPosition() + (2));
                if (bonusEnemyList.get(index).getXPosition() > 950) {
                    bonusEnemyList.remove(index);
                    newBonusEnemy = true;
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
                projectiles.clear();
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
            saveCurrentScore(false);
            // Le da al jugador la opción de volver a jugar o salir
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Te gustaría jugar de nuevo?", "Tu perdiste el juego con " + score + " puntos", 0);
            // Si eligen jugar de nuevo, esto reinicia todos los elementos del juego.
            if (respuesta == 0) {
                ResetearValores();
                ConfigurarJuego();
            }
            // Si eligen no volver a jugar, se cierra el juego.
            if (respuesta == 1) {
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
        projectiles.clear();
        score = 0;
        level = 1;
        bossHealth = 45;
        numberOfLives = 3;
        CantidadBalas = 0;
        Velocidad = 0;
        newBulletCanFire = true;
        newBeamCanFire = true;
        newBonusEnemy = true;
        Quizz.ReiniciarQuizz();
                
        for(int i = 1; i <= 11; i++){
            CantidadElemento[i] = 0;
        }
        highScore = scoreService.bestScore();
    }

    private void saveCurrentScore(boolean won) {
        String username = FrmNombre.nombre;
        if (username == null || username.isBlank()) {
            username = "Player";
        }
        int difficulty = RunEntry.DIFFICULTY_EASY;
        try {
            difficulty = Dificultad.getTipoDificultad();
        } catch (Exception ignored) {
            // Configuracion puede fallar fuera de UI; default fácil
        }
        try {
            scoreService.saveRun(new RunEntry(username, score, level, difficulty, won));
            highScore = Math.max(highScore, scoreService.bestScore());
        } catch (IllegalArgumentException ex) {
            scoreService.saveRun(new RunEntry("Player", score, level, difficulty, won));
            highScore = Math.max(highScore, score);
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
        highScore = scoreService.bestScore();
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
        final int[] frameNumber = {0};
        gameLoop.start(
                e -> {
                    ActualizarEstadoJuego(frameNumber[0]++);
                    revalidate();
                    repaint();
                },
                e -> {
                    hitMarker = false;
                    revalidate();
                    repaint();
                });
        revalidate();
        repaint();
    }
    
    public void stop() {
        gameLoop.stop();
    }
}
