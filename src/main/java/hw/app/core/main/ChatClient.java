package hw.app.core.main;

import java.util.PriorityQueue;

public class ChatClient {
    public String name;
    public boolean authorized = true;
    public final PriorityQueue<String> messages = new PriorityQueue<>();
}
