package hw.app.core.socket;

import hw.app.core.common.MessageType;
import hw.app.core.main.ChatClient;
import hw.app.core.main.Mediator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSocketInputStreamHandler extends Thread {
    private final InputStream _socketInputStream;
    private final ChatClient _client;
    private final Socket _socket;
    public ClientSocketInputStreamHandler(ChatClient client, Socket socket) throws IOException {
        _socketInputStream = socket.getInputStream();
        _client = client;
        _socket = socket;
    }

    public void run() {
        try {

            var reader = new BufferedReader(new InputStreamReader(_socketInputStream));

            String message;
            boolean stop;

            do {
                message = reader.readLine();

                synchronized(_client) {
                    stop = !_client.authorized;
                }

                if (message.isEmpty()){
                    Thread.sleep(50);
                    continue;
                }

                var parts = message.split(";");

                var type = parts[0];

                var receiver = parts[1];
                var payload = parts[2];
                var sender = _client.name;

                if (type.equals(MessageType.PrivateMessage.toString())) {
                    Mediator.sendPrivateMessage(sender, receiver, payload);
                }

                if (type.equals(MessageType.GroupMessage.toString())) {
                    Mediator.sendGroupMessage(sender, payload);
                }

            } while (!stop);

        } catch (Exception ex) {
            Mediator.log("Error: " + ex.getMessage());
        }

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
