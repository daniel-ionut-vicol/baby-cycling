package ro.develbox.commands.protocol;


public class ServerProtocol extends Protocol{
	
	public ServerProtocol(IProtocolResponse responder, ICommandSender sender) {
		super(responder,sender,true);
	}
}
