package ro.develbox.commands.string;

import ro.develbox.commands.CommandRegister;

public class CommandRegisterString extends CommandRegister {

    public CommandRegisterString(){
        super();
    }
    
    public CommandRegisterString(String nickName, String email, String registrationId) {
        super(nickName, email, registrationId);
    }
    
    @Override
    public void fromNetwork(String stringParameters) {
        String[] params = stringParameters.split(CommandConstructorString.PARAMSEP);
        setNickName(params[0]);
        setEmail(params[1]);
        setRegistrationId(params[2]);
    }

    @Override
    public String toNetwork() {
        return COMMAND + getNickName() + CommandConstructorString.PARAMSEP + getEmail() + CommandConstructorString.PARAMSEP + getRegistrationId();
    }

}
