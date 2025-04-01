import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

// Classe principal que representa o jogo da memória
public class JogoDaMemoria extends JFrame {
    // Componentes da interface
    private JButton botaoIniciar;
    private JTextField campoNomeJogador;
    private JComboBox<Dificuldade> seletorDificuldade;
    private JLabel rotuloRodada, rotuloPontuacao;
    private JPanel painelGrade;
    private List<JButton> botoesGrade;
    
    // Estado do jogo
    private int rodadaAtual = 1;
    private int pontuacao = 0;
    private List<Integer> sequenciaAtual;
    private List<Integer> sequenciaJogador;
    private boolean jogando = false;
    private Dificuldade dificuldadeAtual;

    public JogoDaMemoria() {
        inicializarInterface();
    }

    // Configura a interface gráfica
    private void inicializarInterface() {
        setTitle("Jogo da Memória");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel superior com controles
        JPanel painelSuperior = new JPanel();
        campoNomeJogador = new JTextField(10);
        seletorDificuldade = new JComboBox<>(Dificuldade.values());
        botaoIniciar = new JButton("Iniciar Jogo");
        rotuloRodada = new JLabel("Rodada: 1");
        rotuloPontuacao = new JLabel("Pontuação: 0");

        // Adiciona componentes ao painel superior
        painelSuperior.add(new JLabel("Nome:"));
        painelSuperior.add(campoNomeJogador);
        painelSuperior.add(new JLabel("Dificuldade:"));
        painelSuperior.add(seletorDificuldade);
        painelSuperior.add(botaoIniciar);
        painelSuperior.add(rotuloRodada);
        painelSuperior.add(rotuloPontuacao);
        add(painelSuperior, BorderLayout.NORTH);

        // Cria grade de 3x3 botões
        painelGrade = new JPanel(new GridLayout(3, 3));
        botoesGrade = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            JButton botao = new JButton();
            botao.setBackground(Color.GRAY);
            botao.setOpaque(true);
            botao.setBorderPainted(false);
            botao.addActionListener(e -> manipularCliqueBotaoGrade(botao));
            botoesGrade.add(botao);
            painelGrade.add(botao);
        }
        add(painelGrade, BorderLayout.CENTER);

        botaoIniciar.addActionListener(e -> iniciarJogo());

        pack();
        setSize(600, 400);
        setVisible(true);
    }

    // Inicia novo jogo
    private void iniciarJogo() {
        String nomeJogador = campoNomeJogador.getText().trim();
        if (nomeJogador.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira seu nome!");
            return;
        }
        
        // Reinicia estado do jogo
        dificuldadeAtual = (Dificuldade) seletorDificuldade.getSelectedItem();
        rodadaAtual = 1;
        pontuacao = 0;
        atualizarRotulos();
        
        // Desabilita controles durante o jogo
        campoNomeJogador.setEnabled(false);
        seletorDificuldade.setEnabled(false);
        botaoIniciar.setEnabled(false);
        
        jogando = false;
        gerarNovaSequencia();
        exibirSequencia();
    }

    // Gera nova sequência aleatória
    private void gerarNovaSequencia() {
        sequenciaAtual = new ArrayList<>();
        Random aleatorio = new Random();
        int tamanhoSequencia = 2 + (rodadaAtual - 1); // Aumenta a cada rodada
        
        for (int i = 0; i < tamanhoSequencia; i++) {
            int indiceBotao = aleatorio.nextInt(9);
            sequenciaAtual.add(indiceBotao);
        }
    }

    // Exibe sequência para o jogador
    private void exibirSequencia() {
        desativarBotoesGrade();
        jogando = false;

        AtomicInteger indiceAtual = new AtomicInteger(0);
        Timer temporizador = new Timer(0, null);
        temporizador.setRepeats(true);

        // Temporizador para exibir sequência com intervalos
        temporizador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (indiceAtual.get() >= sequenciaAtual.size()) {
                    temporizador.stop();
                    ativarBotoesGrade();
                    jogando = true; // Permite jogador interagir
                    return;
                }

                // Ativa/desativa botão da sequência
                int indiceBotao = sequenciaAtual.get(indiceAtual.get());
                ativarBotao(indiceBotao);

                Timer temporizadorDesativacao = new Timer(500, e2 -> {
                    desativarBotao(indiceBotao);
                });
                temporizadorDesativacao.setRepeats(false);
                temporizadorDesativacao.start();

                indiceAtual.incrementAndGet();
                temporizador.setDelay(dificuldadeAtual.getIntervalo());
            }
        });

        temporizador.setInitialDelay(0);
        temporizador.start();
    }

    // Ativa (muda cor) botão específico
    private void ativarBotao(int indice) {
        botoesGrade.get(indice).setBackground(Color.GREEN);
    }

    // Desativa (reset cor) botão específico
    private void desativarBotao(int indice) {
        botoesGrade.get(indice).setBackground(Color.GRAY);
    }

    // Habilita/desabilita interação com botões
    private void desativarBotoesGrade() {
        botoesGrade.forEach(botao -> botao.setEnabled(false));
    }

    private void ativarBotoesGrade() {
        botoesGrade.forEach(botao -> botao.setEnabled(true));
        sequenciaJogador = new ArrayList<>(); // Prepara para nova entrada
    }

    // Processa cliques do jogador
    private void manipularCliqueBotaoGrade(JButton botao) {
        if (!jogando) return;

        int indice = botoesGrade.indexOf(botao);
        sequenciaJogador.add(indice);
        botao.setBackground(Color.BLUE); // Feedback visual

        // Verifica se sequência está incorreta
        if (sequenciaJogador.size() > sequenciaAtual.size()) {
            fimDeJogo();
            return;
        }

        // Compara cada elemento da sequência
        for (int i = 0; i < sequenciaJogador.size(); i++) {
            if (!sequenciaJogador.get(i).equals(sequenciaAtual.get(i))) {
                fimDeJogo();
                return;
            }
        }

        // Se sequência completa correta, avança rodada
        if (sequenciaJogador.size() == sequenciaAtual.size()) {
            pontuacao += sequenciaAtual.size() * dificuldadeAtual.getMultiplicadorPontuacao();
            rodadaAtual++;
            atualizarRotulos();
            gerarNovaSequencia();
            exibirSequencia();
        }
    }

    // Atualiza display de rodada e pontuação
    private void atualizarRotulos() {
        rotuloRodada.setText("Rodada: " + rodadaAtual);
        rotuloPontuacao.setText("Pontuação: " + pontuacao);
    }

    // Finaliza jogo e mostra pontuação
    private void fimDeJogo() {
        jogando = false;
        desativarBotoesGrade();
        JOptionPane.showMessageDialog(this, "Fim de jogo! Pontuação: " + pontuacao);
        GerenciadorPontuacao.adicionarPontuacao(campoNomeJogador.getText(), pontuacao);
        reiniciarJogo();
    }

    // Reseta controles para novo jogo
    private void reiniciarJogo() {
        campoNomeJogador.setEnabled(true);
        seletorDificuldade.setEnabled(true);
        botaoIniciar.setEnabled(true);
        sequenciaAtual.clear();
        botoesGrade.forEach(botao -> {
            botao.setBackground(Color.GRAY);
            botao.setEnabled(false);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JogoDaMemoria::new);
    }
}

// Enum que define os níveis de dificuldade
enum Dificuldade {
    FACIL(1000, 100) {  // Intervalo maior, multiplicador menor
        @Override
        public String toString() { return "Fácil"; }
    },
    MEDIO(750, 150) {
        @Override
        public String toString() { return "Médio"; }
    },
    DIFICIL(500, 200) {  // Intervalo menor, multiplicador maior
        @Override
        public String toString() { return "Difícil"; }
    };

    private final int intervalo;  // Tempo entre ativações (ms)
    private final int multiplicadorPontuacao;  // Multiplicador de pontos

    Dificuldade(int intervalo, int multiplicadorPontuacao) {
        this.intervalo = intervalo;
        this.multiplicadorPontuacao = multiplicadorPontuacao;
    }

    public int getIntervalo() { return intervalo; }
    public int getMultiplicadorPontuacao() { return multiplicadorPontuacao; }
}

// Gerencia o registro de pontuações
class GerenciadorPontuacao {
    private static List<Pontuacao> pontuacoes = new ArrayList<>();

    public static void adicionarPontuacao(String nome, int pontuacao) {
        pontuacoes.add(new Pontuacao(nome, pontuacao));
    }

    // Classe interna para armazenar pontuações
    private static class Pontuacao {
        String nome;
        int pontuacao;

        public Pontuacao(String nome, int pontuacao) {
            this.nome = nome;
            this.pontuacao = pontuacao;
        }
    }
}