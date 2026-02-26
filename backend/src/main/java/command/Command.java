package command;

public interface Command {
    void execute(Object data, ClientContext clientContext );
}
