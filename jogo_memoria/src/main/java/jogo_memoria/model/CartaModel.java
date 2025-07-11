package jogo_memoria.model;

public class CartaModel {

    private final String caminhoImagem;
    private boolean foiCombinada = false;
    private boolean virada = false;

    public CartaModel(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public boolean foiCombinada() {
        return foiCombinada;
    }

    public void setFoiCombinada(boolean foiCombinada) {
        this.foiCombinada = foiCombinada;
    }

    public boolean isVirada() {
        return virada;
    }

    public void setVirada(boolean virada) {
        this.virada = virada;
    }
}