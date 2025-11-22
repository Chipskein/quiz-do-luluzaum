package com.server.negocio;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Question {
    public int id;

    public String question;

    public Map<String,String> options;

    public Option correctAnswer;

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Question para JSON", e);
        }
    }
}
