package com.server.negocio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Player {
    private static final Logger log = Logger.getLogger(Player.class.getName());

    private String nick;

    private int score = 0;

    @JsonIgnore
    private final Socket socket;

    @JsonIgnore
    private final PrintWriter out;

    @JsonIgnore
    private final BufferedReader in;

    public Player(Socket socket) throws  IOException {
        this.socket = socket;
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.nick = socket.getRemoteSocketAddress().toString();
    }

    public String getNickname() {
        return nick;
    }

    public int getScore() {
        return score;
    }

    public void setNickname(String nick) {
        this.nick = nick;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void removeScore(int points) {
        this.score -= points;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendError(String msg) {
        sendMessage("ERROR,{\"message\":\"" + msg.replace("\"", "'") + "\"}");
    }

    public String waitClientMessage() throws IOException {
        try {
            return in.readLine();
        } catch (IOException e) {
            if (socket.isClosed() || socket.isInputShutdown() || Thread.currentThread().isInterrupted()) {
                log.log(Level.INFO, "Conexão com o jogador {0} foi encerrada.", nick);
                return null;
            }
            throw e;
        }
    }

    public void close() throws IOException {
        socket.close();
    }

    @Override
    public String toString() {
        return "Player{nick='" + nick + "', score=" + score + "}";
    }

}
