package ro.develbox.commands.string;

import ro.develbox.commands.CommandMessage;

public class CommandMessageString extends CommandMessage {

    @Override
    public void fromNetwork(String stringParameters) {
        String[] params = stringParameters.split(CommandConstructorString.PARAMSEP);
        setType(params[0]);
        setMessage(params[1]);
    }

    @Override
    public String toNetwork() {
        return COMMAND + getStringType() + CommandConstructorString.PARAMSEP + getMessage();
    }

}
