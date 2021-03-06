package ro.develbox.protocol;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ro.develbox.commands.Command;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

@SuppressWarnings("rawtypes")
public abstract class NetworkProtocol extends Protocol implements INetworkProtocol, ICommandReceivedListener {

	private Object srLock = new Object();
	
	private BlockingQueue<Command> commands;

	protected boolean connected;

	protected ICommunicationChannel commChannel;

	protected volatile boolean sequenceStarted = false;

	protected NetworkProtocol(IProtocolResponse responder, ICommunicationChannel commChannel,
			ICommandContructor commandConstructor, Class commandAnnotation) {
		super(responder, commandConstructor, commandAnnotation);
		this.commChannel = commChannel;
		commands = new LinkedBlockingQueue<>();
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
		synchronized (srLock) {
			sequenceStarted = true;
			// while we did not reached the end of the sequence
			do {
				commChannel.sendCommand(toSend);
				response = getReceiveCommand();
				toSend = validateAndRespond(response);
				if (toSend == null) {
					break;
				}
			} while (lastCommand != null);
			sequenceStarted = false;
		}

		return response;
	}

	@Override
	public void onCommandReceived(Command command)
			throws WarnCommandException, ErrorCommandException, ProtocolViolatedException, IOException {
		if (sequenceStarted) {
			try {
				commands.put(command);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else {
			synchronized (srLock) {
				// if we did not start a command sequence, start it now
				Command toSend = validateAndRespond(command);
				if(toSend!=null){
					commChannel.sendCommand(toSend);
				}
				// startCommandSequence(toSend);
			}
		}
	}

	private Command getReceiveCommand() throws IOException {
		Command received = null;
		while (received == null) {
			if(!connected){
				throw new IOException("Disconnected");
			}
			try {
				received = commands.poll(100, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return received;
	}

	protected abstract void afterConnected() throws IOException;

	public Command createCommand(String command) {
		return commandConstructor.createCommandInstance(command);
	}

}
