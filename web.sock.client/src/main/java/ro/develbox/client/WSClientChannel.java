package ro.develbox.client;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandConstructorInstance;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.protocol.ICommunicationChannel;

public class WSClientChannel extends WebSocketAdapter implements ICommunicationChannel{

	private static final Object lock = new Object();

    private static WebSocketClient client;
    
    private BlockingQueue<Command> commands;
    
    private URI uri;
    ICommandContructor commandConstr;
    
    public WSClientChannel(URI uri){
    	this(uri,CommandConstructorInstance.commandConstructor);
    }
    
    public WSClientChannel(URI uri, ICommandContructor commandConstr) {
        this.uri = uri;
        this.commandConstr = commandConstr;
        commands = new LinkedBlockingQueue<>();
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
        Future<Session> fut = client.connect(this, uri);
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
		if (isConnected()) {
            getRemote().sendString(command.toNetwork());
        } else {
            throw new IOException("Not connected");
        }
	}
	
	@Override
	public Command receiveCommand() {
		try {
			Command received = null;
			while(isConnected()&& received==null){
				received = commands.poll(100, TimeUnit.MILLISECONDS);
			}
			return received;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    @Override
    public void onWebSocketText(String message) {
        // logic for creating command
        Command command = commandConstr.constructCommand(message);
        if (command != null) {
            try {
				commands.offer(command,Long.MAX_VALUE,TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }

}
