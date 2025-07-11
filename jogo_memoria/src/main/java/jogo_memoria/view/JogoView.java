package jogo_memoria.view;

import jogo_memoria.controller.JogoController;
import jogo_memoria.exception.RecursoNaoEncontradoException;
import jogo_memoria.model.CartaModel;
import jogo_memoria.model.RegistroPontuacaoModel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class JogoView {
    private final Stage palcoPrincipal;
    private JogoController controle;
    private final Label jogadorAtualLabel = new Label("");
    private final Label pontuacaoJogador1Label = new Label("Jogador 1: 0 pontos");
    private final Label pontuacaoJogador2Label = new Label("Jogador 2: 0 pontos");

    public JogoView(Stage palcoPrincipal) {
        this.palcoPrincipal = palcoPrincipal;
    }

    public void setControle(JogoController controle) {
        this.controle = controle;
    }

    public void mostrarMenuPrincipal(List<RegistroPontuacaoModel> melhoresPontuacoes) throws RecursoNaoEncontradoException {
        VBox layoutMenu = new VBox();
        layoutMenu.getStyleClass().add("menu-vbox");

        Label titulo = new Label("Jogo da Memória");
        titulo.getStyleClass().add("game-title");

        VBox placarBox = new VBox(5);
        placarBox.setAlignment(Pos.CENTER);
        Label tituloPlacar = new Label("Melhores Pontuações");
        tituloPlacar.getStyleClass().add("highscore-title");
        placarBox.getChildren().add(tituloPlacar);

        if (melhoresPontuacoes.isEmpty()) {
            placarBox.getChildren().add(new Label("Ninguém jogou ainda!"));
        } else {
            for (int i = 0; i < melhoresPontuacoes.size(); i++) {
                RegistroPontuacaoModel registro = melhoresPontuacoes.get(i);
                Label placarLabel = new Label(String.format("#%d %s - %d pts",
                        i + 1, registro.getNomeJogador(), registro.getPontos()));
                placarLabel.getStyleClass().add("highscore-entry");
                placarBox.getChildren().add(placarLabel);
            }
        }

        Label nomeLabel1 = new Label("Nome do Jogador 1:");
        nomeLabel1.getStyleClass().add("name-label");
        TextField campoNome1 = new TextField();
        campoNome1.setPromptText("Jogador 1");
        campoNome1.setMaxWidth(200);

        Label nomeLabel2 = new Label("Nome do Jogador 2:");
        nomeLabel2.getStyleClass().add("name-label");
        TextField campoNome2 = new TextField();
        campoNome2.setPromptText("Jogador 2");
        campoNome2.setMaxWidth(200);

        Button botaoIniciar = new Button("Iniciar Jogo");
        botaoIniciar.getStyleClass().add("start-button");
        botaoIniciar.setDisable(true);
        botaoIniciar.setOnAction(e -> controle.iniciarJogo(campoNome1.getText(), campoNome2.getText()));

        campoNome1.textProperty().addListener((obs, oldText, newText) -> botaoIniciar.setDisable(newText.trim().isEmpty() || campoNome2.getText().trim().isEmpty()));
        campoNome2.textProperty().addListener((obs, oldText, newText) -> botaoIniciar.setDisable(newText.trim().isEmpty() || campoNome1.getText().trim().isEmpty()));

        layoutMenu.getChildren().addAll(titulo, placarBox, nomeLabel1, campoNome1, nomeLabel2, campoNome2, botaoIniciar);
        Scene cena = new Scene(layoutMenu, 520, 600);
        aplicarEstilo(cena);
        palcoPrincipal.setScene(cena);
    }

    public void mostrarTelaJogo(List<CartaModel> cartas) throws RecursoNaoEncontradoException {
        GridPane gridCartas = new GridPane();
        gridCartas.getStyleClass().add("card-grid");

        for (int i = 0; i < cartas.size(); i++) {
            CartaView visaoCarta = new CartaView(cartas.get(i));
            visaoCarta.setOnAction(e -> controle.lidarComCliqueNaCarta(visaoCarta));
            gridCartas.add(visaoCarta, i % 4, i / 4);
        }

        Label jogadorAtualLabel = this.jogadorAtualLabel;
        jogadorAtualLabel.getStyleClass().add("player-turn-label");
        Label pontuacaoJogador1Label = this.pontuacaoJogador1Label;
        pontuacaoJogador1Label.getStyleClass().add("score-label");
        Label pontuacaoJogador2Label = this.pontuacaoJogador2Label;
        pontuacaoJogador2Label.getStyleClass().add("score-label");

        VBox barraTopo = new VBox(jogadorAtualLabel, pontuacaoJogador1Label, pontuacaoJogador2Label);
        barraTopo.getStyleClass().add("top-bar");

        BorderPane layoutJogo = new BorderPane();
        layoutJogo.setTop(barraTopo);
        layoutJogo.setCenter(gridCartas);

        Scene cena = new Scene(layoutJogo, 520, 560);
        aplicarEstilo(cena);
        palcoPrincipal.setScene(cena);
    }

    public void mostrarTelaVitoria(String nomeJogadorVencedor, int pontosVencedor, String nomeJogadorPerdedor, int pontosPerdedor) throws RecursoNaoEncontradoException {
        VBox layoutVitoria = new VBox();
        layoutVitoria.getStyleClass().add("menu-vbox");

        Label titulo = new Label("Fim de Jogo!");
        titulo.getStyleClass().add("game-title");
        titulo.setStyle("-fx-font-size: 32px;");

        Label infoVencedor = new Label(String.format("Vencedor: %s com %d pontos!", nomeJogadorVencedor, pontosVencedor));
        infoVencedor.setStyle("-fx-font-size: 24px; -fx-text-fill: #004d40;");

        Label infoPerdedor = new Label(String.format("Perdedor: %s com %d pontos!", nomeJogadorPerdedor, pontosPerdedor));
        infoPerdedor.setStyle("-fx-font-size: 20px;");

        Button botaoJogarNovamente = new Button("Jogar Novamente");
        botaoJogarNovamente.getStyleClass().add("start-button");
        botaoJogarNovamente.setOnAction(e -> controle.jogarNovamente());

        Button botaoMenu = new Button("Voltar ao Menu");
        botaoMenu.getStyleClass().add("start-button");
        botaoMenu.setStyle("-fx-background-color: #6c757d;");
        botaoMenu.setOnAction(e -> controle.voltarAoMenu());

        layoutVitoria.getChildren().addAll(titulo, infoVencedor, infoPerdedor, botaoJogarNovamente, botaoMenu);
        Scene cena = new Scene(layoutVitoria, 520, 560);
        aplicarEstilo(cena);
        palcoPrincipal.setScene(cena);
    }

    public void mostrarTelaEmpate(String nomeJogadorVencedor, int pontosVencedor) throws RecursoNaoEncontradoException {
        VBox layoutVitoria = new VBox();
        layoutVitoria.getStyleClass().add("menu-vbox");

        Label titulo = new Label("Fim de Jogo!");
        titulo.getStyleClass().add("game-title");
        titulo.setStyle("-fx-font-size: 32px;");

        Label infoVencedor = new Label(String.format("Empate: %s com %d pontos!", nomeJogadorVencedor, pontosVencedor));
        infoVencedor.setStyle("-fx-font-size: 24px; -fx-text-fill: #004d40;");

        Button botaoJogarNovamente = new Button("Jogar Novamente");
        botaoJogarNovamente.getStyleClass().add("start-button");
        botaoJogarNovamente.setOnAction(e -> controle.jogarNovamente());

        Button botaoMenu = new Button("Voltar ao Menu");
        botaoMenu.getStyleClass().add("start-button");
        botaoMenu.setStyle("-fx-background-color: #6c757d;");
        botaoMenu.setOnAction(e -> controle.voltarAoMenu());

        layoutVitoria.getChildren().addAll(titulo, infoVencedor, botaoJogarNovamente, botaoMenu);
        Scene cena = new Scene(layoutVitoria, 520, 560);
        aplicarEstilo(cena);
        palcoPrincipal.setScene(cena);
    }

    public void atualizarJogadorAtual(String nomeJogador) {
        jogadorAtualLabel.setText("Turno de: " + nomeJogador);
    }

    public void atualizarPontuacaoJogador(String nomeJogador, int pontos, int jogadorNumero) {
        if (jogadorNumero == 1) {
            pontuacaoJogador1Label.setText(nomeJogador + ": " + pontos + " pontos");
        } else if (jogadorNumero == 2) {
            pontuacaoJogador2Label.setText(nomeJogador + ": " + pontos + " pontos");
        }
    }

    private void aplicarEstilo(Scene cena) throws RecursoNaoEncontradoException {
        URL urlCss = getClass().getResource("/jogo_memoria/styles.css");
        if (urlCss == null) {
            throw new RecursoNaoEncontradoException("Arquivo de estilo 'styles.css' não encontrado.");
        }
        cena.getStylesheets().add(urlCss.toExternalForm());
    }
}
