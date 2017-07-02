package ro.develbox.protocol;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandLogin;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

@SuppressWarnings("rawtypes")
public abstract class NetworkProtocol extends Protocol implements INetworkProtocol,ICommandReceivedListener {

	private BlockingQueue<Command> commands;
	
	protected boolean connected;

	protected ICommunicationChannel commChannel;

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
		// while we did not reached the end of the sequence
		do {
			commChannel.sendCommand(toSend);
			if(lastCommand!=null){
				response = getReceiveCommand();
				toSend = validateAndRespond(response);
				if (toSend == null) {
					break;
				}
			}
		}while (lastCommand != null);
		return response;
	}

	@Override
	public void onCommandReceived(Command command) throws WarnCommandException, ErrorCommandException, ProtocolViolatedException, IOException {
		
		if(lastCommand == null){
			//if we did not start a command sequence, start it now 
			Command toSend = validateAndRespond(command);
			startCommandSequence(toSend);
		}else{
			try {
				commands.put(command);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Command getReceiveCommand() {
		Command received = null;
		while(received==null){
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
		return commandConstructor.createCommandInstance(CommandLogin.COMMAND);
	}

}
