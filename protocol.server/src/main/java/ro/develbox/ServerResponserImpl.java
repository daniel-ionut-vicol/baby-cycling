package ro.develbox;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandAuth;
import ro.develbox.commands.CommandLogin;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.CommandRegister;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.model.User;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.server.ServerProtocolApi;
import ro.develbox.protocol.server.ServerResponderApi;

public class ServerResponserImpl extends ServerResponderApi{
	
	private User loggedUser ;	

	protected Command handleLogin(CommandLogin loginInfo){
		//TODO validate login data
		loggedUser = new User();
		CommandMessage response = (CommandMessage)createCommand(CommandMessage.COMMAND);
		response.setType(TYPE.OK);
		response.setMessage("OK");
		return response;
	}
	
	protected Command handleRegister(CommandRegister registerInfo){
		//TODO validate register data and add user
		loggedUser = new User();
		CommandMessage response = (CommandMessage)createCommand(CommandMessage.COMMAND);
		response.setType(TYPE.OK);
		response.setMessage("OK");
		return response;
	}

	@Override
	protected Command handleAuth(CommandAuth command) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
