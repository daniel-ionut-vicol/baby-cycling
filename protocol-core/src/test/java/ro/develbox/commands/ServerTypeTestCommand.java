package ro.develbox.commands;

import ro.develbox.annotation.ServerCommand;
import ro.develbox.annotation.StartCommand;
import ro.develbox.commands.Command;

@ServerCommand
@StartCommand
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
