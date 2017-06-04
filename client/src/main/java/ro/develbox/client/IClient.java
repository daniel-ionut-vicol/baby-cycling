package ro.develbox.client;

import ro.develbox.commands.Command;
import ro.develbox.protocol.ICommandSender;
import ro.develbox.protocol.IProtocolResponse;

public interface IClient extends IProtocolResponse, ICommandSender {

    public void commandReceived(Command command);

    public void errorReceived(Throwable e);

    public void connected();

    public void disconnected(String reason);

}
