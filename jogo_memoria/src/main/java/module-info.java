module com.example.jogo_memoria {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.jogo_memoria to javafx.fxml;
    exports com.example.jogo_memoria;
}