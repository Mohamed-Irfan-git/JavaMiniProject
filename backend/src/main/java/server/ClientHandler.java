package server;

import command.ClientContext;
import command.Command;
import command.CommandRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;

    ClientHandler(Socket socket){
        this.socket  = socket;
    }
    @Override
    public void run() {
       try(
               BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

               ) {

           ClientContext clientContext = new ClientContext(output);
           output.println("server ready");

           String line;
           while((line= input.readLine())!=null){
                String [] parts = line.split(",");
                String commandName = parts[0];
                Command command = CommandRegistry.getCommand(commandName);

                if(command != null){
                    command.execute(parts,clientContext);
                }
                else {
                    output.println("unknown command");
                }
           }
       } catch (IOException e) {
           throw new RuntimeException(e);
       }


    }
}
