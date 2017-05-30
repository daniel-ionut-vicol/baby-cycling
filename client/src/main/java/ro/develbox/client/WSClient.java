package ro.develbox.client;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import ro.develbox.commands.Command;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.commands.string.CommandConstructorString;

public class WSClient extends WebSocketAdapter {

    private static final Object lock = new Object();

    private static WebSocketClient client;

    private URI uri;

    private List<IClient> listeners;
    
    ICommandContructor commandConstr ;

    public WSClient(URI uri,ICommandContructor commandConstr) {
        this.uri = uri;
        this.commandConstr = commandConstr;
        listeners = new ArrayList<>();
    }

    public void connect() throws Exception {
        if (client == null) {
            synchronized (lock) {
                if (client == null) {
                    client = new WebSocketClient();
                    client.start();
                }
            }
        }
        Future<Session> fut = client.connect(this, uri);
        while (!fut.isDone()) {
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    @Override
    public void onWebSocketText(String message) {
        // logic for creating command
        Command command = commandConstr.constructCommand(message);
        if (command != null) {
            for (IClient listener : listeners) {
                listener.commandReceived(command);
            }
        }
    }

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        for (IClient listener : listeners) {
            listener.connected();
        }
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        onWebSocketText(new String(payload));
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        for (IClient listener : listeners) {
            listener.disconnected(reason);
        }
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        for (IClient listener : listeners) {
            listener.errorReceived(cause);
        }
    }

    public void sendCommand(Command command) throws IOException {
        if (isConnected()) {
            getRemote().sendString(command.toNetwork());
        } else {
            throw new IOException("Not connected");
        }
    }

    public void addListener(IClient listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(IClient listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

}
