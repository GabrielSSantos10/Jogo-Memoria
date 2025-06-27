package jogo_memoria;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MemoryGame extends Application {

    private static final int NUMERO_PARES = 8;
    private static final int COLUNAS_GRADE = 4;
    private static final int LINHAS_GRADE = 4;

    private Stage palcoPrincipal;
    private Carta primeiraCarta = null;
    private Carta segundaCarta = null;
    private int paresEncontrados = 0;

    private Timeline linhaDoTempo;
    private Label rotuloCronometro;
    private long tempoInicial;

    @Override
    public void start(Stage palco) {
        this.palcoPrincipal = palco;
        palcoPrincipal.setTitle("Jogo da Memória");
        exibirCenaMenu();
        palcoPrincipal.setResizable(false);
        palcoPrincipal.show();
    }

    private void exibirCenaMenu() {
        Label titulo = new Label("Jogo da Memória");
        titulo.getStyleClass().add("game-title");

        Button botaoIniciar = new Button("Iniciar Jogo");
        botaoIniciar.getStyleClass().add("start-button");
        botaoIniciar.setOnAction(e -> iniciarJogo());

        VBox layoutMenu = new VBox();
        layoutMenu.getStyleClass().add("menu-vbox");
        layoutMenu.getChildren().addAll(titulo, botaoIniciar);

        Scene cenaMenu = new Scene(layoutMenu, 520, 560);
        cenaMenu.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        palcoPrincipal.setScene(cenaMenu);
    }

    // NOVO MÉTODO - Adicionado para a tela de vitória
    private void exibirCenaVitoria(String tempoFinal) {
        Label titulo = new Label("Parabéns!");
        titulo.getStyleClass().add("game-title");

        Label rotuloInfoTempo = new Label("Seu tempo foi:");
        rotuloInfoTempo.setStyle("-fx-font-size: 24px; -fx-text-fill: #004d40;");

        Label rotuloTempoFinal = new Label(tempoFinal);
        rotuloTempoFinal.getStyleClass().add("timer-label");

        Button botaoJogarNovamente = new Button("Jogar Novamente");
        botaoJogarNovamente.getStyleClass().add("start-button");
        botaoJogarNovamente.setOnAction(e -> iniciarJogo());

        VBox layoutVitoria = new VBox();
        layoutVitoria.getStyleClass().add("menu-vbox");
        layoutVitoria.getChildren().addAll(titulo, rotuloInfoTempo, rotuloTempoFinal, botaoJogarNovamente);

        Scene cenaVitoria = new Scene(layoutVitoria, 520, 560);
        cenaVitoria.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        palcoPrincipal.setScene(cenaVitoria);
    }

    private void iniciarJogo() {
        paresEncontrados = 0;
        primeiraCarta = null;
        segundaCarta = null;

        GridPane grade = criarGradeCartas();
        configurarCartas(grade);

        rotuloCronometro = new Label("00:00");
        rotuloCronometro.getStyleClass().add("timer-label");

        BorderPane layoutJogo = new BorderPane();
        layoutJogo.getStyleClass().add("game-pane");

        VBox barraSuperior = new VBox(rotuloCronometro);
        barraSuperior.getStyleClass().add("top-bar");

        layoutJogo.setTop(barraSuperior);
        layoutJogo.setCenter(grade);

        Scene cenaJogo = new Scene(layoutJogo, 520, 560);
        cenaJogo.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        palcoPrincipal.setScene(cenaJogo);

        iniciarCronometro();
    }

    private void iniciarCronometro() {
        tempoInicial = System.currentTimeMillis();
        linhaDoTempo = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            long milissegundosPassados = System.currentTimeMillis() - tempoInicial;
            long segundosPassados = milissegundosPassados / 1000;
            long minutos = segundosPassados / 60;
            long segundos = segundosPassados % 60;
            rotuloCronometro.setText(String.format("%02d:%02d", minutos, segundos));
        }));
        linhaDoTempo.setCycleCount(Timeline.INDEFINITE);
        linhaDoTempo.play();
    }

    private GridPane criarGradeCartas() {
        GridPane grade = new GridPane();
        grade.getStyleClass().add("card-grid");
        return grade;
    }

    private void configurarCartas(GridPane grade) {
        List<Image> imagens = new ArrayList<>();
        String extensaoArquivo = ".jpg";

        for (int i = 1; i <= NUMERO_PARES; i++) {
            String caminhoImagem = "/images/flower" + i + extensaoArquivo;
            try {
                Image imagem = new Image(Objects.requireNonNull(getClass().getResourceAsStream(caminhoImagem)));
                imagens.add(imagem);
                imagens.add(imagem);
            } catch (NullPointerException e) {
                System.err.println("Erro: Não foi possível encontrar a imagem: " + caminhoImagem);
                return;
            }
        }

        Collections.shuffle(imagens);

        for (int i = 0; i < COLUNAS_GRADE * LINHAS_GRADE; i++) {
            Carta carta = new Carta(imagens.get(i));
            grade.add(carta, i % COLUNAS_GRADE, i / COLUNAS_GRADE);
        }
    }

    private void verificarCombinacao() {
        boolean combinou = primeiraCarta.getImagem() == segundaCarta.getImagem();

        if (combinou) {
            primeiraCarta.setDisable(true);
            segundaCarta.setDisable(true);
            paresEncontrados++;
            primeiraCarta = null;
            segundaCarta = null;
            verificarVitoria();
        } else {
            PauseTransition pausa = new PauseTransition(Duration.seconds(0.7));
            pausa.setOnFinished(evento -> {
                primeiraCarta.virarParaBaixo();
                segundaCarta.virarParaBaixo();
                primeiraCarta = null;
                segundaCarta = null;
            });
            pausa.play();
        }
    }

    // MÉTODO MODIFICADO - Agora chama a nova tela
    private void verificarVitoria() {
        if (paresEncontrados == NUMERO_PARES) {
            linhaDoTempo.stop();
            exibirCenaVitoria(rotuloCronometro.getText());
        }
    }

    private class Carta extends Button {
        private final Image imagem;
        private final ImageView visualizadorImagem;
        private boolean estaVirada = false;

        public Carta(Image imagem) {
            this.imagem = imagem;
            this.visualizadorImagem = new ImageView();
            this.visualizadorImagem.setFitWidth(100);
            this.visualizadorImagem.setFitHeight(100);
            this.visualizadorImagem.setPreserveRatio(true);
            this.visualizadorImagem.getStyleClass().add("image-view");

            getStyleClass().add("card");
            setPrefSize(110, 110);

            setOnAction(evento -> tratarCliqueCarta());
            virarParaBaixo();
        }

        public Image getImagem() {
            return imagem;
        }

        public void virarParaCima() {
            this.visualizadorImagem.setImage(this.imagem);
            setGraphic(this.visualizadorImagem);
            estaVirada = true;
        }

        public void virarParaBaixo() {
            setGraphic(null);
            estaVirada = false;
        }

        private void tratarCliqueCarta() {
            if (estaVirada || segundaCarta != null) {
                return;
            }

            virarParaCima();

            if (primeiraCarta == null) {
                primeiraCarta = this;
            } else {
                segundaCarta = this;
                verificarCombinacao();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}