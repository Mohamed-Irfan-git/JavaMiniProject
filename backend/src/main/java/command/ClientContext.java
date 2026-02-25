package command;

import java.io.PrintWriter;

public class ClientContext {
    private PrintWriter output;

    public ClientContext(PrintWriter output){
        this.output = output;
    }

    public PrintWriter getOutput(){
        return output;
    }
}
