package ro.develbox.commands;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.annotation.StartCommand;
import ro.develbox.annotation.TestAnnotation;
import ro.develbox.commands.Command;

@ClientCommand(nextCommandType={TestTypeCommand.class},responseCommandType={TestTypeCommand.class})
@ServerCommand(nextCommandType={TestTypeCommand.class},responseCommandType={TestTypeCommand.class})
@TestAnnotation
@StartCommand
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
