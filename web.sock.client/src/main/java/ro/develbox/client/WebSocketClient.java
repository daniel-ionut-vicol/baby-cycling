package ro.develbox.client;

import java.io.IOException;
import java.net.URI;

import ro.develbox.commands.Command;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.protocol.ICommandSender;
import ro.develbox.protocol.INetworkCommandReceiver;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.client.ClientProtocol;

public class WebSocketClient implements INetworkCommandReceiver, IProtocolResponse, ICommandSender{

    private WSClient wsClient;
    private ClientProtocol cProtocol;
    private ClientExceptionListener exListener;

    public WebSocketClient(URI uri, ClientExceptionListener exListener,ICommandContructor commandConstr) throws Exception {
        this.exListener = exListener;
        wsClient = new WSClient(uri,commandConstr);
        wsClient.connect();
        wsClient.addListener(this);
        cProtocol = new ClientProtocol(this, this){};
    }

    @Override
    public Command getCommandResponse(Command command) {
        return null;
    }

    @Override
    public Command commandReceived(Command command) {
        try {
            Command response = cProtocol.commandReceived(command);
            return response;
        } catch (Exception e) {
            redirectException(e);
        }
        return null;
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
