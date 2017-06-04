package ro.develbox.protocol.server;

import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.Command;
import ro.develbox.protocol.ICommandSender;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.Protocol;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;
import ro.develbox.protocol.exceptions.ServerProtocolViolatedException;

@SuppressWarnings("rawtypes")
public class ServerProtocol extends Protocol {

    public ServerProtocol(IProtocolResponse responder, ICommandSender sender) {
        super(responder, sender, ServerCommand.class);
    }

    @Override
    protected Class[] getAcceptedCommands() {
        ServerCommand last = (ServerCommand)lastCommand.getClass().getAnnotation(ServerCommand.class);
        return last.nextCommandType();
    }

    @Override
    protected Class[] getAcceptedResponses() {
        ServerCommand last = (ServerCommand)lastCommand.getClass().getAnnotation(ServerCommand.class);
        return last.responseCommandType();
    }

    @Override
    protected ProtocolViolatedException getProtocolViolatedException(String cause, Command command,
            Command prevCommand) {
        return new ServerProtocolViolatedException(cause, command, prevCommand);
    }
    
    
    
}
