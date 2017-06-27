package ro.develbox.commands.string;

import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.ICommandContructor;

public class CommandMessageString extends CommandMessage {

    public CommandMessageString() {
		super();
	}

	public CommandMessageString(TYPE type, String message) {
		super(type, message);
	}

	@Override
    public void fromNetwork(String stringParameters) {
        String[] params = stringParameters.split(ICommandContructor.PARAMSEP);
        setType(params[0]);
        setMessage(params[1]);
    }

    @Override
    public String toNetwork() {
        return COMMAND + getStringType() + ICommandContructor.PARAMSEP + getMessage();
    }

}
