package ro.develbox.client;

import java.io.IOException;
import java.net.URI;

import ro.develbox.commands.Command;
import ro.develbox.commands.protocol.ClientProtocol;

public class WebSocketClient implements IClient {

    private WSClient wsClient;
    private ClientProtocol cProtocol;
    private ClientExceptionListener exListener;

    public WebSocketClient(URI uri, ClientExceptionListener exListener) throws Exception {
        this.exListener = exListener;
        wsClient = new WSClient(uri);
        wsClient.connect();
        wsClient.addListener(this);
        cProtocol = new ClientProtocol(this, this);
    }

    @Override
    public Command getCommandResponse(Command command) {
        return null;
    }

    @Override
    public void commandReceived(Command command) {
        try {
            Command response = cProtocol.commandReceived(command);
            sendCommand(response);
        } catch (Exception e) {
            redirectException(e);
        }
    }

    @Override
    public void sendCommand(Command command) {
        try {
            wsClient.sendCommand(command);
        } catch (IOException e) {
            redirectException(e);
        }
    }

    @Override
    public void errorReceived(Throwable e) {
    }

    @Override
    public void connected() {
    }

    @Override
    public void disconnected(String reason) {
    }

    private void redirectException(Exception e) {
        if (exListener != null) {
            exListener.exceptionOccured(e);
        } else {
            e.printStackTrace();
        }
    }

}
