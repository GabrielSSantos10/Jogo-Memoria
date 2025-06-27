☕ Jogo da Memória com Arquitetura MVC em JavaFX

![image](https://github.com/user-attachments/assets/9a0965f6-0ff1-4739-82d5-53bd78c4ea36)

📝 Sobre o Projeto
Este projeto é um clássico Jogo da Memória com tema de flores, desenvolvido como uma aplicação de desktop utilizando Java e a biblioteca JavaFX. O principal objetivo do projeto foi aplicar e demonstrar conceitos avançados de engenharia de software, como a arquitetura MVC (Model-View-Controller), tratamento de exceções personalizado e boas práticas de organização de código.

O resultado é uma aplicação robusta, organizada e de fácil manutenção, com uma experiência de usuário inspirada nos clássicos jogos de fliperama, incluindo ranking de pontuações e um sistema de score dinâmico.

✨ Funcionalidades Principais
Arquitetura MVC: O código é estritamente organizado no padrão MVC (Model-View-Controller), separando os dados, a interface e a lógica de controle.

Sistema de Pontuação Dinâmico: A pontuação é calculada com base no tempo gasto para concluir o jogo. A fórmula base é de 100 pontos para 5 minutos, com bônus por terminar mais rápido.

Ranking (Top 3): O jogo salva e exibe as três maiores pontuações, incentivando a rejogabilidade. O ranking persiste enquanto a aplicação está em execução.

Interface Gráfica com JavaFX: Interface de usuário moderna e responsiva, com telas para menu principal, jogo e tela de vitória.

Tratamento de Exceções Personalizado: Implementação de uma classe de exceção customizada (RecursoNaoEncontradoException) para lidar de forma elegante com erros de carregamento de arquivos (imagens, CSS).

Ciclo de Jogo Completo: O jogador pode inserir seu nome, jogar, ver sua pontuação, jogar novamente com o mesmo nome ou voltar ao menu para um novo jogador.

🏛️ Arquitetura MVC Aplicada
A escolha do padrão MVC foi fundamental para a organização do projeto:

Model (modelo): Contém todas as regras de negócio e o estado do jogo. As classes ModeloJogo, ModeloPlacar e ModeloCarta gerenciam as cartas, a pontuação e os dados do jogador, sem qualquer conhecimento da interface gráfica.

View (visao): Responsável por toda a renderização da interface gráfica. As classes VisaoJogo e VisaoCarta constroem as telas e os componentes visuais. Ela é uma camada "passiva" que apenas exibe dados e reporta ações do usuário ao Controller.

Controller (controle): Atua como o cérebro da aplicação. A classe ControleJogo recebe as interações do usuário (ex: cliques), processa a lógica, atualiza o Model com novos dados e comanda a View para que ela se atualize e reflita o novo estado do jogo.

🛠️ Tecnologias Utilizadas
Java: JDK 17 ou superior

JavaFX: SDK 21 ou superior para a interface gráfica

IDE: Desenvolvido no IntelliJ IDEA

🚀 Como Executar o Projeto
Para clonar e executar este projeto em sua máquina local, siga os passos abaixo.

Pré-requisitos
JDK (Java Development Kit) - Versão 17 ou mais recente.

JavaFX SDK - Versão 21 ou mais recente. Faça o download aqui.

IntelliJ IDEA (ou outra IDE com suporte a JavaFX).

Passos para Execução
Clone o repositório:

Bash

git clone https://github.com/GabrielSSantos10/Jogo-Memoria.git

Abra o projeto no IntelliJ IDEA.

Configure a SDK do JavaFX:

Vá em File > Project Structure > Libraries.

Clique no + e adicione a pasta lib da sua SDK do JavaFX como uma biblioteca para o projeto.

Configure as Opções da VM (Máquina Virtual):

Vá em Run > Edit Configurations....

No campo VM options, adicione o seguinte, substituindo CAMINHO/PARA/SUA/JAVAFX_SDK/lib pelo caminho correto em sua máquina:

--module-path "CAMINHO/PARA/SUA/JAVAFX_SDK/lib" --add-modules javafx.controls,javafx.fxml
Execute a aplicação:

Encontre a classe JogoMemoriaFloridoApp.java no pacote br.com.projeto.jogo_memoria.

Clique com o botão direito e selecione Run 'AplicacaoJogoMemoria.main()'.

📁 Estrutura do Código
.
└── src/main/java/jogo_memoria/
    ├── JogoMemoriaFloridoApp.java     # Ponto de entrada da aplicação
    ├── controller/
    │   └── JogoController.java         # Controller principal
    ├── exception/
    │   └── RecursoNaoEncontradoException.java # Exceção customizada
    ├── model/
    │   ├── CartaModel.java
    │   ├── JogoModel.java
    │   ├── PlacarModel.java
    │   └── RegistroPontuacaoModel.java
    └── view/
        ├── CartaView.java
        └── JogoView.java
📜 Licença
Este projeto está sob a licença MIT.

👨‍💻 Autor
Gabriel Simplicio dos Santos.
