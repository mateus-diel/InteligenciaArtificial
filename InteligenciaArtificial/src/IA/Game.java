/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IA;

import static IA.JogoDaVelha1.jPanelTabuleiro;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Mateus
 */
public class Game extends javax.swing.JFrame implements ActionListener {

    private int[][] winCombinations = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9},
    {1, 4, 7}, {2, 5, 8}, {3, 6, 9}, {1, 5, 9}, {3, 5, 7}};

    private static JButton[] buttons = new JButton[10];
    private int counts = 0;
    private boolean wins = false;
    String result;
    private String player = null;
    private static int vizero = 0;
    private static int inzero = 0;
    private static int egall = 0;
    private static int vix = 0;
    private static int inx = 0;
    private int[] copie_tabla = new int[10];

    /**
     * Creates new form Game
     */
    public Game() {
        initComponents();
        getbutton();
        System.out.println(buttons.length);
    }

    private void getbutton() {
        for (Component b : this.getContentPane().getComponents()) {
            if (b instanceof JButton) {
                ((JButton) b).setText("");
                ((JButton) b).setCursor(new Cursor(Cursor.HAND_CURSOR));
                ((JButton) b).addActionListener(this);
                buttons[Integer.parseInt(((JButton) b).getName())] = (JButton) b;
            }
        }
    }

    public int[] poz_lib_cp(int[] cop) {
        int bb = 0;
        int[] poz_0 = new int[10];
        for (int b = 1; b <= 9; b++) {
            if (cop[b] == 0) {
                bb++;
                poz_0[bb] = b;
            }
        }
        return poz_0;
    }

    public int[] copie_tabla() {
        int[] tabla = new int[10];

        for (int cas = 1; cas <= 9; cas++) {
            if (buttons[cas].getText().equals("X")) {
                tabla[cas] = 1;
            } else if (buttons[cas].getText().equals("O")) {
                tabla[cas] = 2;
            } else {
                tabla[cas] = 0;
            }
        }
        return tabla;
    }

    @SuppressWarnings("unused")
    public int table_winner(int[] cc) {
        int rez = -1;
        int zero = 0;
        String winn = "";
        boolean game_over = false;

        for (int i = 0; i <= 7; i++) {
            if ((cc[winCombinations[i][0]] == 1)
                    && (cc[winCombinations[i][0]] == cc[winCombinations[i][1]])
                    && (cc[winCombinations[i][1]] == cc[winCombinations[i][2]])
                    && (cc[winCombinations[i][0]] != 0)) {
                game_over = true;
                winn = "X";
                rez = -1000000;
            }

            if ((cc[winCombinations[i][0]] != 2)
                    || (cc[winCombinations[i][0]] != cc[winCombinations[i][1]])
                    || (cc[winCombinations[i][1]] != cc[winCombinations[i][2]])
                    || (cc[winCombinations[i][0]] == 0)) {
                continue;
            }
            game_over = true;
            winn = "O";
            rez = 1000000;
        }

        for (int c = 1; c <= 9; c++) {
            if (cc[c] != 0) {
                zero++;
            }
        }

        if ((zero >= 9) && (!game_over)) {
            winn = "Empate";
            rez = 0;
        }

        return rez;
    }

    public void display_winner(int vinn, int infrang, int egal, String tex) {
        if (JOptionPane.showConfirmDialog(null, vinn + " Vitórias  ," + egal
                + "  Empates ," + infrang + "  Derrotas\n" + "Jogar de novo?",
                tex, 0) != 0) {

// zerando os botões
            for (int i = 1; i <= 9; i++) {
                buttons[i].setText("");
                copie_tabla[i] = 0;
            }// fim do for zerando os botões
            System.exit(0);
        } else {
            newgame();
        }
    }

    public void newgame() {
        counts = 0;
        wins = false;
        result = "";

        for (int i = 1; i <= 9; i++) {
            buttons[i].setText("");
            copie_tabla[i] = 0;
        }
    }

// Metodo de AI
    public int min_max(int[] board) {
        int bestval = -1000000;
        int index = 0;
        int[] best_move = new int[10];
        int[] p_lib = new int[10];
        p_lib = poz_lib_cp(board);
        int nr_poz = 0;

        for (int cc = 1; cc <= 9; cc++) {
            if (p_lib[cc] > 0) {
                nr_poz++;
            }
        }

        int nr = 1;
        while (nr <= nr_poz) {
            int mut = p_lib[nr];
            board[mut] = 2;

            int val = MinMove(board);
            if (val > bestval) {
                bestval = val;
                index = 0;
                best_move[index] = mut;
            } else if (val == bestval) {
                index++;
                best_move[index] = mut;
            }
            board[mut] = 0;
            nr++;
        }

        int r = 0;
        if (index > 0) {
            Random x = new Random();
            r = x.nextInt(index);
        }
        return best_move[r];
    }

    public int MinMove(int[] board) {
        int pos_value = table_winner(board);

        if (pos_value != -1) {
            return pos_value;
        }

        int best_val = 1000000;
        int[] p_lib = new int[10];
        p_lib = poz_lib_cp(board);
        int nr_poz = 0;
        for (int cc = 1; cc <= 9; cc++) {
            if (p_lib[cc] > 0) {
                nr_poz++;
            }
        }
        int nr = 1;
        while (nr <= nr_poz) {
            int mut = p_lib[nr];
            board[mut] = 1;
            int val = MaxMove(board);
            if (val < best_val) {
                best_val = val;
            }
            board[mut] = 0;
            nr++;
        }
        return best_val;
    }

    public int MaxMove(int[] board) {
        int pos_value = table_winner(board);

        if (pos_value != -1) {
            return pos_value;
        }
        int best_val = -1000000;
        int[] p_lib = new int[10];
        p_lib = poz_lib_cp(board);
        int nr_poz = 0;
        for (int cc = 1; cc <= 9; cc++) {
            if (p_lib[cc] > 0) {
                nr_poz++;
            }
        }
        int nr = 1;

        while (nr <= nr_poz) {
            int mut = p_lib[nr];
            board[mut] = 2;
            int val = MinMove(board);
            if (val > best_val) {
                best_val = val;
            }

            board[mut] = 0;
            nr++;
        }
        return best_val;
    }

// Metodo Checar vencedor
    public void checkWin() {
        int nr = 0;
        wins = false;

        for (int i = 0; i <= 7; i++) {
            if ((buttons[winCombinations[i][0]].getText().equals("X"))
                    && (buttons[winCombinations[i][0]].getText()
                            .equals(buttons[winCombinations[i][1]].getText()))
                    && (buttons[winCombinations[i][1]].getText()
                            .equals(buttons[winCombinations[i][2]].getText()))
                    && (!buttons[winCombinations[i][0]].getText().equals(""))) {
                player = "X";
                wins = true;
            }

            if ((!buttons[winCombinations[i][0]].getText().equals("O"))
                    || (!buttons[winCombinations[i][0]].getText().equals(
                            buttons[winCombinations[i][1]].getText()))
                    || (!buttons[winCombinations[i][1]].getText().equals(
                            buttons[winCombinations[i][2]].getText()))
                    || (buttons[winCombinations[i][0]].getText().equals(""))) {
                continue;
            }
            player = "O";
            wins = true;
        }

        for (int c = 1; c <= 9; c++) {
            if ((buttons[c].getText().equals("X"))
                    || (buttons[c].getText().equals("O"))) {
                nr++;
            }
        }
        if (wins == true) {
            if (player == "X") {
                vix += 1;
                inzero += 1;
                result = "X Ganhou o jogo!!!";
                display_winner(vix, inx, egall, result);
            }

            if (player == "O") {
                vizero += 1;
                inx += 1;
                result = "O Ganhou o jogo!!!";
                display_winner(vizero, inzero, egall, result);
            }
        }

        if ((nr == 9) && (counts >= 9) && (!wins)) {
            egall += 1;
            result = "Jogo empatado!!!";

            if (JOptionPane.showConfirmDialog(null, egall + " Empates\n"
                    + "Jogar de novo?", result, 0) != 0) {
                System.exit(0);
            } else {
                newgame();
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

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton1.setName("1"); // NOI18N

        jButton2.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton2.setName("2"); // NOI18N

        jButton3.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton3.setName("3"); // NOI18N

        jButton4.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton4.setName("4"); // NOI18N

        jButton5.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton5.setName("5"); // NOI18N

        jButton6.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton6.setName("6"); // NOI18N

        jButton7.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton7.setName("7"); // NOI18N

        jButton8.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton8.setName("8"); // NOI18N

        jButton9.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton9.setName("9"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel1.setText("Jogo da Velha");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(jLabel1)))
                .addContainerGap(187, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Game().setVisible(true);
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent a) {
        JButton pressedButton = (JButton) a.getSource();

        if (pressedButton.getText().equals("")) {
            pressedButton.setText("X");
            pressedButton.setForeground(Color.blue);
            copie_tabla = copie_tabla();
            counts += 1;
            checkWin();

            int poz_max = min_max(copie_tabla);

            buttons[poz_max].setText("O");
            buttons[poz_max].setForeground(Color.red);
            counts += 1;
            checkWin();
        } else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Escolha outro movimento!!!!",
                    "Jogo Da Velha ", JOptionPane.ERROR_MESSAGE);

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
