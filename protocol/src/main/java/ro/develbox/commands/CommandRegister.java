package ro.develbox.commands;

import ro.develbox.annotation.CommandType;

@CommandType(server=true,nextCommandType={CommandLogin.class})
public class CommandRegister extends Command{
	public static final String COMMAND = "reg:" ;
	
	private String nickName;
	private String email;
	private String registrationId;
	
	public CommandRegister() {
		super(COMMAND);
	}
	
	public CommandRegister(String nickName, String email,
			String registrationId) {
		super(COMMAND);
		this.nickName = nickName;
		this.email = email;
		this.registrationId = registrationId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	@Override
	public void getParametersFromNetwork(String stringParameters) {
		String [] params = stringParameters.split(",");
		setNickName(params[0]);
		setEmail(params[1]);
		setRegistrationId(params[2]);
	}

	@Override
	protected String toNetworkParameters() {
		return getNickName()+","+getEmail()+","+getRegistrationId();
	}
	
}
