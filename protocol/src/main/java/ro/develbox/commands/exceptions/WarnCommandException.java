package ro.develbox.commands.exceptions;

import ro.develbox.commands.Command;

public class WarnCommandException extends CommandException {

    public WarnCommandException(Command command) {
        super(command);
    }

}
