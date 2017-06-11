package ro.develbox.protocol;

import java.io.IOException;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandLogin;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

@SuppressWarnings("rawtypes")
public abstract class NetworkProtocol extends Protocol implements INetworkProtocol {

	protected boolean connected;

	protected ICommunicationChannel commChannel;

	protected NetworkProtocol(IProtocolResponse responder, ICommunicationChannel commChannel,
			ICommandContructor commandConstructor, Class commandAnnotation) {
		super(responder, commandConstructor, commandAnnotation);
		this.commChannel = commChannel;
	}

	@Override
	final public void connect() throws Exception {
		commChannel.connect();
		this.connected = true;
		afterConnected();
	}

	@Override
	public void disconnect() throws Exception {
		commChannel.disconnect();
		this.connected = false;
	}

	public Command startCommandSequence(Command command)
			throws WarnCommandException, ErrorCommandException, ProtocolViolatedException, IOException {
		Command response = null;
		Command toSend = command;
		// while we did not reached the end of the sequence
		do {
			commChannel.sendCommand(toSend);
			response = commChannel.receiveCommand();
			toSend = validateAndRespond(response);
			if (toSend == null) {
				break;
			}
		}while (lastCommand != null);
		return response;
	}

	protected abstract void afterConnected() throws IOException;

	public Command createCommand(String command) {
		return commandConstructor.createCommandInstance(CommandLogin.COMMAND);
	}

}
