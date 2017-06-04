package ro.develbox.commands.protocol.exceptions;

import java.util.Arrays;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.commands.Command;

public class ClientProtocolViolatedException extends ProtocolViolatedException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String cause;
    protected Command command;
    protected Command prevCommand;

    public ClientProtocolViolatedException(String cause, Command command, Command prevCommand) {
        super(cause, command, prevCommand);
    }

    @Override
    protected String describeCommand(Command command) {
        ClientCommand annotation = command.getClass().getAnnotation(ClientCommand.class);
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
