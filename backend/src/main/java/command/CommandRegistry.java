package command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("LOGIN", new LoginCommand());
        commands.put("PING", (args, context) -> 
            context.getOutput().println("PONG")
        );
    }

    public static Command getCommand(String name) {
        return commands.get(name);
    }
}