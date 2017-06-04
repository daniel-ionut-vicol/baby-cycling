package ro.develbox.protocol.exceptions;

import ro.develbox.annotation.StartCommand;
import ro.develbox.annotation.TerminalCommand;
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
        String curretnCommandMessage = "Comand : " + command.getCommand() + "violates protocol. Received : "
                + commandDescription(command);
        String secondPartMessage = "\n\t\t" + "Previously command";

        if (prevCommand != null) {
            secondPartMessage = commandDescription(prevCommand);
        } else {
            secondPartMessage = secondPartMessage + " NULL";
        }
        return cause + " \n\t" + curretnCommandMessage + secondPartMessage;
    }

    private String commandDescription(Command command){
        return command.getCommand() + " annotation :" + describeCommand(command) + "(start : "+isStartCommand(command)+", terminal : "+isTerminalCommand(command)+")";
    }
    
    protected abstract String describeCommand(Command command);
    
    private boolean isStartCommand(Command command){
        return command.getClass().getAnnotation(StartCommand.class)!=null;
    }

    private boolean isTerminalCommand(Command command){
        return command.getClass().getAnnotation(TerminalCommand.class)!=null;
    }
}
