package ro.develbox.protocol.client;

import java.io.IOException;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandAuth;
import ro.develbox.commands.CommandConstructorInstance;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.protocol.ICommunicationChannel;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.NetworkProtocol;
import ro.develbox.protocol.ProtocolResponseAuthWrapper;
import ro.develbox.protocol.exceptions.ClientProtocolViolatedException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

@SuppressWarnings("rawtypes")
public class ClientProtocol extends NetworkProtocol {

	public ClientProtocol(IProtocolResponse responder, ICommunicationChannel commChannel,ICommandContructor commandConstrutor) {
		super(new ProtocolResponseAuthWrapper(responder, commandConstrutor), commChannel,
				commandConstrutor, ClientCommand.class);
	}

	@Override
	final protected Class[] getAcceptedCommands() {
		ClientCommand last = (ClientCommand) lastCommand.getClass().getAnnotation(ClientCommand.class);
		return last.nextCommandType();
	}

	@Override
	final protected Class[] getAcceptedResponses() {
		ClientCommand last = (ClientCommand) lastCommand.getClass().getAnnotation(ClientCommand.class);
		return last.responseCommandType();
	}

	@Override
	final protected ProtocolViolatedException getProtocolViolatedException(String cause, Command command,
			Command prevCommand) {
		return new ClientProtocolViolatedException(cause, command, prevCommand);
	}

	@Override
	final protected void afterConnected() throws IOException {
		CommandAuth auth = (CommandAuth) commandConstructor.createCommandInstance(CommandAuth.COMMAND);
		// TODO set auth key
		commChannel.sendCommand(auth);
	}

}
