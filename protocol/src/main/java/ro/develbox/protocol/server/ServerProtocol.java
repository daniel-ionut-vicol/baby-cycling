package ro.develbox.protocol.server;

import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandAuth;
import ro.develbox.commands.CommandConstructorInstance;
import ro.develbox.protocol.ICommandSender;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.NetworkProtocol;
import ro.develbox.protocol.ProtocolResponseAuthWrapper;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;
import ro.develbox.protocol.exceptions.ServerProtocolViolatedException;

@SuppressWarnings("rawtypes")
public abstract class ServerProtocol extends NetworkProtocol{

    public ServerProtocol(IProtocolResponse responder, ICommandSender sender) {
        super(new ProtocolResponseAuthWrapper(responder, CommandConstructorInstance.commandConstructor), sender,
                CommandConstructorInstance.commandConstructor, ServerCommand.class);
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
    public void errorReceived(Throwable e) {
        // TODO Auto-generated method stub
    }

    @Override
    final protected void afterConnected() {
        CommandAuth auth = (CommandAuth)commandConstructor.createCommandInstance(CommandAuth.COMMAND);
        //TODO set auth key
        sender.sendCommand(auth);
    }

    @Override
    public void disconnected(String reason) {
        // TODO Auto-generated method stub

    }

}
