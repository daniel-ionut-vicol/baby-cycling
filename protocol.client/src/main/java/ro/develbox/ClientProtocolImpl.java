package ro.develbox;

import java.io.IOException;

import ro.develbox.commands.CommandLogin;
import ro.develbox.commands.CommandRegister;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.ICommunicationChannel;
import ro.develbox.protocol.client.ClientProtocol;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

public class ClientProtocolImpl extends ClientProtocol {

	public ClientProtocolImpl(ICommunicationChannel commChannel) {
		super(new ClientResponserImpl(), commChannel);
	}

	public void login(String email,String password) throws WarnCommandException, ErrorCommandException, ProtocolViolatedException, IOException {
		CommandLogin login = (CommandLogin) createCommand(CommandLogin.COMMAND);
		login.setEmail(email);
		login.setPassword(password);
		startCommandSequence(login);
	}

	public void register(String email, String nickName, String registrationId)
			throws WarnCommandException, ErrorCommandException, ProtocolViolatedException, IOException {
		CommandRegister register = (CommandRegister) createCommand(CommandRegister.COMMAND);
		register.setEmail(email);
		register.setNickName(nickName);
		register.setRegistrationId(registrationId);
		startCommandSequence(register);
	}

}
