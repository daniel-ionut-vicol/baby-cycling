package ro.develbox.protocol;

import java.io.IOException;

import ro.develbox.commands.Command;

public interface ICommunicationChannel extends INetworkProtocol{

	public void sendCommand(Command command) throws IOException;
	public Command receiveCommand();
	
}
