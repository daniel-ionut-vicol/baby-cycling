package ro.develbox.protocol;

import ro.develbox.commands.Command;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

public interface ICommandReceiver {

    public Command commandReceived(Command command) throws WarnCommandException, ErrorCommandException, ProtocolViolatedException;
}
