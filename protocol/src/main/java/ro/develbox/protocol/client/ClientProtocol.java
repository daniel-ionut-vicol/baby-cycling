package ro.develbox.protocol.client;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.commands.Command;
import ro.develbox.protocol.ICommandSender;
import ro.develbox.protocol.INetworkCommandReceiver;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.Protocol;
import ro.develbox.protocol.exceptions.ClientProtocolViolatedException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

@SuppressWarnings("rawtypes")
public class ClientProtocol extends Protocol implements INetworkCommandReceiver{

    public ClientProtocol(IProtocolResponse responder, ICommandSender sender) {
        super(responder, sender, ClientCommand.class);
    }

    @Override
    protected Class[] getAcceptedCommands() {
        ClientCommand last = (ClientCommand)lastCommand.getClass().getAnnotation(ClientCommand.class);
        return last.nextCommandType();
    }

    @Override
    protected Class[] getAcceptedResponses() {
        ClientCommand last = (ClientCommand)lastCommand.getClass().getAnnotation(ClientCommand.class);
        return last.responseCommandType();
    }

    @Override
    protected ProtocolViolatedException getProtocolViolatedException(String cause, Command command,
            Command prevCommand) {
        return new ClientProtocolViolatedException(cause, command, prevCommand);
    }

    @Override
    public void errorReceived(Throwable e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void connected() {
        //TODO send auth
    }

    @Override
    public void disconnected(String reason) {
        // TODO Auto-generated method stub
        
    }
    
    
    
}
