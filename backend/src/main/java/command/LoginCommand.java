package command;

public class LoginCommand implements Command {

    @Override
    public void execute(String[] args, ClientContext context) {
        context.getOutput().println("Login successful");
    }
}