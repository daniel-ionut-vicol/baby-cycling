package ro.develbox.commands.protocol.exceptions;

import java.util.Arrays;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.Command;

public class ServerProtocolViolatedException extends ProtocolViolatedException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String cause;
    protected Command command;
    protected Command prevCommand;

    public ServerProtocolViolatedException(String cause, Command command, Command prevCommand) {
        super(cause, command, prevCommand);
    }

    @Override
    protected String describeCommand(Command command) {
        ServerCommand annotation = command.getClass().getAnnotation(ServerCommand.class);
        String annotationStr = null;
        if (annotation != null) {
            ClientCommand cc = (ClientCommand)annotation;
            annotationStr = " next command : " + Arrays.toString(cc.nextCommandType()) + ",response :"
                    + Arrays.toString(cc.responseCommandType());
        } else {
            annotationStr = " NO ANNOTATION ";
        }
        return annotationStr;
    }

}
