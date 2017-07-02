package ro.develbox.protocol.server;

import java.io.IOException;

import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandAuth;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.protocol.ICommunicationChannel;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.NetworkProtocol;
import ro.develbox.protocol.ProtocolResponseAuthWrapper;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;
import ro.develbox.protocol.exceptions.ServerProtocolViolatedException;

@SuppressWarnings("rawtypes")
public class ServerProtocol extends NetworkProtocol{

    public ServerProtocol(IProtocolResponse responder, ICommunicationChannel commChannel,ICommandContructor commandConstrutor) {
        super(new ProtocolResponseAuthWrapper(responder, commandConstrutor), commChannel,
        		commandConstrutor, ServerCommand.class);
    }

    @Override
    final protected Class[] getAcceptedCommands() {
        ServerCommand last = (ServerCommand)lastCommand.getClass().getAnnotation(ServerCommand.class);
        return last.nextCommandType();
    }

    @Override
    final protected Class[] getAcceptedResponses() {
        ServerCommand last = (ServerCommand)lastCommand.getClass().getAnnotation(ServerCommand.class);
        return last.responseCommandType();
    }

    @Override
    final protected ProtocolViolatedException getProtocolViolatedException(String cause, Command command,
            Command prevCommand) {
        return new ServerProtocolViolatedException(cause, command, prevCommand);
    }

    @Override
    final protected void afterConnected() throws IOException {
        CommandAuth auth = (CommandAuth)commandConstructor.createCommandInstance(CommandAuth.COMMAND);
        //TODO set auth key
        commChannel.sendCommand(auth);
    }
}
