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

    public void iniciarJogo(String nomeJogador) {
        modeloJogo.setNomeJogadorAtual(nomeJogador);
        iniciarLogicaJogo();
    }

    public void jogarNovamente() {
        iniciarLogicaJogo();
    }

    public void voltarAoMenu() {
        mostrarMenuPrincipal();
    }

    private void iniciarLogicaJogo() {
        try {
            modeloJogo.iniciarNovoJogo();
            visao.mostrarTelaJogo(modeloJogo.getCartas());
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
            visao.atualizarCronometro(String.format("%02d:%02d", minutos, segundos));
        }));
        cronometro.setCycleCount(Timeline.INDEFINITE);
        cronometro.play();
    }

    public void lidarComCliqueNaCarta(CartaView visaoCarta) {
        if (visaoCarta.isDisabled() || segundaCarta != null) return;

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
            });
            pausa.play();
        }
    }

    private void limparSelecao() {
        primeiraCarta = null;
        segundaCarta = null;
    }

    private void lidarComVitoria() {
        cronometro.stop();
        long segundosPassados = (System.currentTimeMillis() - tempoInicial) / 1000;

        int pontos = calcularPontos(segundosPassados);
        String tempoFormatado = formatarTempo(segundosPassados);

        RegistroPontuacaoModel novoRegistro = new RegistroPontuacaoModel(modeloJogo.getNomeJogadorAtual(), pontos, tempoFormatado);
        modeloPlacar.adicionarPontuacao(novoRegistro);

        try {
            visao.mostrarTelaVitoria(modeloJogo.getNomeJogadorAtual(), pontos, tempoFormatado);
        } catch (RecursoNaoEncontradoException e) {
            mostrarErroFatal(e);
        }
    }

    private int calcularPontos(long segundosGastos) {
        // Base: 5 minutos (300s) = 100 pontos.
        double pontuacao = (300.0 / Math.max(1, segundosGastos)) * 100.0;
        return (int) Math.round(pontuacao);
    }

    private String formatarTempo(long totalSegundos) {
        long minutos = totalSegundos / 60;
        long segundos = totalSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    private void mostrarErroFatal(Exception e) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro Crítico");
        alerta.setHeaderText("Ocorreu um erro que impede o jogo de continuar.");
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}
