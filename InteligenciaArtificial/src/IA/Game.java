/*
 * J. Marcos B.
 * https://isjavado.wordpress.com
 */
package IA;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
    
    //matriz das posições que gera vitoria
    private int[][] combinacoesParaVencer = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9},
    {1, 4, 7}, {2, 5, 8}, {3, 6, 9}, {1, 5, 9}, {3, 5, 7}};
    
    //vetor de botoes da tela
    private static JButton[] botoesFrame = new JButton[10];
    
    //variavel pra saber quantos movimentos foram feitos
    private int counts = 0;
    
    //variavel para controle do vencedor
    private boolean wins = false;
    
    //variavel que contem a string do vencedor; X ganhou; O ganhou
    String resultado;
    
    //string com o nome do jogador; X ou O
    private String player = null;
    
    //vitorias da bolinha
    private static int vitoriaBola = 0;
    //empate da bolinha
    private static int empateBola = 0;
    
    //total de empates
    private static int totalEmpate = 0;
    
    //vitoria do x
    private static int vitoriaX = 0;
    //empate do x
    private static int empateX = 0;
    
    //vetor auxiliar dos botoes
    private int[] copiaTabelaBotoes = new int[10];

    /**
     * Creates new form Game
     */
    public Game() {
        initComponents();
        
        //chama função para mapear os botoes do JFrame
        MapButtons();
    }

    //metodo copia os botoes do frame e coloca no vetor ordenado pelo nome do botao
    private void MapButtons() {
        for (Component b : this.getContentPane().getComponents()) {
            if (b instanceof JButton) {
                ((JButton) b).setText("");
                ((JButton) b).setCursor(new Cursor(Cursor.HAND_CURSOR));
                ((JButton) b).addActionListener(this);
                botoesFrame[Integer.parseInt(((JButton) b).getName())] = (JButton) b;
            }
        }
    }

    //verifica os botoes livre e retorna as respectivas posicoes
    public int[] posicaoBotoesLivre(int[] cop) {
        int index = 0;
        int[] posLivre = new int[10];
        for (int i = 1; i <= 9; i++) {
            if (cop[i] == 0) {
                index++;
                posLivre[index] = i;
            }
        }
        return posLivre;
    }

    /*faz uma copia dos botoes do Frame e verifica a situação deles, se ja foram
      preenchidos ou nao
    X equivale ao numero 1 e BOLINHA equivale ao numero 2
    caso nao tenha nada entao o valor na determinada posicao é ZERO*/
    public int[] copiaTabelaBotoes() {
        int[] tabela = new int[10];
        for (int i = 1; i <= 9; i++) {
            if (botoesFrame[i].getText().equals("X")) {
                tabela[i] = 1;
            } else if (botoesFrame[i].getText().equals("O")) {
                tabela[i] = 2;
            } else {
                tabela[i] = 0;
            }
        }
        return tabela;
    }
    
    @SuppressWarnings("unused")
    public int tabelaVencedor(int[] cc) {
        int res = -1;
        int zero = 0;
        String vencedor = "";
        boolean fimDeJogo = false;
        for (int i = 0; i <= 7; i++) {
            //verifica custos
            if ((cc[combinacoesParaVencer[i][0]] == 1)
                    && (cc[combinacoesParaVencer[i][0]] == cc[combinacoesParaVencer[i][1]])
                    && (cc[combinacoesParaVencer[i][1]] == cc[combinacoesParaVencer[i][2]])
                    && (cc[combinacoesParaVencer[i][0]] != 0)) {
                fimDeJogo = true;
                vencedor = "X";
                res = -1000000;
            }
            if ((cc[combinacoesParaVencer[i][0]] != 2)
                    || (cc[combinacoesParaVencer[i][0]] != cc[combinacoesParaVencer[i][1]])
                    || (cc[combinacoesParaVencer[i][1]] != cc[combinacoesParaVencer[i][2]])
                    || (cc[combinacoesParaVencer[i][0]] == 0)) {
                continue;
            }
            fimDeJogo = true;
            vencedor = "O";
            res = 1000000;
        }
        //percorre a tabela pra ver se só há 0
        for (int c = 1; c <= 9; c++) {
            if (cc[c] != 0) {
                zero++;
            }
        }
        //se tiver somente 0 é pq deu empate
        if ((zero >= 9) && (!fimDeJogo)) {
            vencedor = "Empate";
            res = 0;
        }
        return res;
    }

    //informa em um joptionpane quem ganhou
    public void exibeVencedor(int vitor, int derr, int emp, String tex) {
        attLabels();
        if (JOptionPane.showConfirmDialog(null, vitor + " Vitórias  ," + emp
                + "  Empates ," + derr + "  Derrotas\n" + "Jogar de novo?",
                tex, 0) != 0) {
            // zerando os botões
            for (int i = 1; i <= 9; i++) {
                botoesFrame[i].setText("");
                copiaTabelaBotoes[i] = 0;
            }// fim do for zerando os botões
            System.exit(0);
        } else {
            newgame();
        }
    }
    
    //atualiza os labels das vitórias
    public void attLabels() {
        lBolinha.setText(String.valueOf(vitoriaBola));
        lxis.setText(String.valueOf(vitoriaX));
    }

    //novo jogo, inicializa as variaveis para eliminar os antigos valores
    public void newgame() {
        counts = 0;
        wins = false;
        resultado = "";
        for (int i = 1; i <= 9; i++) {
            botoesFrame[i].setText("");
            copiaTabelaBotoes[i] = 0;
        }
    }

// Metodo de AI
    public int min_max(int[] tabela) {
        int melhorValor = -1000000;
        int index = 0;
        int[] melhorJogada = new int[10];
        int[] posicoesLivres = new int[10];
        posicoesLivres = posicaoBotoesLivre(tabela); //posicao dos botes que estao livres
        int numeroPosicao = 0;
        /*verifico quantas posicoes eu tenho livre, nao posso usar o length
        porque o vetor ja foi criado com tamanho 10*/
        for (int i = 1; i <= 9; i++) {
            if (posicoesLivres[i] > 0) {
                numeroPosicao++;
            }
        }        
        //cria vetor melhorJogada com base nas jogadas disponiveis
        int nr = 1;
        while (nr <= numeroPosicao) {
            int aux = posicoesLivres[nr];
            tabela[aux] = 2;

            int val = MinMove(tabela);
            if (val > melhorValor) {
                melhorValor = val;
                index = 0;
                melhorJogada[index] = aux;
            } else if (val == melhorValor) {
                index++;
                melhorJogada[index] = aux;
            }
            tabela[aux] = 0;
            nr++;
        }
        /*se tiver mais uma opção de jogada disponivel faz um random e joga
        senao retorna a melhor possivel*/
        int r = 0;
        if (index > 0) {
            Random x = new Random();
            r = x.nextInt(index);
        }
        return melhorJogada[r];
    }

    public int MinMove(int[] tabela) {
        int pos_value = tabelaVencedor(tabela);
        if (pos_value != -1) {
            return pos_value;
        }
        int best_val = 1000000;
        int[] posicoesLivres = new int[10];
        posicoesLivres = posicaoBotoesLivre(tabela);
        int numeroPosicao = 0;
        for (int cc = 1; cc <= 9; cc++) {
            if (posicoesLivres[cc] > 0) {
                numeroPosicao++;
            }
        }
        int nr = 1;
        while (nr <= numeroPosicao) {
            int aux = posicoesLivres[nr];
            tabela[aux] = 1;
            int val = MaxMove(tabela);
            if (val < best_val) {
                best_val = val;
            }
            tabela[aux] = 0;
            nr++;
        }
        return best_val;
    }

    public int MaxMove(int[] tabela) {
        int pos_value = tabelaVencedor(tabela);
        if (pos_value != -1) {
            return pos_value;
        }
        int best_val = -1000000;
        int[] posicoesLivres = new int[10];
        posicoesLivres = posicaoBotoesLivre(tabela);
        int numeroPosicao = 0;
        for (int cc = 1; cc <= 9; cc++) {
            if (posicoesLivres[cc] > 0) {
                numeroPosicao++;
            }
        }
        int nr = 1;
        while (nr <= numeroPosicao) {
            int aux = posicoesLivres[nr];
            tabela[aux] = 2;
            int val = MinMove(tabela);
            if (val > best_val) {
                best_val = val;
            }
            tabela[aux] = 0;
            nr++;
        }
        return best_val;
    }

// Metodo Checar vencedor
    public void checkWin() {
        int nr = 0;
        wins = false;
        for (int i = 0; i <= 7; i++) {
            if ((botoesFrame[combinacoesParaVencer[i][0]].getText().equals("X"))
                    && (botoesFrame[combinacoesParaVencer[i][0]].getText()
                            .equals(botoesFrame[combinacoesParaVencer[i][1]].getText()))
                    && (botoesFrame[combinacoesParaVencer[i][1]].getText()
                            .equals(botoesFrame[combinacoesParaVencer[i][2]].getText()))
                    && (!botoesFrame[combinacoesParaVencer[i][0]].getText().equals(""))) {
                player = "X";
                wins = true;
            }
            if ((!botoesFrame[combinacoesParaVencer[i][0]].getText().equals("O"))
                    || (!botoesFrame[combinacoesParaVencer[i][0]].getText().equals(
                            botoesFrame[combinacoesParaVencer[i][1]].getText()))
                    || (!botoesFrame[combinacoesParaVencer[i][1]].getText().equals(
                            botoesFrame[combinacoesParaVencer[i][2]].getText()))
                    || (botoesFrame[combinacoesParaVencer[i][0]].getText().equals(""))) {
                continue;
            }
            player = "O";
            wins = true;
        }
        //variavel nr obtem o numero total de jogadas
        for (int i = 1; i <= 9; i++) {
            if ((botoesFrame[i].getText().equals("X"))
                    || (botoesFrame[i].getText().equals("O"))) {
                nr++;
            }
        }        
        //define a pontuacao dos jogadores e exibe o vencedor
        if (wins == true) {
            if (player == "X") {
                vitoriaX += 1;
                empateBola += 1;
                resultado = "X Ganhou o jogo!!!";
                exibeVencedor(vitoriaX, empateX, totalEmpate, resultado);
            }
            if (player == "O") {
                vitoriaBola += 1;
                empateX += 1;
                resultado = "O Ganhou o jogo!!!";
                exibeVencedor(vitoriaBola, empateBola, totalEmpate, resultado);
            }
        }
        //caso ninguem ganhou o jogo, entao há empate
        if ((nr == 9) && (counts >= 9) && (!wins)) {
            totalEmpate += 1;
            resultado = "Jogo empatado!!!";
            if (JOptionPane.showConfirmDialog(null, totalEmpate + " Empates\n"
                    + "Jogar de novo?", resultado, 0) != 0) {
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lxis = new javax.swing.JLabel();
        lBolinha = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inteligência Artificial");

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

        jLabel2.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel2.setText("O = ");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel3.setText("X = ");

        lxis.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        lxis.setText("0");

        lBolinha.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        lBolinha.setText("0");

        jLabel4.setText("Ciência da Computação - UNIJUI");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
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
                                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lxis))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lBolinha))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel4)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
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
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(lBolinha))
                        .addGap(67, 67, 67)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lxis))
                        .addGap(34, 34, 34)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void actionPerformed(ActionEvent a) {
        //qual botao foi pressionado
        JButton pressedButton = (JButton) a.getSource();

        //se clicou em um botao vazio
        if (pressedButton.getText().equals("")) {
            //define X para o botao e verifica se ganhou
            pressedButton.setText("X");
            pressedButton.setForeground(Color.blue);
            copiaTabelaBotoes = copiaTabelaBotoes();
            counts += 1;
            checkWin();

            //se nao ganhou entao é a vez do bolinha jogar, obviamente na melhor posicao
            int poz_max = min_max(copiaTabelaBotoes);
            botoesFrame[poz_max].setText("O");
            botoesFrame[poz_max].setForeground(Color.red);
            counts += 1;
            checkWin();
        } else {
            //se clicou em um botao que ja foi clicado anteriormente...
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lBolinha;
    private javax.swing.JLabel lxis;
    // End of variables declaration//GEN-END:variables
}
