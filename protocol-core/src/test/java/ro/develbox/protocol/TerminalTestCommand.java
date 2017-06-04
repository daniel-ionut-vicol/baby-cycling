package ro.develbox.protocol;

import ro.develbox.annotation.StartCommand;
import ro.develbox.annotation.TerminalCommand;
import ro.develbox.commands.Command;

@TestAnnotation
@StartCommand
@TerminalCommand
public class TerminalTestCommand extends Command{

    public TerminalTestCommand() {
        super("testTerminal");
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
