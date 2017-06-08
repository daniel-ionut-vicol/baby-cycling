package ro.develbox;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandLogin;
import ro.develbox.protocol.ICommandSender;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.client.ClientProtocol;
public class ClientProtocolImpl extends ClientProtocol implements IProtocolResponse{

	public ClientProtocolImpl(IProtocolResponse responder, ICommandSender sender) {
		super(responder, sender);
		
	}

	public void login(String email){
		CommandLogin login = (CommandLogin)commandConstructor.createCommandInstance(CommandLogin.COMMAND);
		login.setEmail(email);
		login.setPassword(email);
	}

	@Override
	public Command getCommandResponse(Command command) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
