package ro.develbox.protocol;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.Command;

@ServerCommand
@ClientCommand
public class NonStartCommand extends Command{

    public NonStartCommand() {
        super("test");
    }

    @Override
    public String toNetwork() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void fromNetwork(String networkRep) {
        // TODO Auto-generated method stub
        
    }

}
