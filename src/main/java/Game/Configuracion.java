/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Tipografia.Fuente;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicButtonUI;
import persistence.RunEntry;

/**
 *
 * @author lenny
 */
public class Configuracion extends javax.swing.JFrame {

    /** 1=fácil, 2=medio, 3=difícil ({@link RunEntry}). */
    public static int TipoDificultad = RunEntry.DIFFICULTY_HARD;
    
    /**
    * @return the TipoDificultad
    */
    public int getTipoDificultad() {
        return TipoDificultad;
    }
    
    FondoGame1 fondo = new FondoGame1();
    Fuente TipoFuente = new Fuente();
    //Color boton facil
    Color CelesteEasy = new Color (99,183,217);
    Color AzulEasy = new Color (0, 12, 109);
    //Color botone medio
    Color MoradoMedio = new Color (182,167,214);
    Color MoradoOscuroMedio = new Color (120, 40, 140);
    //Color Boton Dificil
    Color RosaDificil = new Color (207, 0, 96);
    Color RojoDificil = new Color (187, 0, 19);
    //Colores del boton High Score
    Color GrisGame = new Color(153,153,153);
    Color CelesteGame = new Color(40, 255, 240);
    Color LetrasGris = new Color(51,51,51);
    Color LetrasAzul = new Color(31,75,142);

    private JPanel panelExtra;
    private JSlider sliderSfx;
    private JSlider sliderMusic;
    private JCheckBox chkMute;
    private JCheckBox chkFullscreen;
    private JComboBox<String> comboLanguage;
    private JLabel lblAudio;
    private JLabel lblSfx;
    private JLabel lblMusic;
    private JLabel lblLanguage;
    
    public Configuracion() {
        Messages.reloadFromConfig();
        TipoDificultad = GameConfig.getInstance().getDifficulty();
        this.setContentPane(fondo);
        initComponents();
        
        PanelDificultad.setBackground(new Color (0,0,0,0));
        PanelRecord.setBackground(new Color(0,0,0,0));
        
        //Tipografia diferente
        BtnFacil.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        BtnMedio.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        BtnDificil.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        BtnRecord.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        lblModo.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        lblVerRecord.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        lblDificultad.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        lblDificultad.setVisible(false);
        
        //Forma rectangular de los botones
        ShapedButtonUI squareUI = new ShapedButtonUI();
        squareUI.setShape(ButtonShape.SQUARE, BtnFacil);
        BtnFacil.setUI(squareUI);
        BtnFacil.setPreferredSize(new Dimension(100, 100));
        squareUI.setShape(ButtonShape.SQUARE, BtnMedio);
        BtnMedio.setUI(squareUI);
        BtnMedio.setPreferredSize(new Dimension(100, 100));
        squareUI.setShape(ButtonShape.SQUARE, BtnDificil);
        BtnDificil.setUI(squareUI);
        BtnDificil.setPreferredSize(new Dimension(100, 100));
        squareUI.setShape(ButtonShape.SQUARE, BtnRecord);
        BtnRecord.setUI(squareUI);
        BtnRecord.setPreferredSize(new Dimension(100, 100));

        installExtraSettings();
        applyLocaleTexts();
        pack();
        setLocationRelativeTo(null);
        
        Image icon = new ImageIcon(getClass().getResource("/Imagenes/SpaceChemistryIcon.png")).getImage();
        setIconImage(icon);
    }

    private void installExtraSettings() {
        GameConfig config = GameConfig.getInstance();
        panelExtra = PanelAudio;
        panelExtra.setLayout(new GridBagLayout());
        panelExtra.removeAll();

        lblAudio = new JLabel();
        lblAudio.setForeground(Color.WHITE);
        lblAudio.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 12));
        lblSfx = new JLabel();
        lblSfx.setForeground(Color.WHITE);
        lblMusic = new JLabel();
        lblMusic.setForeground(Color.WHITE);
        lblLanguage = new JLabel();
        lblLanguage.setForeground(Color.WHITE);

        sliderSfx = new JSlider(0, 100, Math.round(config.getSfxVolumeRaw() * 100));
        sliderMusic = new JSlider(0, 100, Math.round(config.getMusicVolumeRaw() * 100));
        chkMute = new JCheckBox();
        chkMute.setOpaque(false);
        chkMute.setForeground(Color.WHITE);
        chkMute.setSelected(config.isMuted());
        chkFullscreen = new JCheckBox();
        chkFullscreen.setOpaque(false);
        chkFullscreen.setForeground(Color.WHITE);
        chkFullscreen.setSelected(config.isFullscreen());
        comboLanguage = new JComboBox<>(new String[]{"es", "en"});
        comboLanguage.setSelectedItem(config.getLanguage());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 8, 4, 8);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 4;
        panelExtra.add(lblAudio, c);
        c.gridwidth = 1;
        c.gridy = 1; c.gridx = 0; panelExtra.add(lblSfx, c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        panelExtra.add(sliderSfx, c);
        c.gridx = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        panelExtra.add(chkMute, c);
        c.gridy = 2; c.gridx = 0; panelExtra.add(lblMusic, c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        panelExtra.add(sliderMusic, c);
        c.gridx = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        panelExtra.add(chkFullscreen, c);
        c.gridy = 3; c.gridx = 0; panelExtra.add(lblLanguage, c);
        c.gridx = 1; panelExtra.add(comboLanguage, c);

        sliderSfx.addChangeListener(e -> {
            if (!sliderSfx.getValueIsAdjusting()) {
                config.setSfxVolume(sliderSfx.getValue() / 100f);
            }
        });
        sliderMusic.addChangeListener(e -> {
            if (!sliderMusic.getValueIsAdjusting()) {
                config.setMusicVolume(sliderMusic.getValue() / 100f);
            }
        });
        chkMute.addActionListener(e -> config.setMuted(chkMute.isSelected()));
        chkFullscreen.addActionListener(e -> config.setFullscreen(chkFullscreen.isSelected()));
        comboLanguage.addActionListener(e -> {
            String lang = String.valueOf(comboLanguage.getSelectedItem());
            config.setLanguage(lang);
            applyLocaleTexts();
        });

        panelExtra.revalidate();
        panelExtra.repaint();
    }

    private void applyLocaleTexts() {
        Messages.reloadFromConfig();
        lblModo.setText(Messages.get("settings.difficulty"));
        BtnFacil.setText(Messages.get("settings.easy"));
        BtnMedio.setText(Messages.get("settings.medium"));
        BtnDificil.setText(Messages.get("settings.hard"));
        lblVerRecord.setText(Messages.get("settings.records"));
        BtnRecord.setText(Messages.get("settings.highscore"));
        if (lblAudio != null) {
            lblAudio.setText(Messages.get("settings.audio"));
            lblSfx.setText(Messages.get("settings.sfx"));
            lblMusic.setText(Messages.get("settings.music"));
            chkMute.setText(Messages.get("settings.mute"));
            chkFullscreen.setText(Messages.get("settings.fullscreen"));
            lblLanguage.setText(Messages.get("settings.language"));
        }
    }
    
    /**
    * Definimos la forma cuadrada
    */
    public enum ButtonShape {
        SQUARE
    }
    
    private class ShapedButtonUI extends BasicButtonUI {
        /** Button shape. */
        private ButtonShape shape;

        public ShapedButtonUI() {
            super();
        }

        public void setShape(ButtonShape shape, JButton button){
            // no pintamos el borde
            button.setBorderPainted(false);
            this.shape = shape;
        }

        public void paint(JComponent c) {
            // definamos las formas de nuestros botones
            Shape buttonShape = null;
            switch (shape) {
                case SQUARE:
                    buttonShape = new Rectangle(0, 0, c.getWidth(), c.getHeight());
                break;
            }
        }

        protected void paintButtonPressed(AbstractButton b) {
            // definamos las formas de nuestros botones
            Shape buttonShape = null;
            switch (shape) {
                case SQUARE:
                buttonShape = new Rectangle(0, 0, b.getWidth(), b.getHeight());
            break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCerrar = new javax.swing.JLabel();
        lblHome1 = new javax.swing.JLabel();
        PanelDificultad = new javax.swing.JPanel();
        lblModo = new javax.swing.JLabel();
        BtnFacil = new javax.swing.JButton();
        BtnMedio = new javax.swing.JButton();
        BtnDificil = new javax.swing.JButton();
        PanelRecord = new javax.swing.JPanel();
        lblVerRecord = new javax.swing.JLabel();
        BtnRecord = new javax.swing.JButton();
        PanelAudio = new javax.swing.JPanel();
        lblDificultad = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(950, 620));
        setUndecorated(true);
        setSize(new java.awt.Dimension(950, 620));

        lblCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Close.png"))); // NOI18N
        lblCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarMouseClicked(evt);
            }
        });

        lblHome1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/home.png"))); // NOI18N
        lblHome1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblHome1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHome1MouseClicked(evt);
            }
        });

        PanelDificultad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 183, 217), 2));

        lblModo.setForeground(new java.awt.Color(255, 255, 255));
        lblModo.setText("Modificar Dificultad: ");

        BtnFacil.setBackground(new java.awt.Color(99, 183, 217));
        BtnFacil.setText("Facil");
        BtnFacil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnFacil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnFacilMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BtnFacilMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BtnFacilMouseExited(evt);
            }
        });

        BtnMedio.setBackground(new java.awt.Color(182, 167, 214));
        BtnMedio.setText("Normal");
        BtnMedio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnMedio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnMedioMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BtnMedioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BtnMedioMouseExited(evt);
            }
        });

        BtnDificil.setBackground(new java.awt.Color(207, 0, 96));
        BtnDificil.setText("Dificil");
        BtnDificil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnDificil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnDificilMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BtnDificilMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BtnDificilMouseExited(evt);
            }
        });

        javax.swing.GroupLayout PanelDificultadLayout = new javax.swing.GroupLayout(PanelDificultad);
        PanelDificultad.setLayout(PanelDificultadLayout);
        PanelDificultadLayout.setHorizontalGroup(
            PanelDificultadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDificultadLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(lblModo)
                .addGap(106, 106, 106)
                .addComponent(BtnFacil, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(BtnMedio, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(BtnDificil, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        PanelDificultadLayout.setVerticalGroup(
            PanelDificultadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDificultadLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(PanelDificultadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblModo)
                    .addComponent(BtnFacil, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnMedio, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnDificil, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        PanelRecord.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 183, 217), 2));

        lblVerRecord.setForeground(new java.awt.Color(255, 255, 255));
        lblVerRecord.setText("Ver Records del Juego:");

        BtnRecord.setBackground(new java.awt.Color(153, 153, 153));
        BtnRecord.setForeground(new java.awt.Color(51, 51, 51));
        BtnRecord.setText("High Score");
        BtnRecord.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnRecord.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnRecordMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BtnRecordMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BtnRecordMouseExited(evt);
            }
        });

        javax.swing.GroupLayout PanelRecordLayout = new javax.swing.GroupLayout(PanelRecord);
        PanelRecord.setLayout(PanelRecordLayout);
        PanelRecordLayout.setHorizontalGroup(
            PanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelRecordLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(lblVerRecord)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addComponent(BtnRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        PanelRecordLayout.setVerticalGroup(
            PanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelRecordLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVerRecord)
                    .addComponent(BtnRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        PanelAudio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 183, 217), 2));
        PanelAudio.setOpaque(false);
        javax.swing.GroupLayout PanelAudioLayout = new javax.swing.GroupLayout(PanelAudio);
        PanelAudio.setLayout(PanelAudioLayout);
        PanelAudioLayout.setHorizontalGroup(
            PanelAudioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 780, Short.MAX_VALUE)
        );
        PanelAudioLayout.setVerticalGroup(
            PanelAudioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );

        lblDificultad.setForeground(new java.awt.Color(0, 0, 153));
        lblDificultad.setText("La dificultad a cambiado a: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblHome1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(PanelAudio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PanelRecord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblCerrar)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(PanelDificultad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblDificultad, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(259, 259, 259))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCerrar)
                .addGap(24, 24, 24)
                .addComponent(PanelDificultad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelRecord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelAudio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(lblDificultad)
                .addGap(18, 18, 18)
                .addComponent(lblHome1)
                .addGap(16, 16, 16))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblCerrarMouseClicked

    private void lblHome1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHome1MouseClicked
        Inicio menu = new Inicio();
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_lblHome1MouseClicked

    private void BtnFacilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnFacilMouseClicked
        GameConfig.getInstance().setDifficulty(RunEntry.DIFFICULTY_EASY);
        TipoDificultad = RunEntry.DIFFICULTY_EASY;
        lblDificultad.setText(Messages.format("settings.difficulty.changed", Messages.get("diff.easy")));
        lblDificultad.setVisible(true);
    }//GEN-LAST:event_BtnFacilMouseClicked

    private void BtnFacilMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnFacilMouseEntered
        BtnFacil.setBackground(AzulEasy);
        BtnFacil.setForeground(Color.white);
    }//GEN-LAST:event_BtnFacilMouseEntered

    private void BtnFacilMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnFacilMouseExited
        BtnFacil.setBackground(CelesteEasy);
        BtnFacil.setForeground(Color.BLACK);
    }//GEN-LAST:event_BtnFacilMouseExited

    private void BtnMedioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnMedioMouseClicked
        GameConfig.getInstance().setDifficulty(RunEntry.DIFFICULTY_MEDIUM);
        TipoDificultad = RunEntry.DIFFICULTY_MEDIUM;
        lblDificultad.setText(Messages.format("settings.difficulty.changed", Messages.get("diff.medium")));
        lblDificultad.setVisible(true);
    }//GEN-LAST:event_BtnMedioMouseClicked

    private void BtnMedioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnMedioMouseEntered
        BtnMedio.setBackground(MoradoOscuroMedio);
        BtnMedio.setForeground(Color.white);
    }//GEN-LAST:event_BtnMedioMouseEntered

    private void BtnMedioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnMedioMouseExited
        BtnMedio.setBackground(MoradoMedio);
        BtnMedio.setForeground(Color.BLACK);
    }//GEN-LAST:event_BtnMedioMouseExited

    private void BtnDificilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnDificilMouseClicked
        GameConfig.getInstance().setDifficulty(RunEntry.DIFFICULTY_HARD);
        TipoDificultad = RunEntry.DIFFICULTY_HARD;
        lblDificultad.setText(Messages.format("settings.difficulty.changed", Messages.get("diff.hard")));
        lblDificultad.setVisible(true);
    }//GEN-LAST:event_BtnDificilMouseClicked

    private void BtnDificilMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnDificilMouseEntered
        BtnDificil.setBackground(RojoDificil);
        BtnDificil.setForeground(Color.white);
    }//GEN-LAST:event_BtnDificilMouseEntered

    private void BtnDificilMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnDificilMouseExited
        BtnDificil.setBackground(RosaDificil);
        BtnDificil.setForeground(Color.BLACK);
    }//GEN-LAST:event_BtnDificilMouseExited

    private void BtnRecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnRecordMouseClicked
        this.dispose();
        Records score = new Records();
        score.setVisible(true);
    }//GEN-LAST:event_BtnRecordMouseClicked

    private void BtnRecordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnRecordMouseEntered
        BtnRecord.setBackground(CelesteGame);
        BtnRecord.setForeground(LetrasAzul);
    }//GEN-LAST:event_BtnRecordMouseEntered

    private void BtnRecordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnRecordMouseExited
        BtnRecord.setBackground(GrisGame);
        BtnRecord.setForeground(LetrasGris);
    }//GEN-LAST:event_BtnRecordMouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Configuracion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Configuracion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Configuracion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Configuracion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Configuracion().setVisible(true);
            }
        });
    }
    
    
    //Método para colocar imagen de fondo
    private class FondoGame1 extends JPanel {
        public Image imagen;
        public void paint(Graphics g) {
            imagen = new ImageIcon(getClass().getResource("/Imagenes/Space1.gif")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDificil;
    private javax.swing.JButton BtnFacil;
    private javax.swing.JButton BtnMedio;
    private javax.swing.JButton BtnRecord;
    private javax.swing.JPanel PanelAudio;
    private javax.swing.JPanel PanelDificultad;
    private javax.swing.JPanel PanelRecord;
    private javax.swing.JLabel lblCerrar;
    private javax.swing.JLabel lblDificultad;
    private javax.swing.JLabel lblHome1;
    private javax.swing.JLabel lblModo;
    private javax.swing.JLabel lblVerRecord;
    // End of variables declaration//GEN-END:variables
}
