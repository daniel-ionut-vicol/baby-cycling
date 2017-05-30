package ro.develbox.commands.protocol;

import ro.develbox.annotation.CommandType;
import ro.develbox.commands.Command;

@CommandType(server = true, client = false)
public class ServerTypeTestCommand extends Command {
    public ServerTypeTestCommand() {
        super("test");
    }

    @Override
    public String toNetwork() {
        return null;
    }

    @Override
    public void fromNetwork(String networkRep) {
        // TODO Auto-generated method stub
    }
}
