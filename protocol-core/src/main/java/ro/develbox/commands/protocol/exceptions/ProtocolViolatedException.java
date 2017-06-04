package ro.develbox.commands.protocol.exceptions;

import java.util.Arrays;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.Command;

@SuppressWarnings({"serial","unchecked","rawtypes"})
public class ProtocolViolatedException extends Exception {

    private String cause;
    private Command command;
    private Command prevCommand;
    private Class commandExpAnotation;

    public ProtocolViolatedException(String cause,Command command, Command prevCommand,Class commandExpAnotation) {
        this.cause = cause;
        this.command = command;
        this.prevCommand = prevCommand;
        this.commandExpAnotation = commandExpAnotation;
    }

    @Override
    public String getMessage() {
        if (command == null) {
            return cause;
        }
        Object annotation = command.getClass().getAnnotation(commandExpAnotation);

        String commandAnnotation = stringAnnotation(annotation);
        String curretnCommandMessage = "Comand : " + command.getCommand() + " violates protocol. Received "
                + " with annotation :" + commandAnnotation;
        String secondPartMessage = "\n\t\t" + "Previously command";

        if (prevCommand != null) {
            annotation = prevCommand.getClass().getAnnotation(commandExpAnotation);
            secondPartMessage = prevCommand.getCommand() + " with annotation " + stringAnnotation(annotation);
        } else {
            secondPartMessage = secondPartMessage + " NULL";
        }
        return cause + " \n\t" + curretnCommandMessage + secondPartMessage;
    }

    private static String stringAnnotation(Object annotation) {
        String annotationStr = null;
        if (annotation != null) {
            if(annotation instanceof ServerCommand){
                ServerCommand sc = (ServerCommand) annotation;
                annotationStr = " next command : "
                    + Arrays.toString(sc.nextCommandType()) + ",response :"
                    + Arrays.toString(sc.responseCommandType());
            }else if(annotation instanceof ServerCommand){
                ClientCommand cc = (ClientCommand) annotation;
                annotationStr = " next command : "
                    + Arrays.toString(cc.nextCommandType()) + ",response :"
                    + Arrays.toString(cc.responseCommandType());
            }
        } else {
            annotationStr = " NO ANNOTATION ";
        }
        return annotationStr;
    }
    
}
