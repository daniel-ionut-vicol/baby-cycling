package ro.develbox.commands;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.StartCommand;
import ro.develbox.commands.Command;

@ClientCommand
@StartCommand
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
