package ro.develbox.commands.exceptions;

import ro.develbox.commands.Command;

public abstract class CommandException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Command command;

    public CommandException(Command command) {
        super(command.toNetwork());
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

}
