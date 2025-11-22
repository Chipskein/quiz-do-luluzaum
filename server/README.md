# Battle Quiz Server

Servidor de rede para o jogo de perguntas e respostas em tempo real entre dois jogadores.

## Funcionalidades

- Aceita conexões de dois jogadores simultaneamente na porta **10000** (TCP).
- Recebe e armazena o nickname de cada jogador.
- Envia mensagem de boas-vindas: `"Cliente Aceito para o BATTLE QUIZ"`.
- Inicia partidas apenas quando ambos os jogadores estiverem conectados.
- Sorteia perguntas aleatoriamente do banco de perguntas, sem repetição.
- Envia pergunta e alternativas para ambos os jogadores.
- Recebe respostas dos jogadores (`RESPOSTA-A`, `RESPOSTA-B`, etc.).
- Calcula pontuação:
  - Primeiro jogador a responder corretamente ganha pontos.
  - Se o primeiro errar e o outro acertar, o adversário ganha pontos.
  - Se ambos erram, ninguém pontua.
- Atualiza e envia o placar a cada rodada.
- Encerra a partida quando um jogador atinge **30 pontos**.
- Anuncia o vencedor.
- Pergunta se os jogadores desejam nova partida:
  - Se ambos aceitarem, reinicia o jogo.
  - Caso contrário, encerra as conexões e volta a esperar novos jogadores.
- Trata desconexões de forma segura.

## Requisitos

- Java 17 ou superior
- Maven
- Dependências:
  - `Jackson` (para serialização/deserialização JSON)

## Como compilar e executar

1. **Compilar o projeto com Maven**:

```bash
mvn clean install
java -jar target/server-1.0.0-jar-with-dependencies.jar

```

### Comandos Cliente

    SENT_YOUR_NICKNAME,{"nickname":"Player1"}

    SENT_PLAY_AGAIN,{"again":true}

    SENT_ANSWER,{"answer":"A"}






