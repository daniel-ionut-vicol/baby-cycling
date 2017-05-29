package ro.develbox.commands;

import ro.develbox.annotation.CommandType;

@CommandType(server=true, nextCommandType = {})
public class CommandAuth extends Command{

	public static final String COMMAND = "auth:";
	
	private String key = "asdasdasd";
	
	public CommandAuth(){
		super(COMMAND);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void getParametersFromNetwork(String stringParameters) {
			setKey(stringParameters);	
	}

	@Override
	protected String toNetworkParameters() {
		return getKey();
	}
	
}
