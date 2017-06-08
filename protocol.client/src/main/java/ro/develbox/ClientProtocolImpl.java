package ro.develbox;

import ro.develbox.commands.CommandLogin;
import ro.develbox.protocol.ICommandSender;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.client.ClientProtocol;
public class ClientProtocolImpl extends ClientProtocol{

	public ClientProtocolImpl(IProtocolResponse responder, ICommandSender sender) {
		super(responder, sender);
		// TODO Auto-generated constructor stub
	}

	public void login(String email){
		CommandLogin login = (CommandLogin)commandConstructor.createCommandInstance(CommandLogin.COMMAND);
		login.setEmail(email);
		login.setPassword(email);
	}
	
}
