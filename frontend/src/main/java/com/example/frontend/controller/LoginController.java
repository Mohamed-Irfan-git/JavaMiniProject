package com.example.frontend.controller;

import com.example.frontend.network.ServerClient;
import com.example.frontend.service.AuthService;
import com.example.frontend.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final ServerClient client = new ServerClient();

    @FXML
    public void initialize() {
        usernameField.setOnAction(actionEvent -> passwordField.requestFocus() );
        // Trigger login when pressing Enter in the password field
        passwordField.setOnAction(event -> login());
    }

    @FXML
    public void login() {

        try {
            client.connect();
            AuthService authService = new AuthService(client);

            String token = authService.login(
                    usernameField.getText(),
                    passwordField.getText()
            );

            if(token != null){
                SessionManager.setToken(token);
                System.out.println("Login success!");
                System.out.println("Token: " + token);

            }else{
                System.out.println("Login failed");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}