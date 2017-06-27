package ro.develbox.commands;

import ro.develbox.commands.CommandMessage.TYPE;

/**
 * Interface for converting message receive from network to internal representation
 * @author danielv
 *
 */
public interface ICommandContructor {

    public static final String PARAMSEP = "0012300";
	
    public Command constructCommand(String strCommand);

    public CommandMessage contructMessageCommand(TYPE type, String message);

    public Command createCommandInstance(String commandName);
}
