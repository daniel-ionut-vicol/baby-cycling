package ro.develbox.commands;

import ro.develbox.annotation.StartCommand;
import ro.develbox.annotation.TestAnnotation;
import ro.develbox.commands.Command;

@TestAnnotation
@StartCommand
public class StartTestCommand extends Command{

    public StartTestCommand() {
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
