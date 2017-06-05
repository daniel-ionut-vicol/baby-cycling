package ro.develbox.commands;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.annotation.StartCommand;
import ro.develbox.annotation.TerminalCommand;

/**
 * Command sent to authenticate the client/server
 * @author danielv
 *
 */
//response to command is a same type command 
//this is the start and the end of the command succession
@ServerCommand(responseCommandType={CommandAuth.class})
@ClientCommand(responseCommandType={CommandAuth.class})
@StartCommand
@TerminalCommand
public abstract class CommandAuth extends Command {

    public static final String COMMAND = "auth:";

    /**
     * Client key
     */
    private String key = "asdasdasd";

    public CommandAuth() {
        super(COMMAND);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
