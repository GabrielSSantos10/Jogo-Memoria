package jogo_memoria.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacarModel {
    private static final String ARQUIVO_PLACAR = "melhores_pontuacoes.dat";
    private List<RegistroPontuacaoModel> melhoresPontuacoes;

    public PlacarModel() {
        melhoresPontuacoes = carregarPontuacoes();
    }

    public void adicionarPontuacao(RegistroPontuacaoModel novoRegistro) {
        melhoresPontuacoes.add(novoRegistro);
        Collections.sort(melhoresPontuacoes); // Ordena pela pontuação (maior primeiro)
        if (melhoresPontuacoes.size() > 3) { // Mantém apenas as 3 melhores
            melhoresPontuacoes = melhoresPontuacoes.subList(0, 3);
        }
        salvarPontuacoes(melhoresPontuacoes);
    }

    public List<RegistroPontuacaoModel> getMelhoresPontuacoes() {
        return melhoresPontuacoes;
    }

    private List<RegistroPontuacaoModel> carregarPontuacoes() {
        List<RegistroPontuacaoModel> pontuacoesCarregadas = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PLACAR))) {
            pontuacoesCarregadas = (List<RegistroPontuacaoModel>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de placar não encontrado. Criando um novo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pontuacoesCarregadas;
    }

    private void salvarPontuacoes(List<RegistroPontuacaoModel> pontuacoes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PLACAR))) {
            oos.writeObject(pontuacoes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
