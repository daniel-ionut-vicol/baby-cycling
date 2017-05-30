package ro.develbox.commands;

import ro.develbox.commands.CommandMessage.TYPE;

/**
 * Interface for converting message receive from network to internal representation
 * @author danielv
 *
 */
public interface ICommandContructor {

    public Command constructCommand(String strCommand);

    public CommandMessage contructMessageCommand(TYPE type, String message);

}
