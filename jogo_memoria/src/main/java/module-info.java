module jogo_memoria {
    requires javafx.controls;
    requires javafx.fxml;


    opens jogo_memoria to javafx.fxml;
    exports jogo_memoria;
}