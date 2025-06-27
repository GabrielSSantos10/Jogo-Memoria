package jogo_memoria;

import jogo_memoria.controller.JogoController;
import jogo_memoria.model.JogoModel;
import jogo_memoria.model.PlacarModel;
import jogo_memoria.view.JogoView;
import javafx.application.Application;
import javafx.stage.Stage;

public class JogoMemoriaFloridoApp extends Application{
    @Override
    public void start(Stage palcoPrincipal) {
        // Cria as instâncias principais
        JogoModel modeloJogo = new JogoModel();
        PlacarModel modeloPlacar = new PlacarModel();
        JogoView visao = new JogoView(palcoPrincipal);
        JogoController controle = new JogoController(modeloJogo, modeloPlacar, visao);

        visao.setControle(controle);

        palcoPrincipal.setTitle("Jogo da Memória");
        controle.mostrarMenuPrincipal();
        palcoPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
