package jogo_memoria.model;

import jogo_memoria.exception.RecursoNaoEncontradoException;
import jogo_memoria.model.CartaModel;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JogoModel {
    private String nomeJogadorAtual;
    private final List<CartaModel> cartas = new ArrayList<>();
    private int paresEncontrados = 0;
    private static final int TOTAL_PARES = 8;

    public void iniciarNovoJogo() throws RecursoNaoEncontradoException {
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

    public String getNomeJogadorAtual() { return nomeJogadorAtual; }
    public void setNomeJogadorAtual(String nomeJogadorAtual) { this.nomeJogadorAtual = nomeJogadorAtual; }
    public List<CartaModel> getCartas() { return Collections.unmodifiableList(cartas); }
    public int getParesEncontrados() { return paresEncontrados; }
    public void incrementarParesEncontrados() { this.paresEncontrados++; }
    public int getTotalPares() { return TOTAL_PARES; }
}
