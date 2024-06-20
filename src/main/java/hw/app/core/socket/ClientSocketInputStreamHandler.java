package hw.app.core.socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClientSocketInputStreamHandler extends Thread {
    private final InputStream _socketInputStream;

    public ClientSocketInputStreamHandler(InputStream socketInputStream) {
        _socketInputStream = socketInputStream;
    }

    public void run() {
        try {

            var reader = new BufferedReader(new InputStreamReader(_socketInputStream));

            String message;

            do {
                message = reader.readLine();

                if (message.isEmpty()){
                    continue;
                }

                Dispatcher.sendToClassMessage(_clientId, message);
                Dispatcher.send(_clientId, message, MessageType.SendMessage);

            } while (!Common.ExitMessage.equals(message));

        } catch (Exception ex) {
            Logger.log("Error: " + ex.getMessage());
        } finally {
            Logger.log("Client " + _clientId + " disconnected.");
            Dispatcher.removeClient(_clientId);
        }
    }
}
