package ro.develbox;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ro.develbox.commands.Command;
import ro.develbox.protocol.ICommunicationChannel;
import ro.develbox.protocol.server.ServerProtocol;

@ServerEndpoint(value = "/cyclingWSE")
public class WebSocketEndPoint implements ICommunicationChannel {

    private ServerProtocol serverProtocol;
    private Session session;
    private BlockingQueue<Command> commands;
    
    public WebSocketEndPoint(){
    	commands = new LinkedBlockingQueue<>();
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Open");
        this.session = session;
        serverProtocol = new ServerProtocolImpl(this);
        try {
			serverProtocol.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close");
        this.session = null;
        try {
			serverProtocol.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        serverProtocol = null;
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        System.out.println("MessageReceived : " + message);
        Command command = serverProtocol.getCommandConstructor().constructCommand(message);
        try {
			commands.offer(command, Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @OnError
    public void onError(Session session, Throwable e) {
        System.out.println("ERROR:");
        e.printStackTrace(System.out);
    }

    @Override
    public void sendCommand(Command command) {
        if (session != null) {
            try {
                session.getBasicRemote().sendText(command.toNetwork());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	@Override
	public void connect() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Command receiveCommand() {
		Command received = null;
		while(session!=null && received==null){
			try {
				received = commands.poll(100, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return received;
	}

}
