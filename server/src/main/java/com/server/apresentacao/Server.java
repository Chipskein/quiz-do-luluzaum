package com.server.apresentacao;

import com.server.database.InMemoryDB;
import com.server.negocio.*;
import com.server.negocio.ClientMessageUtil.NicknameData;
import com.server.negocio.ClientMessageUtil.PlayAgainData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.util.logging.Level;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.logging.Logger;

public class Server {
    private static final Logger log = Logger.getLogger(Server.class.getName());

    private static final int PORT = 10000;

    private final InMemoryDB db = InMemoryDB.getInstance();

    private final Game game = new Game();

    public void start() {
        log.log(Level.INFO, "Servidor Battle Quiz iniciado na porta {0}", PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {

                Socket s1 = serverSocket.accept();
                Player p1 = createPlayer(s1);

                Socket s2 = serverSocket.accept();
                Player p2 = createPlayer(s2);

                db.addPlayer(p1);
                db.addPlayer(p2);

                log.log(Level.INFO, "Jogadores conectados: {0} vs {1}", new Object[]{p1.getNickname(), p2.getNickname()});

                playMatch(p1, p2);
            }

        } catch (IOException e) {
            log.log(Level.SEVERE, "Erro no servidor: {0}", e.getMessage());
        }
    }

    private Player createPlayer(Socket socket) throws IOException {
        Player p = new Player(socket);

        sendCommand(Command.SEND_YOUR_NICKNAME, p, null);

        NicknameData msg = ClientMessageUtil.waitFor(p, Command.SENT_YOUR_NICKNAME, NicknameData.class);

        p.setNickname(msg.nickname);

        sendCommand(Command.CONNECTED, p, null);

        return p;
    }

    private void playMatch(Player p1, Player p2) {
        game.reset();
        sendCommand(Command.GAME_START, p1, null);
        sendCommand(Command.GAME_START, p2, null);

        while (true) {
            Question q = game.getRandomQuestion();

            sendQuestion(p1, q);
            sendQuestion(p2, q);

            try {
                game.checkRound(p1, p2, q.id);
            } catch (IOException e) {
                break;
            }

            sendScore(p1, p2);

            if (game.hasWinner()) {
                announceWinner(p1, p2);

                if (!askPlayAgain(p1, p2)) {
                    close(p1);
                    close(p2);
                    return;
                }

                game.reset();
                sendCommand(Command.NEW_GAME, p1, null);
                sendCommand(Command.NEW_GAME, p2, null);
            }
        }
    }

    private void sendQuestion(Player p, Question q) {
        sendCommand(Command.SHOW_QUESTION, p, q);
    }

    private void sendScore(Player p1, Player p2) {
        var players = new Player[]{p1, p2};
        sendCommand(Command.SHOW_SCORE, p1, players);
        sendCommand(Command.SHOW_SCORE, p2, players);
    }

    private void announceWinner(Player p1, Player p2) {
        Player winner = (p1.getScore() > p2.getScore()) ? p1 : p2;
        sendCommand(Command.GAME_END, p1, winner);
        sendCommand(Command.GAME_END, p2, winner);
    }

    private boolean askPlayAgain(Player p1, Player p2) {
        sendCommand(Command.ASK_PLAY_AGAIN, p1, null);
        sendCommand(Command.ASK_PLAY_AGAIN, p2, null);
        PlayAgainData pr1 = ClientMessageUtil.waitFor(p1, Command.SENT_PLAY_AGAIN, PlayAgainData.class);
        PlayAgainData pr2 = ClientMessageUtil.waitFor(p2, Command.SENT_PLAY_AGAIN, PlayAgainData.class);
        return (pr1.again && pr2.again);
    }

    private void close(Player p) {
        try {
            sendCommand(Command.DISCONNECTED, p, null);
            p.close();
        } catch (IOException ignored) {}
    }

    private void sendCommand(Command cmd, Player p, Object data) {
        log.log(Level.INFO, "Enviando comando {0} para {1}", new Object[]{cmd, p.getNickname()});
        try {
            String json = (data == null) ? "" : new ObjectMapper().writeValueAsString(data);
            String msg = cmd + "," + json;
            p.sendMessage(msg);

        } catch (JsonProcessingException e) {
            log.log(Level.SEVERE, "Erro ao enviar comando: {0}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }

}
