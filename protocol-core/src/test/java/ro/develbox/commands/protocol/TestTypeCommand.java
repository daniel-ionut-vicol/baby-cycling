package ro.develbox.commands.protocol;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.Command;

@ClientCommand(nextCommandType={TestTypeCommand.class},responseCommandType={TestTypeCommand.class})
@ServerCommand(nextCommandType={TestTypeCommand.class},responseCommandType={TestTypeCommand.class})
public class TestTypeCommand extends Command{

    public TestTypeCommand() {
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
