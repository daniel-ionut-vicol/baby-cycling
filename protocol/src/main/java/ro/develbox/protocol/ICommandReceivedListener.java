package ro.develbox.protocol;

import java.io.IOException;

import ro.develbox.commands.Command;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

public interface ICommandReceivedListener {
	public void onCommandReceived(Command command) throws WarnCommandException, ErrorCommandException, ProtocolViolatedException, IOException;
}
