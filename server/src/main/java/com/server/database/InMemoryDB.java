package com.server.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.server.negocio.Player;
import com.server.negocio.Question;

public class InMemoryDB {

    private static InMemoryDB instance;

    private List<Question> questions;

    private List<Player> players;

    private static final String DATA_FILE = "data.json";

    private InMemoryDB() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream dataStream = InMemoryDB.class
                    .getClassLoader()
                    .getResourceAsStream(DATA_FILE);

            if (dataStream == null) {
                throw new RuntimeException(DATA_FILE + " not found in resources!");
            }

            questions = mapper.readValue(
                dataStream,
                new TypeReference<List<Question>>() {}
            );

            players = new ArrayList<>();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + DATA_FILE, e);
        }
    }


    public static synchronized InMemoryDB getInstance() {
        if (instance == null) instance = new InMemoryDB();
        return instance;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Optional<Question> findById(int id) {
        return questions.stream().filter(i -> i.id == id).findFirst();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Optional<Player> getPlayer(String nickname){
        return players.stream().filter(i -> i.getNickname().equalsIgnoreCase(nickname)).findFirst();
    }

}
