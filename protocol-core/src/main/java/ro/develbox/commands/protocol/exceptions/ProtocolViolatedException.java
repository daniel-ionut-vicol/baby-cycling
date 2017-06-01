package ro.develbox.commands.protocol.exceptions;

import java.util.Arrays;

import ro.develbox.annotation.CommandInfo;
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
        CommandInfo annotation = (CommandInfo)command.getClass().getAnnotation(CommandInfo.class);

        String commandAnnotation = stringAnnotation(annotation);
        String curretnCommandMessage = "Comand : " + command.getCommand() + " violates protocol. Received on "
                + (server ? "server" : "client") + " with annotation :" + commandAnnotation;
        String secondPartMessage = "\n\t\t" + "Previously command";

        if (prevCommand != null) {
            annotation = (CommandInfo)prevCommand.getClass().getAnnotation(CommandInfo.class);
            secondPartMessage = prevCommand.getCommand() + " with annotation " + stringAnnotation(annotation);
        } else {
            secondPartMessage = secondPartMessage + " NULL";
        }
        return cause + " \n\t" + curretnCommandMessage + secondPartMessage;
    }

    private static String stringAnnotation(CommandInfo annotation) {
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
