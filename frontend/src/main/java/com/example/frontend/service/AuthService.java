package com.example.frontend.service;

import com.example.frontend.network.ServerClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthService {

    private ServerClient client;
    private ObjectMapper mapper = new ObjectMapper();

    public AuthService(ServerClient client){
        this.client = client;
    }

    public String login(String username, String password) throws Exception {

        String loginJson = String.format(
                "{\"command\":\"LOGIN\",\"data\":{\"username\":\"%s\",\"password\":\"%s\"}}",
                username,
                password
        );

        String response = client.sendRequest(loginJson);

        JsonNode node = mapper.readTree(response);

        if(node.get("success").asBoolean()){

            return node.get("token").asText();
        }

        return null;
    }
}