package ro.develbox.client;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import ro.develbox.commands.Command;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.protocol.ICommunicationChannel;

public class WebSockCommunicationChannel extends ICommunicationChannel{

	private static final Object lock = new Object();
	private WebSocketClient client;
	private WebSocketAdapter adapter;
	private URI uri;
    ICommandContructor commandConstr;
    
    public WebSockCommunicationChannel(URI uri) {
        this.uri = uri;
        adapter = new WebSocketAdapter(){
        	@Override
        	public void onWebSocketText(String message) {
        		Command command = WebSockCommunicationChannel.this.commandConstr.constructCommand(message);
                onReceiveCommand(command);
        	}
        };
    }
	
    public ICommandContructor getCommandConstr() {
		return commandConstr;
	}

	public void setCommandConstr(ICommandContructor commandConstr) {
		this.commandConstr = commandConstr;
	}
    
	@Override
	public void connect() throws Exception {
		if (client == null) {
            synchronized (lock) {
                if (client == null) {
                    client = new WebSocketClient();
                    client.start();
                }
            }
        }
        Future<Session> fut = client.connect(adapter, uri);
        while (!fut.isDone()) {
            TimeUnit.MILLISECONDS.sleep(100);
        }
	}

	@Override
	public void disconnect() throws Exception {
		client.destroy();
	}

	@Override
	public void sendCommand(Command command) throws IOException {
		adapter.getSession().getRemote().sendString(command.toNetwork());
	}

}
