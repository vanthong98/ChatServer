package hw.app.core.main;

import hw.app.core.common.MessageType;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Mediator {

    public static final ArrayList<ChatClient> clients = new ArrayList<>();
    public static ObservableList<String> connectedClientNames;
    public static TextArea messageTextArea;
    public static TextArea systemConsoleTextArea;

    public static void sendPrivateMessage(String sender, String receiver, String message) {
        clients.stream()
                .filter(x -> x.name.equals(receiver))
                .findFirst()
                .ifPresent(x -> {
                    synchronized (x.messages) {
                        x.messages.add(MessageType.PrivateMessage + ";" + sender + ";" + receiver + ";" + message);
                    }
                });
    }

    public static void sendGroupMessage(String sender, String message) {
        Platform.runLater(() -> {
            var m = sender + " [" + getCurrentTime() + "]" + ":\n" + message + "\n";
            messageTextArea.appendText(m);
        });

        clients.forEach(x -> {
            if (x.name.equals(sender)) {
                return;
            }
            synchronized (x.messages) {
                x.messages.add(MessageType.GroupMessage + ";" + sender + ";" + "group" + ";" + message);
            }
        });
    }

    public static void broadcastClientConnected(String clientName) {

        Platform.runLater(() -> Mediator.connectedClientNames.add(clientName));

        clients.forEach(x -> {
            if (x.name.equals(clientName)) {
                return;
            }
            synchronized (x.messages) {
                x.messages.add(MessageType.ClientConnected + ";" + clientName);
            }
        });
    }

    public static void sendClientConnectedList(String receiver) {
        clients.stream()
                .filter(x -> x.name.equals(receiver))
                .findFirst()
                .ifPresent(x -> {
                    synchronized (x.messages) {
                        clients.forEach(client ->{
                            if (client.name.equals(receiver)) {
                                return;
                            }
                            x.messages.add(MessageType.ClientConnected + ";" + client.name);
                        });
                    }

                    x.messages.add(MessageType.ClientNameGranted + ";" + receiver);
                });
    }

    public static void broadcastClientDisconnected(String clientName) {

        Platform.runLater(() -> Mediator.connectedClientNames.remove(clientName));

        clients.removeIf(x -> x.name.equals(clientName));

        clients.forEach(x -> {
            synchronized (x.messages) {
                x.messages.add(MessageType.ClientDisconnected + ";" + clientName);
            }
        });
    }

    public static void forceDisconnect(String clientName) {
        clients.stream()
                .filter(x -> x.name.equals(clientName))
                .findFirst()
                .ifPresent(client -> {
                    client.authorized = false;
                    log("force disconnect: " + clientName);
                });
    }

    public static void log(String message) {
        Platform.runLater(() -> systemConsoleTextArea.appendText(message + "\n"));
        System.out.println(message);
    }

    public static String getCurrentTime(){
        var currentTime = LocalTime.now();
        var formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return currentTime.format(formatter);
    }
}
