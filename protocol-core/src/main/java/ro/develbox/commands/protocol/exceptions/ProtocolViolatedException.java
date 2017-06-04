package ro.develbox.commands.protocol.exceptions;

import ro.develbox.commands.Command;

public abstract class ProtocolViolatedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String cause;
    protected Command command;
    protected Command prevCommand;

    public ProtocolViolatedException(String cause, Command command, Command prevCommand) {
        this.cause = cause;
        this.command = command;
        this.prevCommand = prevCommand;
    }

    @Override
    public String getMessage() {
        if (command == null) {
            return cause;
        }
        String commandDesc = describeCommand(command);
        String curretnCommandMessage = "Comand : " + command.getCommand() + "violates protocol. Received "
                + " with annotation :" + commandDesc;
        String secondPartMessage = "\n\t\t" + "Previously command";

        if (prevCommand != null) {
            secondPartMessage = prevCommand.getCommand() + " with annotation " + describeCommand(prevCommand);
        } else {
            secondPartMessage = secondPartMessage + " NULL";
        }
        return cause + " \n\t" + curretnCommandMessage + secondPartMessage;
    }

    protected abstract String describeCommand(Command command);

}
