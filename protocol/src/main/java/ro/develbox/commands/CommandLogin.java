package ro.develbox.commands;

import ro.develbox.annotation.CommandType;

@CommandType(server=true, nextCommandType = {})
public class CommandLogin extends Command{

	public static final String COMMAND = "login:";
	
	private String email ;
	
	public CommandLogin() {
		super(COMMAND);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void getParametersFromNetwork(String stringParameters) {
			setEmail(stringParameters);	
	}

	@Override
	protected String toNetworkParameters() {
		return getEmail();
	}
	
}
