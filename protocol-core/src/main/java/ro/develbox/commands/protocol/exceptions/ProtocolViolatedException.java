package ro.develbox.commands.protocol.exceptions;

import java.util.Arrays;

import ro.develbox.annotation.CommandType;
import ro.develbox.commands.Command;

public class ProtocolViolatedException extends Exception {

    private String cause;
    private Command command;
    private Command prevCommand;
    private boolean server;

    public ProtocolViolatedException(String cause,Command command, Command prevCommand, boolean server) {
        this.cause = cause;
        this.command = command;
        this.prevCommand = prevCommand;
        this.server = server;
    }

    @Override
    public String getMessage() {
        if (command == null) {
            return cause;
        }
        CommandType annotation = (CommandType)command.getClass().getAnnotation(CommandType.class);

        String commandAnnotation = stringAnnotation(annotation);
        String curretnCommandMessage = "Comand : " + command.getCommand() + " violates protocol. Received on "
                + (server ? "server" : "client") + " with annotation :" + commandAnnotation;
        String secondPartMessage = "\n\t\t" + "Previously command";

        if (prevCommand != null) {
            annotation = (CommandType)prevCommand.getClass().getAnnotation(CommandType.class);
            secondPartMessage = prevCommand.getCommand() + " with annotation " + stringAnnotation(annotation);
        } else {
            secondPartMessage = secondPartMessage + " NULL";
        }
        return cause + " \n\t" + curretnCommandMessage + secondPartMessage;
    }

    private static String stringAnnotation(CommandType annotation) {
        String annotationStr = null;
        if (annotation != null) {
            annotationStr = "server:" + annotation.server() + ",client:" + annotation.client() + ",next command : "
                    + Arrays.toString(annotation.nextCommandType()) + ",response :"
                    + Arrays.toString(annotation.responseCommandType());
        } else {
            annotationStr = " NO ANNOTATION ";
        }
        return annotationStr;
    }
}
