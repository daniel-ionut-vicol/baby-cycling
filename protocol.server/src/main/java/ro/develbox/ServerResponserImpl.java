package ro.develbox;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandLogin;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.CommandRegister;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.model.User;
import ro.develbox.protocol.IProtocolResponse;

public class ServerResponserImpl implements IProtocolResponse{

	private ICommandContructor commandConstr;
	
	private User loggedUser ;

	public void setCommandConstr(ICommandContructor commandConstr) {
		this.commandConstr = commandConstr;
	}

	@Override
	public Command getCommandResponse(Command command) {
		if(command instanceof CommandLogin){
			return handleLogin((CommandLogin)command);
		}else if (command instanceof CommandRegister){
			return handleRegister((CommandRegister)command);
		}
		return null;
	}

	private Command handleLogin(CommandLogin loginInfo){
		//TODO validate login data
		loggedUser = new User();
		CommandMessage response = (CommandMessage)commandConstr.constructCommand(CommandMessage.COMMAND);
		response.setType(TYPE.OK);
		response.setMessage("OK");
		return response;
	}
	
	private Command handleRegister(CommandRegister registerInfo){
		//TODO validate register data and add user
		loggedUser = new User();
		CommandMessage response = (CommandMessage)commandConstr.constructCommand(CommandMessage.COMMAND);
		response.setType(TYPE.OK);
		response.setMessage("OK");
		return response;
	}
	
	

}
