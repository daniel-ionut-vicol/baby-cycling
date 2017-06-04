package ro.develbox.commands;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;

@ClientCommand
@ServerCommand
public abstract class CommandReset extends Command{

    public static final String COMMAND = "rset:";
    
    public CommandReset() {
        super(COMMAND);
    }
    
}
