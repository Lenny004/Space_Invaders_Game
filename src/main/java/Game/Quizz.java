/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Tipografia.Fuente;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Random;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author lenny
 */
public class Quizz extends javax.swing.JFrame {
    private static int Random, count = 1;
    private static Boolean[] ArrayNoPreguntado = new Boolean[11];
    public static int valor = 0;
    public static int bonus = 0;
    
    Fuente TipoFuente = new Fuente();
    
    //Arreglo para las (11)  preguntas
    //Arreglo para las (11)  preguntas
    private static String[] Pregunta = new String[11];
    private static String[] Respuesta = new String[11];
    private static String[][] PosibleRespuesta = new String[11][4];
    Color GrisGame = new Color(153,153,153);
    Color CelesteGame = new Color(40, 255, 240);
    Color LetrasGris = new Color(51,51,51);
    Color LetrasAzul = new Color(31,75,142);
    FondoGameQ fondo = new FondoGameQ();
    
    public Quizz() {
        this.setContentPane(fondo);
        initComponents();     
        JTAPregunta.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 16));
        lblOpciones.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 12));
        btnRespuesta1.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        btnRespuesta2.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        btnRespuesta3.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
        btnRespuesta4.setFont(TipoFuente.fuenteSpace(TipoFuente.SpaceInvaders, 0, 14));
    }
    
    public static void ReiniciarQuizz(){
        valor = 0;
        
        for (int i = 0; i < 10; i ++){
            ArrayNoPreguntado[i] = false;
        }
         
        Pregunta[0] = "\n¿Cual es la formula quimica del agua?";
        Respuesta[0] = "H2O";
        
        String[] temporal0 = {"H2O", "CO2", "C6H12O6", "Cu"};
        PosibleRespuesta[0] = temporal0.clone();
        
        Pregunta[1] = "\n¿Quien fue el inventor del TNT?";
        Respuesta[1] = "Alfred Nobel";
        String[] temporal1 = {"Alfred Nobel", "Isaac Newton", "Albert Einstein", "Erwin Schrodinger"};
        PosibleRespuesta[1] = temporal1.clone();
        
        Pregunta[2] = "\n¿Cual de los siguientes elementos\n\nse usa como combustible en los reactores\n\nnucleares?";
        Respuesta[2] = "Uranio";
        String[] temporal2 = {"Uranio", "Helio", "Zinc", "Cobre"};
        PosibleRespuesta[2] = temporal2.clone();
        
        Pregunta[3] = "\n¿Cual de los siguientes hidrocarburos\nes usado en la cocina?";
        Respuesta[3] = "Propano";
        String[] temporal3 = {"Propano", "Benceno", "Bronce", "Hidrogeno"};
        PosibleRespuesta[3] = temporal3.clone();
      
        Pregunta[4] = "\n¿Cual de los siguientes elementos\n\nconforma el bronce?";
        Respuesta[4] = "Cobre y zinc";
        String[] temporal4 = {"Cobre y zinc", "Hierro y cobre", "Cobre y carbono", "Carbono e hidrogeno"};
        PosibleRespuesta[4] = temporal4.clone();
        
        Pregunta[5] = "\n¿Cual de los siguientes metales es\n\nmas denso?";
        Respuesta[5] = "Tungsteno";
        String[] temporal5 = {"Tungsteno", "Hierro", "Titanio", "Cobre"};
        PosibleRespuesta[5] = temporal5.clone();
    
        Pregunta[6] = "\n¿Cual de los siguientes compuestos\n\nse usa para la fabricacion de TNT?";
        Respuesta[6] = "Acido nítrico";
        String[] temporal6 = {"Acido nitrico", "Acido sulfurico", "Dinamita", "Agua"};
        PosibleRespuesta[6] = temporal6.clone();

        Pregunta[7] = "\n¿Cual de los siguientes compuestos/\n\nelementos es menos denso que el Aire?";
        Respuesta[7] = "Helio";
        String[] temporal7 = {"Helio", "Butano", "Propano", "Tungsteno"};
        PosibleRespuesta[7] = temporal7.clone();
        
        Pregunta[8] = "\n¿Cual es la formula quimica del benceno?";
        Respuesta[8] = "C6H6";
        String[] temporal8 = {"C6H6", "C4H10", "C3H8", "H2O"};
        PosibleRespuesta[8] = temporal8.clone();
        
        Pregunta[9] = "\n¿Cual de los siguientes elementos/\n\ncompuestos se encuentra en zonas volcanicas?";
        Respuesta[9] = "Azufre";
        String[] temporal9 = {"Azufre", "Helio", "Uranio", "Tungsteno"};
        PosibleRespuesta[9] = temporal9.clone();
        
        Pregunta[10] = "\n¿Cual de los siguientes elementos es\n\nradioactivo?";
        Respuesta[10] = "Uranio";
        String[] temporal10 = {"Uranio", "Hierro", "Cobre", "Bronce"};
        PosibleRespuesta[10] = temporal10.clone();
    }
    
    public void MetodoPreguntas(){
        GameFrame.PausarJuego();
        
        boolean agregado = false;
  
        do{
            try{
                Random  rnd = new Random();
                Random = Math.abs(rnd.nextInt())%11;
                if(ArrayNoPreguntado[Random] == false){
                    agregado = true;
                    JTAPregunta.setText(Pregunta[Random]);
                    JTAPregunta.setEditable(false);
                    ArrayNoPreguntado[Random] = true;
                    
                    int Random2 = Math.abs(rnd.nextInt())%4;
                    System.out.println(Random2);
                    String[] Orden = {PosibleRespuesta[Random][ (0+Random2)%4 ], PosibleRespuesta[Random][ (1+Random2)%4  ], PosibleRespuesta[Random][ (2+Random2)%4  ], PosibleRespuesta[Random][ (3+Random2)%4  ]};

                    btnRespuesta1.setText( Orden[0] );
                    btnRespuesta2.setText( Orden[1] );
                    btnRespuesta3.setText( Orden[2] );
                    btnRespuesta4.setText( Orden[3] );
                    valor++;
                }
            }
            catch(Exception e){
            }
        }while (!agregado);    
    }
    
    public void ComprobarRespuesta(String r){
        if(r.equals(Respuesta[Random])){
            bonus += 100;
            JOptionPane.showConfirmDialog(this, "Respuesta Correcta! +100 puntos", "Resultado", JOptionPane.DEFAULT_OPTION);
        }
        else{
            JOptionPane.showConfirmDialog(this, "Respuesta Incorrecta +0 puntos", "Resultado", JOptionPane.DEFAULT_OPTION);
        }
        if(valor < 3){
            MetodoPreguntas();
        }
        else{
            setVisible(false);
            GameFrame.ReanudarJuego();
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
            // No pintamos el borde
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
    
    public void IniciarArreglo(){
        for (int i = 0; i < 10; i ++){
            ArrayNoPreguntado[i] = false;
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

        btnRespuesta1 = new javax.swing.JButton();
        btnRespuesta2 = new javax.swing.JButton();
        btnRespuesta3 = new javax.swing.JButton();
        btnRespuesta4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTAPregunta = new javax.swing.JTextArea();
        lblOpciones = new javax.swing.JLabel();
        lblNave = new javax.swing.JLabel();
        lblCerrar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(944, 525));
        setMinimumSize(new java.awt.Dimension(944, 525));
        setUndecorated(true);
        setSize(new java.awt.Dimension(944, 525));

        btnRespuesta1.setBackground(new java.awt.Color(153, 153, 153));
        btnRespuesta1.setForeground(new java.awt.Color(51, 51, 51));
        btnRespuesta1.setText("jButton1");
        btnRespuesta1.setFocusable(false);
        btnRespuesta1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRespuesta1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRespuesta1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRespuesta1MouseExited(evt);
            }
        });

        btnRespuesta2.setBackground(new java.awt.Color(153, 153, 153));
        btnRespuesta2.setForeground(new java.awt.Color(51, 51, 51));
        btnRespuesta2.setText("jButton1");
        btnRespuesta2.setFocusable(false);
        btnRespuesta2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRespuesta2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRespuesta2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRespuesta2MouseExited(evt);
            }
        });

        btnRespuesta3.setBackground(new java.awt.Color(153, 153, 153));
        btnRespuesta3.setForeground(new java.awt.Color(51, 51, 51));
        btnRespuesta3.setText("jButton1");
        btnRespuesta3.setFocusable(false);
        btnRespuesta3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRespuesta3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRespuesta3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRespuesta3MouseExited(evt);
            }
        });

        btnRespuesta4.setBackground(new java.awt.Color(153, 153, 153));
        btnRespuesta4.setForeground(new java.awt.Color(51, 51, 51));
        btnRespuesta4.setText("jButton1");
        btnRespuesta4.setFocusable(false);
        btnRespuesta4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRespuesta4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRespuesta4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRespuesta4MouseExited(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        JTAPregunta.setBackground(new java.awt.Color(79, 153, 173));
        JTAPregunta.setColumns(20);
        JTAPregunta.setRows(5);
        jScrollPane1.setViewportView(JTAPregunta);

        lblOpciones.setForeground(new java.awt.Color(255, 255, 255));
        lblOpciones.setText("Opciones:");

        lblNave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/NaveMediana.gif"))); // NOI18N

        lblCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Close.png"))); // NOI18N
        lblCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnRespuesta4, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnRespuesta1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                                .addComponent(btnRespuesta2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(59, 59, 59))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblOpciones)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(135, 135, 135)
                        .addComponent(lblNave, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCerrar)
                        .addGap(18, 18, 18))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(48, 48, 48)
                    .addComponent(btnRespuesta3, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(516, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblCerrar)
                        .addGap(176, 176, 176))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNave, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(lblOpciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRespuesta1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRespuesta2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74)
                .addComponent(btnRespuesta4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(399, Short.MAX_VALUE)
                    .addComponent(btnRespuesta3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(70, 70, 70)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblCerrarMouseClicked

    private void btnRespuesta1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta1MouseClicked
        ComprobarRespuesta(btnRespuesta1.getText());
    }//GEN-LAST:event_btnRespuesta1MouseClicked

    private void btnRespuesta2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta2MouseClicked
        ComprobarRespuesta(btnRespuesta2.getText());
    }//GEN-LAST:event_btnRespuesta2MouseClicked

    private void btnRespuesta3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta3MouseClicked
        ComprobarRespuesta(btnRespuesta3.getText());
    }//GEN-LAST:event_btnRespuesta3MouseClicked

    private void btnRespuesta4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta4MouseClicked
        ComprobarRespuesta(btnRespuesta4.getText());
    }//GEN-LAST:event_btnRespuesta4MouseClicked

    private void btnRespuesta1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta1MouseEntered
        btnRespuesta1.setBackground(CelesteGame);
        btnRespuesta1.setForeground(LetrasAzul);
    }//GEN-LAST:event_btnRespuesta1MouseEntered

    private void btnRespuesta1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta1MouseExited
        btnRespuesta1.setBackground(GrisGame);
        btnRespuesta1.setForeground(LetrasGris);
    }//GEN-LAST:event_btnRespuesta1MouseExited

    private void btnRespuesta2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta2MouseEntered
        btnRespuesta2.setBackground(CelesteGame);
        btnRespuesta2.setForeground(LetrasAzul);
    }//GEN-LAST:event_btnRespuesta2MouseEntered

    private void btnRespuesta2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta2MouseExited
        btnRespuesta2.setBackground(GrisGame);
        btnRespuesta2.setForeground(LetrasGris);
    }//GEN-LAST:event_btnRespuesta2MouseExited

    private void btnRespuesta3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta3MouseEntered
        btnRespuesta3.setBackground(CelesteGame);
        btnRespuesta3.setForeground(LetrasAzul);
    }//GEN-LAST:event_btnRespuesta3MouseEntered

    private void btnRespuesta3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta3MouseExited
        btnRespuesta3.setBackground(GrisGame);
        btnRespuesta3.setForeground(LetrasGris);
    }//GEN-LAST:event_btnRespuesta3MouseExited

    private void btnRespuesta4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta4MouseEntered
        btnRespuesta4.setBackground(CelesteGame);
        btnRespuesta4.setForeground(LetrasAzul);
    }//GEN-LAST:event_btnRespuesta4MouseEntered

    private void btnRespuesta4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRespuesta4MouseExited
        btnRespuesta4.setBackground(GrisGame);
        btnRespuesta4.setForeground(LetrasGris);
    }//GEN-LAST:event_btnRespuesta4MouseExited

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
            java.util.logging.Logger.getLogger(Quizz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Quizz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Quizz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Quizz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Quizz().setVisible(true);
            }
        });
    }
    
    private class FondoGameQ extends JPanel {
        public Image imagen;
        public void paint(Graphics g) {
            imagen = new ImageIcon(getClass().getResource("/Imagenes/Space1.gif")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea JTAPregunta;
    private javax.swing.JButton btnRespuesta1;
    private javax.swing.JButton btnRespuesta2;
    private javax.swing.JButton btnRespuesta3;
    private javax.swing.JButton btnRespuesta4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCerrar;
    private javax.swing.JLabel lblNave;
    private javax.swing.JLabel lblOpciones;
    // End of variables declaration//GEN-END:variables
}
