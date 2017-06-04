package ro.develbox.protocol.client;

import ro.develbox.commands.Command;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

public interface IClient {

    public void commandClientReceived(Command command) throws WarnCommandException, ErrorCommandException, ProtocolViolatedException ;

    public void errorReceived(Throwable e);

    public void connected();

    public void disconnected(String reason);

}
