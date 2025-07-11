package jogo_memoria.controller;

import jogo_memoria.exception.RecursoNaoEncontradoException;
import jogo_memoria.model.JogoModel;
import jogo_memoria.model.PlacarModel;
import jogo_memoria.model.CartaModel;
import jogo_memoria.model.RegistroPontuacaoModel;
import jogo_memoria.view.JogoView;
import jogo_memoria.view.CartaView;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;

public class JogoController {
    private final JogoModel modeloJogo;
    private final PlacarModel modeloPlacar;
    private final JogoView visao;
    private Timeline cronometro;
    private long tempoInicial;
    private CartaView primeiraCarta, segundaCarta;

    public JogoController(JogoModel modeloJogo, PlacarModel modeloPlacar, JogoView visao) {
        this.modeloJogo = modeloJogo;
        this.modeloPlacar = modeloPlacar;
        this.visao = visao;
    }

    public void mostrarMenuPrincipal() {
        try {
            visao.mostrarMenuPrincipal(modeloPlacar.getMelhoresPontuacoes());
        } catch (RecursoNaoEncontradoException e) {
            mostrarErroFatal(e);
        }
    }

    public void iniciarJogo(String nomeJogador1, String nomeJogador2) {
        iniciarLogicaJogo(nomeJogador1, nomeJogador2);
    }

    public void jogarNovamente() {
        iniciarLogicaJogo(modeloJogo.getNomeJogador1(), modeloJogo.getNomeJogador2());
    }

    public void voltarAoMenu() {
        mostrarMenuPrincipal();
    }

    private void iniciarLogicaJogo(String nomeJogador1, String nomeJogador2) {
        try {
            modeloJogo.iniciarNovoJogo(nomeJogador1, nomeJogador2);
            visao.mostrarTelaJogo(modeloJogo.getCartas());
            visao.atualizarJogadorAtual(modeloJogo.getNomeJogadorAtual());
            visao.atualizarPontuacaoJogador(modeloJogo.getNomeJogador1(), modeloJogo.getPontuacaoJogador1(), 1);
            visao.atualizarPontuacaoJogador(modeloJogo.getNomeJogador2(), modeloJogo.getPontuacaoJogador2(), 2);
            // O cronômetro não será mais usado para pontuação, mas pode ser mantido para tempo de jogo total
            iniciarCronometro();
            primeiraCarta = null;
            segundaCarta = null;
        } catch (RecursoNaoEncontradoException e) {
            mostrarErroFatal(e);
        }
    }

    private void iniciarCronometro() {
        tempoInicial = System.currentTimeMillis();
        cronometro = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            long segundosPassados = (System.currentTimeMillis() - tempoInicial) / 1000;
            long minutos = segundosPassados / 60;
            long segundos = segundosPassados % 60;
//             visao.atualizarCronometro(String.format("%02d:%02d", minutos, segundos));
        }));
        cronometro.setCycleCount(Timeline.INDEFINITE);
        cronometro.play();
    }

    public void lidarComCliqueNaCarta(CartaView visaoCarta) {
        if (visaoCarta.isDisabled() || visaoCarta.getModeloCarta().isVirada() || segundaCarta != null) return;

        visaoCarta.virarParaCima();

        if (primeiraCarta == null) {
            primeiraCarta = visaoCarta;
        } else {
            segundaCarta = visaoCarta;
            verificarCombinacao();
        }
    }

    private void verificarCombinacao() {
        CartaModel modeloCarta1 = primeiraCarta.getModeloCarta();
        CartaModel modeloCarta2 = segundaCarta.getModeloCarta();

        if (modeloCarta1.getCaminhoImagem().equals(modeloCarta2.getCaminhoImagem())) {
            modeloCarta1.setFoiCombinada(true);
            modeloCarta2.setFoiCombinada(true);
            primeiraCarta.setDisable(true);
            segundaCarta.setDisable(true);
            modeloJogo.incrementarParesEncontrados();
            modeloJogo.incrementarPontuacaoJogadorAtual();
            visao.atualizarPontuacaoJogador(modeloJogo.getNomeJogador1(), modeloJogo.getPontuacaoJogador1(), 1);
            visao.atualizarPontuacaoJogador(modeloJogo.getNomeJogador2(), modeloJogo.getPontuacaoJogador2(), 2);
            limparSelecao();
            if (modeloJogo.getParesEncontrados() == modeloJogo.getTotalPares()) {
                lidarComVitoria();
            }
        } else {
            PauseTransition pausa = new PauseTransition(Duration.seconds(0.7));
            pausa.setOnFinished(e -> {
                primeiraCarta.virarParaBaixo();
                segundaCarta.virarParaBaixo();
                limparSelecao();
                modeloJogo.proximoJogador();
                visao.atualizarJogadorAtual(modeloJogo.getNomeJogadorAtual());
            });
            pausa.play();
        }
    }

    private void limparSelecao() {
        primeiraCarta = null;
        segundaCarta = null;
    }

    private void lidarComVitoria() {
        cronometro.stop(); // O cronômetro ainda pode ser usado para tempo total de jogo, mas não para pontuação

        String nomeJogador1 = modeloJogo.getNomeJogador1();
        String nomeJogador2 = modeloJogo.getNomeJogador2();
        int pontosJogador1 = modeloJogo.getPontuacaoJogador1();
        int pontosJogador2 = modeloJogo.getPontuacaoJogador2();

        String nomeVencedor;
        int pontosVencedor;
        String nomePerdedor;
        int pontosPerdedor;

        boolean isEmpate = false;

        if (pontosJogador1 > pontosJogador2) {
            nomeVencedor = nomeJogador1;
            pontosVencedor = pontosJogador1;
            nomePerdedor = nomeJogador2;
            pontosPerdedor = pontosJogador2;
        } else if (pontosJogador2 > pontosJogador1) {
            nomeVencedor = nomeJogador2;
            pontosVencedor = pontosJogador2;
            nomePerdedor = nomeJogador1;
            pontosPerdedor = pontosJogador1;
        } else {
            // Empate - pode-se decidir um critério de desempate ou declarar empate
            nomeVencedor = nomeJogador1 + " e " + nomeJogador2;
            pontosVencedor = pontosJogador1;
            nomePerdedor = "Empate";
            pontosPerdedor = 0;
            isEmpate = true;
        }

        // O placar de melhores pontuações pode ser atualizado com o vencedor, se desejado
        RegistroPontuacaoModel novoRegistro = new RegistroPontuacaoModel(nomeVencedor, pontosVencedor);
        modeloPlacar.adicionarPontuacao(novoRegistro);

        try {
            if (isEmpate){
                visao.mostrarTelaEmpate(nomeVencedor, pontosVencedor);
            }
            else {
                visao.mostrarTelaVitoria(nomeVencedor, pontosVencedor, nomePerdedor, pontosPerdedor);
            }
        } catch (RecursoNaoEncontradoException e) {
            mostrarErroFatal(e);
        }
    }

    private void mostrarErroFatal(Exception e) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro Crítico");
        alerta.setHeaderText("Ocorreu um erro que impede o jogo de continuar.");
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}
