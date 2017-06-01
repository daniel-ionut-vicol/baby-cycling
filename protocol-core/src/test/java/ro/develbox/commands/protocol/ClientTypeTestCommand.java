package ro.develbox.commands.protocol;

import ro.develbox.annotation.CommandInfo;
import ro.develbox.commands.Command;

@CommandInfo(server = false, client = true)
public class ClientTypeTestCommand extends Command {
    public ClientTypeTestCommand() {
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
