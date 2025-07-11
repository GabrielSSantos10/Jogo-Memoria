package jogo_memoria.model;

public class RegistroPontuacaoModel implements Comparable<RegistroPontuacaoModel>, java.io.Serializable {
    private String nomeJogador;
    private int pontos;

    public RegistroPontuacaoModel(String nomeJogador, int pontos) {
        this.nomeJogador = nomeJogador;
        this.pontos = pontos;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public int getPontos() {
        return pontos;
    }

    @Override
    public int compareTo(RegistroPontuacaoModel outro) {
        return Integer.compare(outro.pontos, this.pontos);
    }
}
