package ro.develbox.commands.protocol;

import ro.develbox.annotation.ServerCommand;

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
}
