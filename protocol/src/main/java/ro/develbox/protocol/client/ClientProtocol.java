package ro.develbox.protocol.client;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandAuth;
import ro.develbox.commands.CommandConstructorInstance;
import ro.develbox.protocol.ICommandSender;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.NetworkProtocol;
import ro.develbox.protocol.ProtocolResponseAuthWrapper;
import ro.develbox.protocol.exceptions.ClientProtocolViolatedException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

@SuppressWarnings("rawtypes")
public class ClientProtocol extends NetworkProtocol{

    public ClientProtocol(IProtocolResponse responder, ICommandSender sender) {

        super(new ProtocolResponseAuthWrapper(responder, CommandConstructorInstance.commandConstructor), sender,
                CommandConstructorInstance.commandConstructor, ClientCommand.class);
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
    protected void afterConnected() {
        CommandAuth auth = (CommandAuth)commandConstructor.createCommandInstance(CommandAuth.COMMAND);
        //TODO set auth key
        sender.sendCommand(auth);
    }

    @Override
    public void disconnected(String reason) {
        // TODO Auto-generated method stub

    }

}
