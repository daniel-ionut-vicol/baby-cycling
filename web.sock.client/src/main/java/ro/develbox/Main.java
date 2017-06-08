package ro.develbox;

import java.net.URI;

import ro.develbox.client.WebSocketClient;
import ro.develbox.commands.Command;
import ro.develbox.commands.string.CommandAuthString;
import ro.develbox.commands.string.CommandConstructorString;
import ro.develbox.commands.string.CommandRegisterString;

public class Main {

    public static void main(String[] args) {
        String destUri = "ws://localhost:8080/Server/cyclingWSE";

        try {
            URI uri = new URI(destUri);
            WebSocketClient client = new WebSocketClient(uri, null, new CommandConstructorString());
            Command auth = new CommandAuthString();
            Command register = new CommandRegisterString("nick", "email", "regId");
            client.sendCommand(auth);
            client.sendCommand(register);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
        }
        System.exit(1);
    }
}
