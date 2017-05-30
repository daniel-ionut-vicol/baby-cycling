package ro.develbox;

import java.net.URI;

import ro.develbox.client.WebSocketClient;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandAuth;
import ro.develbox.commands.CommandRegister;

public class Main {

    public static void main(String[] args) {
        String destUri = "ws://localhost:8080/Server/cyclingWSE";

        try {
            URI uri = new URI(destUri);
            WebSocketClient client = new WebSocketClient(uri, null);
            Command auth = new CommandAuth();
            Command register = new CommandRegister("nick", "email", "regId");
            client.sendCommand(auth);
            client.sendCommand(register);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
        }
        System.exit(1);
    }
}
