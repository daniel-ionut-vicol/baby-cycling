package ro.develbox;

import java.io.IOException;

import ro.develbox.commands.Command;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.ICommunicationChannel;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;
import ro.develbox.protocol.server.ServerProtocol;

public class ServerProtocolImpl extends ServerProtocol{

	public ServerProtocolImpl(ICommunicationChannel commChannel) {
		super(new ServerResponserImpl(), commChannel);
	}
	
	@Override
	public Command validateAndRespond(Command receivedCommand)
			throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
		Command command = super.validateAndRespond(receivedCommand);
		try {
			startCommandSequence(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return command;
	}
	
}
