package ro.develbox.commands.string;

import ro.develbox.annotation.CommandType;
import ro.develbox.commands.CommandAuth;

@CommandType(server = true, nextCommandType = {})
public class CommandAuthString extends CommandAuth {

    @Override
    public String toNetwork() {
        return COMMAND+getKey();
    }

    @Override
    public void fromNetwork(String networkRep) {
        setKey(networkRep);
    }

}
