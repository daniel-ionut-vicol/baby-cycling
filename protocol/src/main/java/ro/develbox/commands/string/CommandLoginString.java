package ro.develbox.commands.string;

import ro.develbox.commands.CommandLogin;

public class CommandLoginString extends CommandLogin {
    
    @Override
    public void fromNetwork(String networkRep) {
        String[] params = networkRep.split(CommandConstructorString.PARAMSEP);
        setEmail(params[0]);
        setPassword(params[1]);
    }
    
    @Override
    public String toNetwork() {
        return COMMAND + getEmail()+CommandConstructorString.PARAMSEP+getPassword();
    }

}
