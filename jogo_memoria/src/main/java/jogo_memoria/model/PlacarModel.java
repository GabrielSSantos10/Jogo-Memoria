package jogo_memoria.model;

import jogo_memoria.model.RegistroPontuacaoModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacarModel {
    private final List<RegistroPontuacaoModel> melhoresPontuacoes = new ArrayList<>();
    private static final int MAX_PONTUACOES = 3;

    public void adicionarPontuacao(RegistroPontuacaoModel novoRegistro) {
        melhoresPontuacoes.add(novoRegistro);
        Collections.sort(melhoresPontuacoes);
        if (melhoresPontuacoes.size() > MAX_PONTUACOES) {
            melhoresPontuacoes.remove(MAX_PONTUACOES);
        }
    }

    public List<RegistroPontuacaoModel> getMelhoresPontuacoes() {
        return Collections.unmodifiableList(melhoresPontuacoes);
    }
}
