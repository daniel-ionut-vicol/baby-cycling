package ro.develbox.commands.exceptions;

import ro.develbox.commands.Command;

public class ErrorCommandException extends CommandException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ErrorCommandException(Command command) {
        super(command);
    }

}
