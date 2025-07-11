package jogo_memoria.model;

import jogo_memoria.exception.RecursoNaoEncontradoException;
import jogo_memoria.model.CartaModel;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JogoModel {
    private String nomeJogador1;
    private String nomeJogador2;
    private int pontuacaoJogador1;
    private int pontuacaoJogador2;
    private int jogadorAtual;
    private final List<CartaModel> cartas = new ArrayList<>();
    private int paresEncontrados = 0;
    private static final int TOTAL_PARES = 8;

    public void iniciarNovoJogo(String nomeJ1, String nomeJ2) throws RecursoNaoEncontradoException {
        this.nomeJogador1 = nomeJ1;
        this.nomeJogador2 = nomeJ2;
        this.pontuacaoJogador1 = 0;
        this.pontuacaoJogador2 = 0;
        this.jogadorAtual = 1; // Começa com o jogador 1
        paresEncontrados = 0;
        cartas.clear();
        List<String> caminhosImagens = new ArrayList<>();
        for (int i = 1; i <= TOTAL_PARES; i++) {
            String caminho = "/images/flower" + i + ".jpg";

            // Tratamento de erro para verificar se a imagem existe
            try (InputStream stream = getClass().getResourceAsStream(caminho)) {
                if (stream == null) {
                    throw new RecursoNaoEncontradoException("Não foi possível encontrar a imagem: " + caminho);
                }
            } catch (java.io.IOException e) {
                throw new RecursoNaoEncontradoException("Erro ao ler o recurso: " + caminho);
            }

            caminhosImagens.add(caminho);
            caminhosImagens.add(caminho); // Adiciona o par
        }
        Collections.shuffle(caminhosImagens);
        for (String caminho : caminhosImagens) {
            cartas.add(new CartaModel(caminho));
        }
    }

    public String getNomeJogador1() { return nomeJogador1; }
    public String getNomeJogador2() { return nomeJogador2; }
    public int getPontuacaoJogador1() { return pontuacaoJogador1; }
    public int getPontuacaoJogador2() { return pontuacaoJogador2; }
    public int getJogadorAtual() { return jogadorAtual; }
    public void proximoJogador() { this.jogadorAtual = (this.jogadorAtual == 1) ? 2 : 1; }
    public void incrementarPontuacaoJogadorAtual() {
        if (jogadorAtual == 1) {
            pontuacaoJogador1 += 10;
        } else {
            pontuacaoJogador2 += 10;
        }
    }
    public String getNomeJogadorAtual() { return (jogadorAtual == 1) ? nomeJogador1 : nomeJogador2; }
    public List<CartaModel> getCartas() { return Collections.unmodifiableList(cartas); }
    public int getParesEncontrados() { return paresEncontrados; }
    public void incrementarParesEncontrados() { this.paresEncontrados++; }
    public int getTotalPares() { return TOTAL_PARES; }
}
