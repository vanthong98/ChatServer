package hw.app.core;

import hw.app.core.common.DataType;
import hw.app.core.common.MessageType;

import java.util.ArrayList;
import java.util.UUID;

public class Dispatcher {
    public static ArrayList<ChatClient> clients = new ArrayList<>();
    public static ArrayList<ChatGroup> groups = new ArrayList<>();

    public static void SendMessageToClient(UUID clientId, String message, MessageType messageType, DataType dataType) {

    }
}
