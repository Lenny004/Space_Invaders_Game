package Game;

import Tipografia.Fuente;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import persistence.RunEntry;
import persistence.ScoreService;

/**
 *
 * @author lenny
 */
public class Records extends javax.swing.JFrame {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM HH:mm").withZone(ZoneId.systemDefault());

    FondoGame1 fondo = new FondoGame1();
    Fuente TipoFuente = new Fuente();
    private JLabel lblRecentTitle;
    private final JLabel[] histNames = new JLabel[5];
    private final JLabel[] histScores = new JLabel[5];
    
    public Records() {
        Messages.reloadFromConfig();
        this.setContentPane(fondo);
        initComponents();
        installHistorySection();
        applyLocaleTexts();
        mostrarDatos();
        lblNombre.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 12));
        lblPlace.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 12));
        lblScore.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 12));
        lbl1.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lbl2.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lbl3.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lbl4.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lbl5.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblnombre1.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblnombre2.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblnombre3.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblnombre4.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblnombre5.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblR1.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblR2.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblR3.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblR4.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        lblR5.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
        
        Image icon = new ImageIcon(getClass().getResource("/Imagenes/SpaceChemistryIcon.png")).getImage();
        setIconImage(icon);
    }

    private void installHistorySection() {
        // Compactar top 5 para dejar sitio al historial
        lbl1.setBounds(40, 36, 20, 16);
        lbl2.setBounds(40, 62, 20, 16);
        lbl3.setBounds(40, 88, 20, 16);
        lbl4.setBounds(40, 114, 20, 16);
        lbl5.setBounds(41, 140, 20, 16);
        lblnombre1.setBounds(180, 36, 307, 16);
        lblnombre2.setBounds(180, 62, 307, 16);
        lblnombre3.setBounds(180, 88, 307, 16);
        lblnombre4.setBounds(180, 114, 307, 16);
        lblnombre5.setBounds(180, 140, 307, 16);
        lblR1.setBounds(590, 36, 150, 16);
        lblR2.setBounds(590, 62, 150, 16);
        lblR3.setBounds(590, 88, 150, 16);
        lblR4.setBounds(590, 114, 150, 16);
        lblR5.setBounds(590, 140, 150, 16);

        lblRecentTitle = new JLabel();
        lblRecentTitle.setForeground(new Color(99, 183, 217));
        lblRecentTitle.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 12));
        jPanel1.add(lblRecentTitle);
        lblRecentTitle.setBounds(23, 180, 200, 16);

        Color[] colors = {
                new Color(99, 183, 217),
                new Color(182, 167, 214),
                new Color(182, 167, 214),
                new Color(182, 167, 214),
                new Color(231, 125, 176)
        };
        for (int i = 0; i < 5; i++) {
            int y = 210 + i * 28;
            histNames[i] = new JLabel("---");
            histNames[i].setForeground(colors[i]);
            histNames[i].setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
            jPanel1.add(histNames[i]);
            histNames[i].setBounds(40, y, 450, 16);

            histScores[i] = new JLabel("0");
            histScores[i].setForeground(colors[i]);
            histScores[i].setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 10));
            jPanel1.add(histScores[i]);
            histScores[i].setBounds(520, y, 220, 16);
        }
    }

    private void applyLocaleTexts() {
        lblPlace.setText(Messages.get("records.place"));
        lblNombre.setText(Messages.get("records.player"));
        lblScore.setText(Messages.get("records.score"));
        if (lblRecentTitle != null) {
            lblRecentTitle.setText(Messages.get("records.recent"));
        }
    }

    private static String difficultyText(int difficulty) {
        return switch (RunEntry.normalizeDifficulty(difficulty)) {
            case RunEntry.DIFFICULTY_MEDIUM -> Messages.get("diff.medium");
            case RunEntry.DIFFICULTY_HARD -> Messages.get("diff.hard");
            default -> Messages.get("diff.easy");
        };
    }

    private static String tipFor(RunEntry run) {
        String result;
        if (run.isWon()) {
            result = Messages.get("result.win");
        } else if (run.getLevelReached() > GameBalance.VICTORY_AFTER_LEVEL) {
            result = Messages.get("result.endless");
        } else {
            result = Messages.get("result.loss");
        }
        return Messages.format("tooltip.run", run.getLevelReached(), difficultyText(run.getDifficulty()), result);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblPlace = new javax.swing.JLabel();
        lbl1 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        lbl3 = new javax.swing.JLabel();
        lbl4 = new javax.swing.JLabel();
        lbl5 = new javax.swing.JLabel();
        lblnombre1 = new javax.swing.JLabel();
        lblnombre2 = new javax.swing.JLabel();
        lblnombre3 = new javax.swing.JLabel();
        lblnombre4 = new javax.swing.JLabel();
        lblnombre5 = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblScore = new javax.swing.JLabel();
        lblR1 = new javax.swing.JLabel();
        lblR2 = new javax.swing.JLabel();
        lblR3 = new javax.swing.JLabel();
        lblR4 = new javax.swing.JLabel();
        lblR5 = new javax.swing.JLabel();
        lblCerrar = new javax.swing.JLabel();
        lblHome = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(950, 525));
        setUndecorated(true);
        setSize(new java.awt.Dimension(950, 525));

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(null);

        lblPlace.setForeground(new java.awt.Color(99, 183, 217));
        lblPlace.setText("PLACE");
        jPanel1.add(lblPlace);
        lblPlace.setBounds(23, 5, 110, 16);

        lbl1.setForeground(new java.awt.Color(99, 183, 217));
        lbl1.setText("1.");
        jPanel1.add(lbl1);
        lbl1.setBounds(40, 70, 10, 16);

        lbl2.setForeground(new java.awt.Color(182, 167, 214));
        lbl2.setText("2.");
        jPanel1.add(lbl2);
        lbl2.setBounds(40, 130, 20, 16);

        lbl3.setForeground(new java.awt.Color(182, 167, 214));
        lbl3.setText("3.");
        jPanel1.add(lbl3);
        lbl3.setBounds(40, 190, 20, 16);

        lbl4.setForeground(new java.awt.Color(182, 167, 214));
        lbl4.setText("4.");
        jPanel1.add(lbl4);
        lbl4.setBounds(40, 250, 30, 16);

        lbl5.setForeground(new java.awt.Color(231, 125, 176));
        lbl5.setText("5.");
        jPanel1.add(lbl5);
        lbl5.setBounds(41, 310, 30, 16);

        lblnombre1.setForeground(new java.awt.Color(99, 183, 217));
        lblnombre1.setText("Nombre1");
        jPanel1.add(lblnombre1);
        lblnombre1.setBounds(180, 70, 307, 20);

        lblnombre2.setForeground(new java.awt.Color(182, 167, 214));
        lblnombre2.setText("Nombre2");
        jPanel1.add(lblnombre2);
        lblnombre2.setBounds(180, 130, 307, 16);

        lblnombre3.setForeground(new java.awt.Color(182, 167, 214));
        lblnombre3.setText("Nombre3");
        jPanel1.add(lblnombre3);
        lblnombre3.setBounds(180, 190, 307, 16);

        lblnombre4.setForeground(new java.awt.Color(182, 167, 214));
        lblnombre4.setText("Nombre4");
        jPanel1.add(lblnombre4);
        lblnombre4.setBounds(180, 250, 307, 16);

        lblnombre5.setForeground(new java.awt.Color(231, 125, 176));
        lblnombre5.setText("Nombre5");
        jPanel1.add(lblnombre5);
        lblnombre5.setBounds(180, 310, 307, 16);

        lblNombre.setForeground(new java.awt.Color(99, 183, 217));
        lblNombre.setText("PLAYER NAME");
        jPanel1.add(lblNombre);
        lblNombre.setBounds(270, 10, 160, 16);

        lblScore.setForeground(new java.awt.Color(99, 183, 217));
        lblScore.setText("SCORE");
        jPanel1.add(lblScore);
        lblScore.setBounds(620, 10, 70, 16);

        lblR1.setForeground(new java.awt.Color(99, 183, 217));
        lblR1.setText("Record1");
        jPanel1.add(lblR1);
        lblR1.setBounds(590, 70, 150, 16);

        lblR2.setForeground(new java.awt.Color(182, 167, 214));
        lblR2.setText("Record2");
        jPanel1.add(lblR2);
        lblR2.setBounds(590, 130, 150, 16);

        lblR3.setForeground(new java.awt.Color(182, 167, 214));
        lblR3.setText("Record3");
        jPanel1.add(lblR3);
        lblR3.setBounds(590, 190, 150, 16);

        lblR4.setForeground(new java.awt.Color(182, 167, 214));
        lblR4.setText("Record4");
        jPanel1.add(lblR4);
        lblR4.setBounds(590, 250, 150, 16);

        lblR5.setForeground(new java.awt.Color(231, 125, 176));
        lblR5.setText("Record5");
        jPanel1.add(lblR5);
        lblR5.setBounds(590, 310, 150, 16);

        lblCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Close.png"))); // NOI18N
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarMouseClicked(evt);
            }
        });

        lblHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/home.png"))); // NOI18N
        lblHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHomeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(915, 915, 915)
                        .addComponent(lblCerrar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 809, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblHome)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblCerrar)
                .addGap(43, 43, 43)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(lblHome, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblCerrarMouseClicked

    private void lblHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHomeMouseClicked
        Inicio menu = new Inicio();
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_lblHomeMouseClicked

    private void mostrarDatos (){
        ScoreService scores = ScoreService.getInstance();
        List<RunEntry> top = scores.topRuns(5);
        javax.swing.JLabel[] names = {lblnombre1, lblnombre2, lblnombre3, lblnombre4, lblnombre5};
        javax.swing.JLabel[] scoreLabels = {lblR1, lblR2, lblR3, lblR4, lblR5};
        for (int i = 0; i < names.length; i++) {
            if (i < top.size()) {
                RunEntry run = top.get(i);
                names[i].setText(run.getUsername());
                scoreLabels[i].setText(String.valueOf(run.getScore()));
                String tip = tipFor(run);
                names[i].setToolTipText(tip);
                scoreLabels[i].setToolTipText(tip);
            } else {
                names[i].setText("---");
                scoreLabels[i].setText("0");
                names[i].setToolTipText(null);
                scoreLabels[i].setToolTipText(null);
            }
        }

        List<RunEntry> recent = scores.history(5);
        for (int i = 0; i < histNames.length; i++) {
            if (i < recent.size()) {
                RunEntry run = recent.get(i);
                String when = DATE_FMT.format(run.getPlayedAt());
                histNames[i].setText(when + "  " + run.getUsername());
                histScores[i].setText(String.valueOf(run.getScore()));
                String tip = tipFor(run);
                histNames[i].setToolTipText(tip);
                histScores[i].setToolTipText(tip);
            } else {
                histNames[i].setText("---");
                histScores[i].setText("0");
                histNames[i].setToolTipText(null);
                histScores[i].setToolTipText(null);
            }
        }
    }
    
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
            java.util.logging.Logger.getLogger(Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Records().setVisible(true);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lblCerrar;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPlace;
    private javax.swing.JLabel lblR1;
    private javax.swing.JLabel lblR2;
    private javax.swing.JLabel lblR3;
    private javax.swing.JLabel lblR4;
    private javax.swing.JLabel lblR5;
    private javax.swing.JLabel lblScore;
    private javax.swing.JLabel lblnombre1;
    private javax.swing.JLabel lblnombre2;
    private javax.swing.JLabel lblnombre3;
    private javax.swing.JLabel lblnombre4;
    private javax.swing.JLabel lblnombre5;
    // End of variables declaration//GEN-END:variables
}
