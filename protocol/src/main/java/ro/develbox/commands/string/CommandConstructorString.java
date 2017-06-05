package ro.develbox.commands.string;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandAuth;
import ro.develbox.commands.CommandLogin;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.CommandRegister;
import ro.develbox.commands.ICommandContructor;

public class CommandConstructorString implements ICommandContructor {

    public static final String PARAMSEP = ",,++--00";
    
    @Override
    public Command constructCommand(String strCommand) {
        if (strCommand == null) {
            return null;
        }
        String commandParams = null;
        Command command = null;
        if (strCommand.startsWith(CommandLogin.COMMAND)) {
            command = new CommandLoginString();
            commandParams = strCommand.substring(CommandLogin.COMMAND.length());
        } else if (strCommand.startsWith(CommandRegister.COMMAND)) {
            command = new CommandRegisterString();
            commandParams = strCommand.substring(CommandRegister.COMMAND.length());
        } else if (strCommand.startsWith(CommandAuthString.COMMAND)) {
            command = new CommandAuthString();
            commandParams = strCommand.substring(CommandAuth.COMMAND.length());
        } else if (strCommand.startsWith(CommandMessage.COMMAND)) {
            command = new CommandMessageString();
            commandParams = strCommand.substring(CommandMessage.COMMAND.length());
        } else {
            return null;
        }
        command.fromNetwork(commandParams);
        return command;
    }

    @Override
    public CommandMessage contructMessageCommand(TYPE type, String message) {
        return new CommandMessageString();
    }

    @Override
    public Command createCommandInstance(String commandName) {
        Command command = null;
        if (commandName.equals(CommandLogin.COMMAND)) {
            command = new CommandLoginString();
        } else if (commandName.equals(CommandRegister.COMMAND)) {
            command = new CommandRegisterString();
        } else if (commandName.equals(CommandAuthString.COMMAND)) {
            command = new CommandAuthString();
        } else if (commandName.equals(CommandMessage.COMMAND)) {
            command = new CommandMessageString();
        }
        return command;
    }
    
    

}
