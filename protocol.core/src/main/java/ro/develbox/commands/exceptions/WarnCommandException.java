package ro.develbox.commands.exceptions;

import ro.develbox.commands.Command;

public class WarnCommandException extends CommandException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public WarnCommandException(Command command) {
        super(command);
    }

}
