package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiServer {

    private ExecutorService executorService;
    private static final int threadPool = 20;
    private static final int port = 5000;

    public MultiServer(){
        executorService = Executors.newFixedThreadPool(threadPool);
    }


    /**
        using this function csn simply start the server
     **/
   public void start (){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("server started on " + port);

            /**
                until user shutdown the system this run
                infinite loop here at the same time 20 thread
                can use
             **/
            while(true){
                Socket socket = serverSocket.accept();
                executorService.submit(new ClientHandler(socket));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
   }
}
