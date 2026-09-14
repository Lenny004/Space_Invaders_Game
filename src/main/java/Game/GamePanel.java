package Game;

import Controlador.KeyboardController;
import Tipografia.Fuente;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import persistence.RunEntry;
import persistence.ScoreService;

/**
 *
 * @author Lenny
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
    private final CraftingSystem.Flags craftFlags = new CraftingSystem.Flags();
    private String craftFeedback;
    private int craftFeedbackTicks;
    private boolean pauseMenuOpen;
    private boolean craftOverlayOpen;
    private boolean tutorialMode;
    private boolean endlessMode;
    private TutorialController tutorial;
    private final ComboSystem comboSystem = new ComboSystem();
    private final TemporaryBuffSystem temporaryBuffs = new TemporaryBuffSystem();
    private final ArrayList<BuffDrop> buffDropList = new ArrayList<>();
    private int bossMaxHealth = GameBalance.BOSS_HEALTH;
    private boolean desperateMinionsSpawned;
    private int lastHitScore;
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
    private int bossHealth = GameBalance.BOSS_HEALTH;
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
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Metodos extra
    
    // Usado en la clase Enemy para ayudar con el método de dibujo para el jefe
    public int getBossHealth() {
        return bossHealth;
    }
    
    public void ReanudarJuego(){
        pauseMenuOpen = false;
        if (controladores != null) {
            controladores.resetController();
        }
        gameLoop.resume();
        requestFocusInWindow();
    }

    public void PausarJuego(){
        gameLoop.pause();
    }

    public void setTutorialMode(boolean enabled) {
        this.tutorialMode = enabled;
        if (!enabled) {
            tutorial = null;
        }
    }

    public boolean isTutorialMode() {
        return tutorialMode;
    }

    public void setEndlessMode(boolean enabled) {
        this.endlessMode = enabled;
    }

    public boolean isEndlessMode() {
        return endlessMode;
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURAR JUEGO

    public void ConfigurarJuego() {
        Configuracion.TipoDificultad = GameConfig.getInstance().getDifficulty();
        levelManager = new LevelManager(tutorialMode ? 1 : Configuracion.TipoDificultad);

        if (tutorialMode) {
            setupTutorialLevel();
            return;
        }

        // Panel de victoria si gana (no aplica en endless)
        if (levelManager.isVictory(level, endlessMode)) {
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
            bossMaxHealth = GameBalance.bossMaxHealth(level, levelManager.getTipoDificultad());
            bossHealth = bossMaxHealth;
            desperateMinionsSpawned = false;
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

    private void setupTutorialLevel() {
        enemyList.clear();
        enemyList.addAll(levelManager.createTutorialWave());
        controladores.resetController();
        NaveJugador = levelManager.createPlayer(controladores);
        lifeList.clear();
        lifeList.addAll(levelManager.createLifeIcons(numberOfLives));
        shieldList.clear();
        shieldList.addAll(levelManager.createShields());
        projectiles.clear();
        beamList.clear();
        ElementoList.clear();
        buffDropList.clear();
        temporaryBuffs.reset();
        comboSystem.reset();
        newBulletCanFire = true;
        newBeamCanFire = true;
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
        int bulletLevel = temporaryBuffs.effectiveBulletLevel(CantidadBalas);
        projectiles.addAll(PowerUpSystem.createBurst(NaveJugador, bulletLevel));
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
            if (level % 3 != 0) {
                g.drawString("+ " + lastHitScore, markerX + 20, markerY -= 1);
            } else {
                g.drawString("-" + Math.max(1, lastHitScore), markerX + 75, markerY += 1);
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

        // Dibuja proyectiles del jugador
        for (Projectile projectile : projectiles) {
            if (projectile != null) {
                projectile.draw(g);
            }
        }
        
        // Dibuja los elementos generados
        for (int index = 0; index < ElementoList.size(); index++) {
            ElementoList.get(index).draw(g);
        }

        for (int index = 0; index < buffDropList.size(); index++) {
            buffDropList.get(index).draw(g);
        }
        
        // Dibuja los rayos generados
        for (int index = 0; index < beamList.size(); index++) {
            beamList.get(index).draw(g);
        }
        // Coloca enemigo bonus
        for (int index = 0; index < bonusEnemyList.size(); index++) {
            bonusEnemyList.get(index).bonusDraw(g);
        }

        if (craftFeedback != null && craftFeedbackTicks > 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
            g.drawString(craftFeedback, 500, 80);
        }
        
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 12));
        // Establece la visualización de la puntuación
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 260, 20);

        if (comboSystem.isActive()) {
            g.setColor(Color.ORANGE);
            g.drawString("COMBO x" + comboSystem.getMultiplier(), 260, 42);
        }

        // Configura la visualización del contador de vida
        g.setColor(Color.WHITE);
        g.drawString("Vidas:", 11, 20);
        for (int index = 0; index < lifeList.size(); index++) {
            lifeList.get(index).lifeDraw(g);
        }
        // Establece la visualización de nivel
        g.setColor(Color.WHITE);
        String levelLabel = endlessMode
                ? Messages.format("hud.endless.level", level)
                : ("Nivel " + level);
        g.drawString(levelLabel, 750, 20);
        int difficulty = levelManager != null
                ? levelManager.getTipoDificultad()
                : GameConfig.getInstance().getDifficulty();
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.PLAIN, 11));
        g.drawString(Messages.format("hud.difficulty", Messages.get(GameBalance.difficultyMessageKey(difficulty))), 750, 38);
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 12));

        // Establece la visualización de Highscore
        g.setColor(Color.WHITE);
        g.drawString("High Score: " + highScore, 440, 20);

        // Dibuja una pantalla de salud para el nivel de jefe
        if (levelManager != null && levelManager.isBossLevel(level) && !tutorialMode) {
            g.setColor(Color.WHITE);
            g.drawString("Vida del Jefe: " + bossHealth + "/" + bossMaxHealth, 440, 600);
            BossPhase phase = BossPhase.fromHealth(bossHealth, bossMaxHealth);
            g.setColor(Color.YELLOW);
            g.setFont(new Font(TipoFuente.SpaceInvaders, Font.PLAIN, 12));
            g.drawString(Messages.get("boss.phase." + phase.name().toLowerCase()), 440, 620);
        }

        if (!temporaryBuffs.activeLabels().isEmpty()) {
            g.setColor(new Color(80, 200, 255));
            g.setFont(new Font(TipoFuente.SpaceInvaders, Font.PLAIN, 12));
            g.drawString(Messages.get("buff.active") + " " + String.join(" ", temporaryBuffs.activeLabels()), 11, 635);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 12));
        g.drawString(Messages.get("craft.hint"), 11, 655);
        
        //G le asignamos la tipografía personalizada y comenzará a tener ese valor de este punto en adelante
        g.setColor(Color.WHITE);
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 12));
        //Mensaje Press
        g.drawString("PRESS", 1010, 20);
        
        
        //G le asignamos la tipografía personalizada y comenzará a tener ese valor de este punto en adelante
        g.setColor(Color.BLACK);
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
        
        // Atajos overlay: 1-6 disparo, Q-Y velocidad
        g.drawString("1", 1015, 43);
        g.drawString("2", 1015, 78);
        g.drawString("3", 1015, 112);
        g.drawString("4", 1015, 146);
        g.drawString("5", 1015, 180);
        g.drawString("6", 1015, 217);
        g.drawString("Q", 1015, 253);
        g.drawString("W", 1015, 288);
        g.drawString("E", 1015, 322);
        g.drawString("R", 1015, 358);
        g.drawString("T", 1015, 392);
        g.drawString("Y", 1015, 428);
        
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

        if (craftOverlayOpen) {
            drawCraftOverlay(g);
        }

        if (tutorialMode && tutorial != null) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 14));
            g.drawString(Messages.get(tutorial.bannerKey()), 180, 48);
        }
    }

    private void drawCraftOverlay(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(80, 40, 840, 580);

        g.setColor(new Color(99, 183, 217));
        g.drawRect(80, 40, 840, 580);

        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 22));
        g.setColor(Color.WHITE);
        g.drawString(Messages.get("craft.title"), 120, 80);
        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.PLAIN, 12));
        g.drawString(Messages.get("craft.close"), 120, 105);

        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 14));
        g.setColor(RojoG.brighter());
        g.drawString(Messages.get("craft.section.bullets"), 120, 140);
        g.setColor(new Color(90, 140, 220));
        g.drawString(Messages.get("craft.section.speed"), 520, 140);

        g.setFont(new Font(TipoFuente.SpaceInvaders, Font.PLAIN, 12));
        for (int i = 0; i < CraftingSystem.RECIPES.length; i++) {
            CraftingSystem.Recipe recipe = CraftingSystem.RECIPES[i];
            boolean bullets = i < 6;
            int colX = bullets ? 120 : 520;
            int row = bullets ? i : i - 6;
            int y = 170 + row * 55;

            boolean used = craftFlags.isUsed(i);
            boolean can = CraftingSystem.canCraft(recipe, i, CantidadElemento, craftFlags);

            if (used) {
                g.setColor(Color.GRAY);
            } else if (can) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(new Color(160, 160, 160));
            }

            String status = used ? Messages.get("craft.used") : ("[" + recipe.getHotkeyLabel() + "]");
            g.drawString(status + "  " + Messages.get(recipe.getNameKey()) + "  (" + recipe.getFormula() + ")", colX, y);
            g.drawString(Messages.get(recipe.getEffectKey()) + "  |  " + recipe.formatCost(), colX, y + 18);
        }

        if (craftFeedback != null && craftFeedbackTicks > 0) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font(TipoFuente.SpaceInvaders, Font.BOLD, 20));
            g.drawString(craftFeedback, 420, 90);
        }
    }
    
    public void ColisionesBalas(int index, int damage){
        if (index < 0 || index >= enemyList.size()) {
            return;
        }
        Enemy target = enemyList.get(index);
        int dmg = Math.max(1, damage);

        // Puntaje de actualizaciones para niveles normales / minions
        if (!target.isBoss()) {
            int mult = comboSystem.registerKill();
            lastHitScore = GameBalance.SCORE_ALIEN * mult;
            score += lastHitScore;
            hitMarker = true;
            markerX = target.getXPosition();
            markerY = target.getYPosition();
            enemyList.remove(index);

            if (tutorialMode || DropSystem.shouldDrop(randomElemento)) {
                spawnElementDrop(markerX, markerY);
            }
            if (!tutorialMode && TemporaryBuffSystem.shouldDrop(randomElemento)) {
                buffDropList.add(new BuffDrop(
                        markerX, markerY, TemporaryBuffSystem.rollType(randomElemento)));
            }
            maybeSpawnLifeDrop(markerX + 18, markerY, GameBalance.LIFE_DROP_CHANCE_PERCENT);
            return;
        }

        // Jefe: usa damage del proyectil
        hitMarker = true;
        markerX = target.getXPosition();
        markerY = target.getYPosition() + 165;
        lastHitScore = dmg;
        bossHealth = Math.max(0, bossHealth - dmg);
        maybeSpawnDesperateMinions();
        if (bossHealth == 0) {
            enemyList.remove(index);
            score += GameBalance.SCORE_BOSS;
            comboSystem.registerKill();
            if (DropSystem.shouldDropOnBossKill()) {
                spawnElementDrop(markerX, markerY);
            }
            maybeSpawnLifeDrop(markerX + 18, markerY, GameBalance.LIFE_DROP_CHANCE_BOSS_PERCENT);
        }
    }

    private void maybeSpawnDesperateMinions() {
        if (desperateMinionsSpawned || levelManager == null || !levelManager.isBossLevel(level)) {
            return;
        }
        if (BossPhase.fromHealth(bossHealth, bossMaxHealth) != BossPhase.DESPERATE) {
            return;
        }
        desperateMinionsSpawned = true;
        enemyList.addAll(levelManager.createBossMinions(level, findBoss()));
    }

    private Enemy findBoss() {
        for (Enemy enemy : enemyList) {
            if (enemy.isBoss()) {
                return enemy;
            }
        }
        return null;
    }

    private boolean hasLivingBoss() {
        return findBoss() != null;
    }

    private void spawnElementDrop(int x, int y) {
        int elementoDado = DropSystem.rollElement(randomElemento);
        Elemento = new ElementoDrop(x, y, elementoDado, 0, null);
        ElementoList.add(Elemento);
    }

    /** Vida extra: no tutorial, no si ya está en {@link GameBalance#MAX_LIVES}. */
    private void maybeSpawnLifeDrop(int x, int y, int chancePercent) {
        if (tutorialMode) {
            return;
        }
        if (TemporaryBuffSystem.shouldSpawnLife(randomElemento, numberOfLives, chancePercent)) {
            buffDropList.add(new BuffDrop(x, y, BuffDrop.Type.LIFE));
        }
    }
    
    public void ColisionesEscudo(int index){
        CollisionSystem.degradeShield(shieldList, index);
        shieldSoundAudio.play();
        newBulletCanFire = true;
    }

    private void spawnEnemyBeams() {
        if (tutorialMode || !newBeamCanFire || enemyList.isEmpty()) {
            return;
        }
        if (levelManager == null || !levelManager.isBossLevel(level)) {
            int chance = Math.max(1, GameBalance.normalBeamChance(
                    levelManager != null ? levelManager.getTipoDificultad() : RunEntry.DIFFICULTY_MEDIUM));
            for (int index = 0; index < enemyList.size(); index++) {
                if (enemyList.get(index).isBoss()) {
                    continue;
                }
                if (randomDisparosE.nextInt(chance) == 0) {
                    beam = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition(), 0, Color.YELLOW);
                    beamList.add(beam);
                    beamSoundAudio.play();
                }
                newBeamCanFire = false;
            }
            return;
        }

        int difficulty = levelManager.getTipoDificultad();
        int minionChance = Math.max(1, GameBalance.normalBeamChance(difficulty));
        int bossChance = Math.max(1, GameBalance.bossBeamChance(difficulty));
        for (int index = 0; index < enemyList.size(); index++) {
            Enemy enemy = enemyList.get(index);
            if (!enemy.isBoss()) {
                if (randomDisparosE.nextInt(minionChance) == 0) {
                    beamList.add(new Beam(enemy.getXPosition(), enemy.getYPosition(), 0, Color.YELLOW));
                    beamSoundAudio.play();
                    newBeamCanFire = false;
                }
                continue;
            }
            if (randomDisparosE.nextInt(bossChance) != 0) {
                newBeamCanFire = false;
                continue;
            }
            BossPhase phase = BossPhase.fromHealth(bossHealth, bossMaxHealth);
            int bx = enemy.getXPosition();
            int by = enemy.getYPosition();
            switch (phase) {
                case OPENING -> {
                    beamList.add(new Beam(bx + 75, by + 140, 0, Color.YELLOW));
                }
                case RAGE -> {
                    beamList.add(new Beam(bx + 75, by + 140, 0, Color.YELLOW));
                    beamList.add(new Beam(bx, by + 110, 0, Color.YELLOW));
                    beamList.add(new Beam(bx + 150, by + 110, 0, Color.YELLOW));
                }
                case DESPERATE -> {
                    beamList.add(new Beam(bx + 75, by + 140, 0, Color.ORANGE));
                    beamList.add(new Beam(bx, by + 110, 0, Color.ORANGE));
                    beamList.add(new Beam(bx + 150, by + 110, 0, Color.ORANGE));
                    beamList.add(new Beam(bx + 30, by + 150, 0, Color.RED));
                    beamList.add(new Beam(bx + 120, by + 150, 0, Color.RED));
                }
            }
            beamSoundAudio.play();
            newBeamCanFire = false;
        }
    }

    private void spawnBonusEnemy() {
        if (tutorialMode || !newBonusEnemy || level % 3 == 0) {
            return;
        }
        if (randomDisparosE.nextInt(GameBalance.BONUS_SPAWN_RANGE) == GameBalance.BONUS_SPAWN_HIT) {
            bonusEnemy = new Ship(-50, 30, Color.RED, null);
            bonusEnemyList.add(bonusEnemy);
            newBonusEnemy = false;
        }
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ACTUALIZAR ESTADO DEL JUEGO
    
    public void ActualizarEstadoJuego(int frameNumber) {
        if (controladores == null) {
            return;
        }

        handlePauseInput();
        if (pauseMenuOpen || !gameLoop.isRunning()) {
            return;
        }

        handleCraftOverlayToggle();
        if (craftOverlayOpen) {
            handleCrafting();
            updateTutorial();
            return;
        }

        handlePlayerFire();
        tickCraftFeedback();
        comboSystem.tick();
        temporaryBuffs.tick();
        updateElementDrops();
        updateBuffDrops();
        updateTutorial();

        // Permite al jugador moverse hacia la izquierda y hacia la derecha
        PowerUpVelocidad();

        // High score en memoria (sin I/O por frame)
        if (score > highScore) {
            highScore = score;
        }

        // Agrega la opción para restablecer el puntaje alto
        if (!tutorialMode && controladores.getKeyStatus(82)) { // KEYSTATUS(82) es la tecla R según el código ASCII
            int respuesta = JOptionPane.showConfirmDialog(
                    null,
                    Messages.get("dialog.reset.highscore"),
                    Messages.get("dialog.yes.title"),
                    JOptionPane.YES_NO_OPTION);
            controladores.resetController();
            if (respuesta == 0) {
                scoreService.clearHighScores();
                highScore = 0;
            }
        }

        spawnEnemyBeams();
        spawnBonusEnemy();

        // Hace que los enemigos se muevan y cambien de dirección en las fronteras.
        boolean skipEnemyMove = temporaryBuffs.hasSlow() && (frameNumber % 2 == 0);
        if (!enemyList.isEmpty() && !skipEnemyMove) {
            boolean bossLevel = levelManager != null && levelManager.isBossLevel(level);
            EnemyFormation.advance(enemyList, bossLevel);
        }

        // Proyectiles del jugador (movimiento + colisiones)
        newBulletCanFire = CollisionSystem.updatePlayerProjectiles(
                projectiles, enemyList, shieldList, bonusEnemyList,
                new CollisionSystem.Listener() {
                    @Override
                    public void onEnemyHit(int enemyIndex, int damage) {
                        hitSoundAudio.play();
                        ColisionesBalas(enemyIndex, damage);
                    }
                    @Override
                    public void onShieldHit(int shieldIndex) {
                        ColisionesEscudo(shieldIndex);
                    }
                    @Override
                    public void onBonusHit(int bonusIndex) {
                        if (bonusIndex >= 0 && bonusIndex < bonusEnemyList.size()) {
                            Ship bonus = bonusEnemyList.get(bonusIndex);
                            int dropX = bonus.getXPosition();
                            int dropY = bonus.getYPosition();
                            bonusEnemyList.remove(bonusIndex);
                            newBonusEnemy = true;
                            bonusSoundAudio.play();
                            score += GameBalance.SCORE_BONUS;
                            maybeSpawnLifeDrop(dropX, dropY, GameBalance.LIFE_DROP_CHANCE_BONUS_PERCENT);
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

        // Mueve rayos y resuelve colisiones con escudos / nave
        updateEnemyBeams();

        // Acelera el disparo del rayo permitiendo solo que se disparen nuevos rayos una vez que todos los rayos antiguos están fuera de la pantalla o han chocado
        if (beamList.isEmpty()) {
            newBeamCanFire = true;
        }

        /// Destruye escudos si los alienígenas chocan con ellos
        for (int input = 0; input < enemyList.size(); input++) {
            Enemy enemy = enemyList.get(input);
            for (int j = 0; j < shieldList.size(); j++) {
                //Se manda a llamar la Clase GameObject
                if (enemy.Colisionando(shieldList.get(j))) {
                    shieldList.remove(j);
                }
            }
            // El jefe no invade: se queda en la arena. Un minion que baja se descarta.
            if (enemy.isBoss()) {
                continue;
            }
            if (enemy.getYPosition() + 50 >= 600) {
                if (hasLivingBoss()) {
                    enemyList.remove(input);
                    input--;
                    continue;
                }
                enemyList.clear();
                shieldList.clear();
                beamList.clear();
                ElementoList.clear();
                buffDropList.clear();
                projectiles.clear();
                comboSystem.onPlayerHit();
                numberOfLives = Math.max(0, numberOfLives - 1);
                deathSoundAudio.play();
                ConfigurarJuego();
                return;
            }
        }

        // Actualiza la pantalla del contador de vida
        if (NaveJugador != null && NaveJugador.estaColisionando) {
            applyPlayerDamage();
            NaveJugador.estaColisionando = false;
        }

        // Termina el juego si el jugador se queda sin vidas
        if (lifeList.isEmpty() && numberOfLives <= 0) {
            deathSoundAudio.play();
            if (tutorialMode) {
                abortTutorialToMenu();
                return;
            }
            saveCurrentScore(false);
            String overMsg = endlessMode
                    ? Messages.format("dialog.endless.over", score, level)
                    : Messages.format("dialog.game.over", score);
            int respuesta = JOptionPane.showConfirmDialog(
                    null,
                    Messages.get("dialog.play.again"),
                    overMsg,
                    JOptionPane.YES_NO_OPTION);
            if (respuesta == 0) {
                ResetearValores();
                ConfigurarJuego();
            } else {
                stop();
                GameOver fin = new GameOver();
                fin.AsignarScore(score);
                fin.setVisible(true);
            }
            return;
        }
        

        // Pasa al siguiente nivel, restablece todas las listas, 
        // configura todos los contadores a los valores correctos
        if (enemyList.isEmpty()){
            if (tutorialMode) {
                if (tutorial != null && tutorial.isActive()) {
                    enemyList.addAll(levelManager.createTutorialWave());
                }
                return;
            }
            beamList.clear();
            shieldList.clear();
            ElementoList.clear();
            buffDropList.clear();
            if(level%3 == 0) bonusEnemyList.clear();
            lifeList.clear();
            level += 1;
            ConfigurarJuego();
            levelUpSoundAudio.play(); // Plays level up sound 
        }

    }

    private void handlePauseInput() {
        if (!controladores.getKeyStatus(27)) {
            return;
        }
        if (craftOverlayOpen) {
            craftOverlayOpen = false;
            controladores.resetController();
            return;
        }
        if (pauseMenuOpen) {
            return;
        }
        pauseMenuOpen = true;
        controladores.resetController();
        PausarJuego();
        java.awt.EventQueue.invokeLater(() -> {
            MenuEmergente menu = new MenuEmergente();
            menu.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    pauseMenuOpen = false;
                }
            });
            menu.setVisible(true);
        });
    }

    private void handleCraftOverlayToggle() {
        if (!controladores.getKeyStatus(KeyEvent.VK_C)) {
            return;
        }
        craftOverlayOpen = !craftOverlayOpen;
        controladores.resetController();
    }

    private void handlePlayerFire() {
        if (controladores.getKeyStatus(32) && newBulletCanFire) {
            newBeamCanFire = false;
            bulletSoundAudio.play();
            PowerUpBalas();
            if (tutorialMode && tutorial != null) {
                tutorial.onFired();
            }
        }
    }

    private void tickCraftFeedback() {
        if (craftFeedbackTicks > 0) {
            craftFeedbackTicks--;
            if (craftFeedbackTicks == 0) {
                craftFeedback = null;
            }
        }
    }

    private void handleCrafting() {
        tickCraftFeedback();
        CraftingSystem.Result result = CraftingSystem.tryCraft(controladores, CantidadElemento, craftFlags);
        if (result == null) {
            return;
        }
        craftFeedback = result.getFeedbackKey();
        craftFeedbackTicks = 40;
        controladores.resetController();
        if (result.getMinBullets() != null) {
            CantidadBalas = Math.max(CantidadBalas, result.getMinBullets());
        }
        if (result.getSpeedLevel() != null) {
            Velocidad = result.getSpeedLevel();
            PowerUpVelocidad();
        }
        if (tutorialMode && tutorial != null) {
            tutorial.onCrafted(result.getRecipeIndex());
        }
    }

    private void updateTutorial() {
        if (!tutorialMode || tutorial == null) {
            return;
        }
        if (controladores.getKeyStatus(KeyEvent.VK_LEFT) || controladores.getKeyStatus(KeyEvent.VK_RIGHT)) {
            tutorial.onMoved();
        }
        if (tutorial.consumeDropSpawnRequest()) {
            int x = NaveJugador != null ? NaveJugador.getXPosition() : 500;
            ElementoList.add(new ElementoDrop(x, 180, 6, 0, null));
        }
        if (tutorial.consumeCraftAssistRequest()) {
            if (CantidadElemento[6] < 1) {
                CantidadElemento[6] = 1;
            }
            craftOverlayOpen = true;
        }
        if (tutorial.consumeCompletion()) {
            completeTutorial();
        }
    }

    private void completeTutorial() {
        GameConfig.getInstance().setTutorialCompleted(true);
        craftOverlayOpen = false;
        tutorialMode = false;
        tutorial = null;
        stop();
        java.awt.EventQueue.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    null,
                    Messages.get("tutorial.done"),
                    Messages.get("tutorial.title"),
                    JOptionPane.INFORMATION_MESSAGE);
            FrmNombre.PararJuego();
            new Inicio().setVisible(true);
        });
    }

    private void abortTutorialToMenu() {
        craftOverlayOpen = false;
        tutorialMode = false;
        tutorial = null;
        stop();
        java.awt.EventQueue.invokeLater(() -> {
            FrmNombre.PararJuego();
            new Inicio().setVisible(true);
        });
    }

    private void updateElementDrops() {
        String pickup = DropSystem.updateDrops(ElementoList, NaveJugador, CantidadElemento);
        if (pickup != null) {
            craftFeedback = pickup;
            craftFeedbackTicks = 40;
            bonusSoundAudio.play();
            if (tutorialMode && tutorial != null) {
                tutorial.onDropCollected();
            }
        }
    }

    private void updateEnemyBeams() {
        int speed = levelManager != null && levelManager.isBossLevel(level)
                ? GameBalance.BOSS_BEAM_SPEED
                : GameBalance.NORMAL_BEAM_SPEED;

        for (int index = beamList.size() - 1; index >= 0; index--) {
            Beam current = beamList.get(index);
            current.setYPosition(current.getYPosition() + speed);
            if (current.getYPosition() > 800) {
                beamList.remove(index);
                continue;
            }

            boolean hitShield = false;
            for (int j = shieldList.size() - 1; j >= 0; j--) {
                if (current.Colisionando(shieldList.get(j))) {
                    CollisionSystem.degradeShield(shieldList, j);
                    shieldSoundAudio.play();
                    beamList.remove(index);
                    hitShield = true;
                    break;
                }
            }
            if (hitShield) {
                continue;
            }

            if (NaveJugador != null && current.Colisionando(NaveJugador)) {
                beamList.remove(index);
                damageSoundAudio.play();
                applyPlayerDamage();
            }
        }
    }

    private void applyPlayerDamage() {
        if (temporaryBuffs.tryAbsorbHit()) {
            craftFeedback = Messages.get("buff.shield.break");
            craftFeedbackTicks = 40;
            return;
        }
        comboSystem.onPlayerHit();
        if (!lifeList.isEmpty()) {
            lifeList.remove(lifeList.size() - 1);
            numberOfLives = lifeList.size();
        }
    }

    private void updateBuffDrops() {
        String pickup = temporaryBuffs.updateDrops(buffDropList, NaveJugador);
        int extraLives = temporaryBuffs.consumePendingLives();
        boolean grantedLife = false;
        for (int i = 0; i < extraLives; i++) {
            grantedLife |= grantExtraLife();
        }
        if (pickup != null) {
            craftFeedback = extraLives > 0 && !grantedLife
                    ? Messages.get("buff.life.max")
                    : pickup;
            craftFeedbackTicks = 40;
            bonusSoundAudio.play();
        }
    }

    private boolean grantExtraLife() {
        if (numberOfLives >= GameBalance.MAX_LIVES) {
            return false;
        }
        numberOfLives++;
        if (levelManager != null) {
            lifeList.clear();
            lifeList.addAll(levelManager.createLifeIcons(numberOfLives));
        }
        return true;
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
        buffDropList.clear();
        projectiles.clear();
        score = 0;
        level = 1;
        bossMaxHealth = GameBalance.BOSS_HEALTH;
        bossHealth = bossMaxHealth;
        desperateMinionsSpawned = false;
        numberOfLives = tutorialMode
                ? GameBalance.LIVES_MEDIUM
                : GameBalance.startingLives(GameConfig.getInstance().getDifficulty());
        CantidadBalas = 0;
        Velocidad = 0;
        newBulletCanFire = true;
        newBeamCanFire = true;
        newBonusEnemy = true;
        craftFlags.reset();
        craftFeedback = null;
        craftFeedbackTicks = 0;
        pauseMenuOpen = false;
        craftOverlayOpen = false;
        comboSystem.reset();
        temporaryBuffs.reset();
        lastHitScore = GameBalance.SCORE_ALIEN;
                
        for(int i = 1; i <= 11; i++){
            CantidadElemento[i] = 0;
        }
        highScore = scoreService.bestScore();
    }

    private void saveCurrentScore(boolean won) {
        if (tutorialMode) {
            return;
        }
        String username = FrmNombre.nombre;
        if (username == null || username.isBlank()) {
            username = "Player";
        }
        int difficulty = GameConfig.getInstance().getDifficulty();
        try {
            scoreService.saveRun(new RunEntry(username, score, level, difficulty, won));
            highScore = Math.max(highScore, scoreService.bestScore());
        } catch (IllegalArgumentException ex) {
            scoreService.saveRun(new RunEntry("Player", score, level, difficulty, won));
            highScore = Math.max(highScore, score);
        }
    }
        
    public void IniciarJuego(){
        Messages.reloadFromConfig();
        // Establecer el tamaño del Panel
        this.setSize(AnchoJuego, AlturaJuego);
        this.setPreferredSize(new Dimension(AnchoJuego, AlturaJuego));
        this.setBackground(Color.BLACK);

        GameConfig.getInstance().applyTo(
                beamSoundAudio, bulletSoundAudio, levelUpSoundAudio, deathSoundAudio,
                hitSoundAudio, shieldSoundAudio, bossSoundAudio, bonusSoundAudio, damageSoundAudio);
        GameConfig.getInstance().applyAudio();

        // Registrar KeyboardController como KeyListener
        controladores = new KeyboardController();
        this.addKeyListener(controladores);

        // Llama a setupGame para inicializar campos
        ResetearValores();
        if (tutorialMode) {
            endlessMode = false;
            tutorial = new TutorialController();
            if (FrmNombre.nombre == null || FrmNombre.nombre.isBlank()) {
                FrmNombre.nombre = "Tutorial";
            }
        }
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
