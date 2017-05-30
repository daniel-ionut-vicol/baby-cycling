package ro.develbox.commands.exceptions;

import ro.develbox.commands.Command;

public class ErrorCommandException extends CommandException {

    public ErrorCommandException(Command command) {
        super(command);
    }

}
