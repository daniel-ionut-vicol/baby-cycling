package ro.develbox.commands;

public abstract class Command {
	private String command ;
	
	public Command(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}
	
	final public String toNetwork(){
		return command + toNetworkParameters(); 
	}
	
	abstract public void getParametersFromNetwork(String stringParameters);
	abstract protected String toNetworkParameters();
	
	//TODO ADD a better way for this 
	public static Command constructCommand(String strCommand){
		Command command = null;
		if(strCommand==null){
			return null;
		}
		String commandParams = null;
		if(strCommand.startsWith(CommandLogin.COMMAND)){
			command = new CommandLogin();
			commandParams = strCommand.substring(CommandLogin.COMMAND.length());
		}else if (strCommand.startsWith(CommandRegister.COMMAND)){
			command = new CommandRegister();
			commandParams = strCommand.substring(CommandRegister.COMMAND.length());
		}else if (strCommand.startsWith(CommandAuth.COMMAND)){
			command = new CommandAuth();
			commandParams = strCommand.substring(CommandAuth.COMMAND.length());
		}else if (strCommand.startsWith(CommandMessage.COMMAND)){
			command = new CommandMessage();
			commandParams = strCommand.substring(CommandMessage.COMMAND.length());
		}
		else {
			return null;
		}
		command.getParametersFromNetwork(commandParams);
		return command;
	}
	
}
