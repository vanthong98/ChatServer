package hw.app.core.socket;

import hw.app.core.main.ChatClient;
import hw.app.core.main.Mediator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketOutputStreamHandler extends Thread {
    private final OutputStream _socketOutputStream;
    private final ChatClient _client ;
    private final Socket _socket ;

    public ClientSocketOutputStreamHandler(ChatClient client, Socket socket) throws IOException {
        _socketOutputStream = socket.getOutputStream();
        _client = client;
        _socket = socket;
    }

    public void run() {
        PrintWriter writer = new PrintWriter(_socketOutputStream, true);

        String message;
        boolean stop = false;
        do {
            synchronized(_client.messages) {
                message = _client.messages.poll();
            }

            synchronized(_client) {
                stop = !_client.authorized;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (message == null) {
                continue;
            }

            message = message + "\n";

            writer.println(message);

        } while (!stop);

        if (!_socket.isClosed()){
            try {
                _socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Mediator.broadcastClientDisconnected(_client.name);
    }
}
