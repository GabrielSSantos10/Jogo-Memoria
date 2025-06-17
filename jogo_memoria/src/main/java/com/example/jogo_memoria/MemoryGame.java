package com.example.jogo_memoria;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MemoryGame extends Application {
    private static final int NUM_PAIRS = 8;
    private static final int GRID_COLUMNS = 4;
    private static final int GRID_ROWS = 4;

    private List<Card> cards;
    private Card firstCard = null;
    private Card secondCard = null;
    private int pairsFound = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jogo da Memória - Flores");

        GridPane grid = createGrid();
        setupCards(grid);

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        return grid;
    }

    private void setupCards(GridPane grid) {
        cards = new ArrayList<>();
        List<Image> images = new ArrayList<>();

        for (int i = 1; i <= NUM_PAIRS; i++) {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/flower" + i + ".jpg")));
            images.add(image);
            images.add(image); // Adiciona o par
        }

        Collections.shuffle(images);

        for (int i = 0; i < GRID_COLUMNS * GRID_ROWS; i++) {
            Card card = new Card(images.get(i));
            cards.add(card);
            grid.add(card, i % GRID_COLUMNS, i / GRID_COLUMNS);
        }
    }

    private void checkMatch() {
        if (firstCard.getImage() == secondCard.getImage()) {
            // É um par!
            firstCard.setDisable(true);
            secondCard.setDisable(true);
            pairsFound++;

            // Reseta a seleção para o próximo par
            firstCard = null;
            secondCard = null;

            checkWin();
        } else {
            // Não é um par, vira de volta após uma pausa
            PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
            pause.setOnFinished(event -> {
                firstCard.flipDown();
                secondCard.flipDown();

                // A SOLUÇÃO: Resetar a seleção AQUI DENTRO, depois de virar as cartas.
                firstCard = null;
                secondCard = null;
            });
            pause.play();
        }
    }

    private void checkWin() {
        if (pairsFound == NUM_PAIRS) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Parabéns!");
            alert.setHeaderText(null);
            alert.setContentText("Você encontrou todos os pares! 🎉");
            alert.showAndWait();
        }
    }

    // Classe interna para representar uma carta
    private class Card extends Button {
        private final Image image;
        private final ImageView imageView;
        private boolean isFlipped = false;

        public Card(Image image) {
            this.image = image;
            this.imageView = new ImageView();
            this.imageView.setFitWidth(100);
            this.imageView.setFitHeight(100);

            setGraphic(null); // Começa virada para baixo
            getStyleClass().add("card");
            setPrefSize(100, 100);

            setOnAction(event -> handleCardClick());
        }

        public Image getImage() {
            return image;
        }

        public void flipUp() {
            setGraphic(imageView);
            imageView.setImage(image);
            isFlipped = true;
        }

        public void flipDown() {
            setGraphic(null);
            isFlipped = false;
        }

        private void handleCardClick() {
            if (isFlipped || (firstCard != null && secondCard != null)) {
                return; // Ignora clique se a carta já está virada ou se duas já foram escolhidas
            }

            flipUp();

            if (firstCard == null) {
                firstCard = this;
            } else {
                secondCard = this;
                checkMatch();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
