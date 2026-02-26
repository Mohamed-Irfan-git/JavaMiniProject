package command;

import dao.UserDAO;
import service.AuthService;
import utility.HikariCPDataSource;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private static final Map<String, Command> commands = new HashMap<>();

    public static void init() {
        try {
            Connection connection = HikariCPDataSource.getInstance().getConnection();

            UserDAO userDAO = new UserDAO(connection);
            AuthService authService = new AuthService(userDAO);

            commands.put("LOGIN", new LoginCommand(authService));
            commands.put("PING", (args, context) ->
                    context.getOutput().println("PONG")
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize CommandRegistry: " + e.getMessage());
        }
    }

    public static Command getCommand(String name) {
        return commands.get(name);
    }
}