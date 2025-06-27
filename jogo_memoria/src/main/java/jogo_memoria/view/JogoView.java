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
    private final Label cronometroLabel = new Label("00:00");

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
                Label placarLabel = new Label(String.format("#%d %s - %d pts (%s)",
                        i + 1, registro.nomeJogador(), registro.pontos(), registro.tempoFormatado()));
                placarLabel.getStyleClass().add("highscore-entry");
                placarBox.getChildren().add(placarLabel);
            }
        }

        Label nomeLabel = new Label("Digite seu nome:");
        nomeLabel.getStyleClass().add("name-label");
        TextField campoNome = new TextField();
        campoNome.setPromptText("Jogador");
        campoNome.setMaxWidth(200);

        Button botaoIniciar = new Button("Iniciar Jogo");
        botaoIniciar.getStyleClass().add("start-button");
        botaoIniciar.setDisable(true);
        botaoIniciar.setOnAction(e -> controle.iniciarJogo(campoNome.getText()));

        campoNome.textProperty().addListener((obs, oldText, newText) -> botaoIniciar.setDisable(newText.trim().isEmpty()));

        layoutMenu.getChildren().addAll(titulo, placarBox, nomeLabel, campoNome, botaoIniciar);
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

        cronometroLabel.getStyleClass().add("timer-label");
        VBox barraTopo = new VBox(cronometroLabel);
        barraTopo.getStyleClass().add("top-bar");

        BorderPane layoutJogo = new BorderPane();
        layoutJogo.setTop(barraTopo);
        layoutJogo.setCenter(gridCartas);

        Scene cena = new Scene(layoutJogo, 520, 560);
        aplicarEstilo(cena);
        palcoPrincipal.setScene(cena);
    }

    public void mostrarTelaVitoria(String nomeJogador, int pontos, String tempo) throws RecursoNaoEncontradoException {
        VBox layoutVitoria = new VBox();
        layoutVitoria.getStyleClass().add("menu-vbox");

        Label titulo = new Label("Parabéns, " + nomeJogador + "!");
        titulo.getStyleClass().add("game-title");
        titulo.setStyle("-fx-font-size: 32px;");

        Label infoPontos = new Label(String.format("Você fez %d pontos!", pontos));
        infoPontos.setStyle("-fx-font-size: 24px; -fx-text-fill: #004d40;");

        Label infoTempo = new Label("Seu tempo: " + tempo);
        infoTempo.setStyle("-fx-font-size: 20px;");

        Button botaoJogarNovamente = new Button("Jogar Novamente");
        botaoJogarNovamente.getStyleClass().add("start-button");
        botaoJogarNovamente.setOnAction(e -> controle.jogarNovamente());

        Button botaoMenu = new Button("Voltar ao Menu");
        botaoMenu.getStyleClass().add("start-button");
        botaoMenu.setStyle("-fx-background-color: #6c757d;");
        botaoMenu.setOnAction(e -> controle.voltarAoMenu());

        layoutVitoria.getChildren().addAll(titulo, infoPontos, infoTempo, botaoJogarNovamente, botaoMenu);
        Scene cena = new Scene(layoutVitoria, 520, 560);
        aplicarEstilo(cena);
        palcoPrincipal.setScene(cena);
    }

    public void atualizarCronometro(String texto) {
        cronometroLabel.setText(texto);
    }

    private void aplicarEstilo(Scene cena) throws RecursoNaoEncontradoException {
        URL urlCss = getClass().getResource("/jogo_memoria/styles.css");
        if (urlCss == null) {
            throw new RecursoNaoEncontradoException("Arquivo de estilo 'styles.css' não encontrado.");
        }
        cena.getStylesheets().add(urlCss.toExternalForm());
    }
}
