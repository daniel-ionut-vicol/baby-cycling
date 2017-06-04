package ro.develbox.commands.protocol;

import ro.develbox.annotation.ClientCommand;

public class ClientProtocol extends Protocol {

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
    
}
