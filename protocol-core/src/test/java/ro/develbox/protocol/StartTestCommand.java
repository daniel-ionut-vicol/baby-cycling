package ro.develbox.protocol;

import ro.develbox.annotation.StartCommand;
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
