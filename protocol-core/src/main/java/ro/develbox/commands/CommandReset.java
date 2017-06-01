package ro.develbox.commands;

public abstract class CommandReset extends Command{

    public static final String COMMAND = "rset:";
    
    public CommandReset() {
        super(COMMAND);
    }
    
}
