package ro.develbox.protocol;

import java.io.IOException;

import ro.develbox.commands.Command;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

public class NetworkProtocolImplMock extends NetworkProtocol{

	boolean afterConnectCalled;
	
	protected NetworkProtocolImplMock(IProtocolResponse responder, ICommunicationChannel commChannel,
			ICommandContructor commandConstructor, Class commandAnnotation) {
		super(responder, commChannel, commandConstructor, commandAnnotation);
	}
	
	@Override
	protected void afterConnected() throws IOException {
		afterConnectCalled = true;
	}

	@Override
	protected Class[] getAcceptedCommands() {
		return null;
	}

	@Override
	protected Class[] getAcceptedResponses() {
		return null;
	}

	@Override
	protected ProtocolViolatedException getProtocolViolatedException(String cause, Command command,
			Command prevCommand) {
		return null;
	}

}
