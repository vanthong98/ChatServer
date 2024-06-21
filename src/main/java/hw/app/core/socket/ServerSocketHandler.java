package hw.app.core.socket;


import hw.app.core.main.ChatClient;
import hw.app.core.main.Mediator;
import javafx.application.Platform;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ServerSocketHandler extends Thread {

    int _port ;

    private final Random _random = new Random();

    public ServerSocketHandler(int port) {
        _port = port;
    }

    public void run(){
        Mediator.log("Server socket handler started at port " + _port);

        var clientNames = getClientNames();

        try {

            Socket socket;

            try (var _serverSocket = new ServerSocket(_port)) {
                //noinspection InfiniteLoopStatement
                do {

                    Mediator.log("Server socket is waiting for a new connection");

                    socket = _serverSocket.accept();

                    int randomIndex = _random.nextInt(clientNames.size());
                    var clientName = clientNames.get(randomIndex);
                    clientNames.remove(clientName);

                    Mediator.log("Server socket is accepted a new connection with client " + clientName);

                    var client = new ChatClient();

                    client.name = clientName;

                    synchronized (Mediator.clients) {
                        Mediator.clients.add(client);
                        Mediator.broadcastClientConnected(clientName);
                    }

                    var socketInputHandler = new ClientSocketInputStreamHandler(client, socket);
                    var socketOutputHandler = new ClientSocketOutputStreamHandler(client, socket);

                    socketInputHandler.start();
                    socketOutputHandler.start();

                    Mediator.sendClientConnectedList(clientName);
                }
                while (true);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<String> getClientNames() {
        var clientNames = new ArrayList<String>();

        clientNames.add("Banana_0");
        clientNames.add("Orange_0");
        clientNames.add("Apple_0");
        clientNames.add("Lemon_0");
        clientNames.add("Mango_0");
        clientNames.add("Banana_1");
        clientNames.add("Orange_1");
        clientNames.add("Apple_1");
        clientNames.add("Lemon_1");
        clientNames.add("Mango_1");
        clientNames.add("Banana_2");
        clientNames.add("Orange_2");
        clientNames.add("Apple_2");
        clientNames.add("Lemon_3");
        clientNames.add("Mango_4");
        return clientNames;
    }
}
