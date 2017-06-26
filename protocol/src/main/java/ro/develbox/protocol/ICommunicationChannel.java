package ro.develbox.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ro.develbox.commands.Command;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

public abstract class ICommunicationChannel implements INetworkProtocol{

	List<ICommandReceivedListener> listeners;
	
	public ICommunicationChannel() {
		listeners = new ArrayList<>();
	}
	
	public abstract void sendCommand(Command command) throws IOException;
	
	public void onReceiveCommand(Command command){
		for(ICommandReceivedListener listener: listeners)
		{
			try {
				listener.onCommandReceived(command);
			} catch (WarnCommandException | ErrorCommandException | ProtocolViolatedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addListener(ICommandReceivedListener listener){
		listeners.add(listener);
	}
	
}
