package ro.develbox;

import java.net.URI;

import ro.develbox.client.WebSockCommunicationChannel;

public class Main {

    public static void main(String[] args) {
        String destUri = "ws://localhost:8080/web.sock.server/cyclingWSE";

        try {
            URI uri = new URI(destUri);
            WebSockCommunicationChannel channel = new WebSockCommunicationChannel(uri);
            
            ClientProtocolImpl client = new ClientProtocolImpl(channel);
            client.connect();
            channel.setCommandConstr(client.getCommandConstructor());
            client.login("email","pass");
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
        }
        System.exit(1);
    }
}
