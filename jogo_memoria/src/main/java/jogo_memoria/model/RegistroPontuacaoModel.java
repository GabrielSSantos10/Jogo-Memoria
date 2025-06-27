package jogo_memoria.model;

import java.util.Comparator;

public record  RegistroPontuacaoModel (String nomeJogador, int pontos, String tempoFormatado) implements Comparable<RegistroPontuacaoModel> {

    @Override
    public int compareTo(RegistroPontuacaoModel outro) {
        return Integer.compare(outro.pontos(), this.pontos);
    }

}
