package jogo_memoria.view;

import jogo_memoria.model.CartaModel;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class CartaView extends Button{
    private final CartaModel modeloCarta;
    private final ImageView imagemVisivel;

    public CartaView(CartaModel modeloCarta) {
        this.modeloCarta = modeloCarta;
        Image imagemFrente = new Image(Objects.requireNonNull(getClass().getResourceAsStream(modeloCarta.getCaminhoImagem())));

        this.imagemVisivel = new ImageView(imagemFrente);
        this.imagemVisivel.setFitWidth(100);
        this.imagemVisivel.setFitHeight(100);
        this.imagemVisivel.setPreserveRatio(true);

        getStyleClass().add("card");
        setPrefSize(110, 110);
        virarParaBaixo();
    }

    public CartaModel getModeloCarta() {
        return modeloCarta;
    }

    public void virarParaCima() {
        setGraphic(this.imagemVisivel);
        modeloCarta.setVirada(true);
    }

    public void virarParaBaixo() {
        setGraphic(null);
        modeloCarta.setVirada(false);
    }
}
