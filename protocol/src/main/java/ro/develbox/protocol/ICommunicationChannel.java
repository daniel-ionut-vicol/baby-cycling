package ro.develbox.protocol;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ro.develbox.commands.Command;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

public abstract class ICommunicationChannel implements INetworkProtocol{
	Set<ICommandReceivedListener> listeners;
	
	public ICommunicationChannel() {
		listeners = new HashSet<>();
	}
	
	public abstract void sendCommand(Command command) throws IOException;
	
	public void onReceiveCommand(final Command command){
		for(final ICommandReceivedListener listener: listeners)
		{
			new Thread() {
				public void run() {
					try {
						listener.onCommandReceived(command);
					} catch (WarnCommandException | ErrorCommandException | ProtocolViolatedException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
		}
	}
	
	public void addListener(ICommandReceivedListener listener){
		listeners.add(listener);
	}
	
}
