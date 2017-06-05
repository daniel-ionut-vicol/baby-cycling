package ro.develbox.protocol;

import ro.develbox.annotation.StartCommand;
import ro.develbox.annotation.TerminalCommand;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.CommandReset;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

/**
 * implementing the protocol based on command annotations
 * 
 * @author danielv
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class Protocol implements ICommandReceiver{

    protected IProtocolResponse responder;
    protected ICommandSender sender;

    protected Command lastCommand;

    /**
     * Expected command annotation
     */
    protected Class commandAnnotation;

    protected Protocol(IProtocolResponse responder, ICommandSender sender, Class commandAnnotation) {
        this.responder = responder;
        this.sender = sender;
        this.commandAnnotation = commandAnnotation;
    }

    public Command getLastCommand() {
        return lastCommand;
    }

    /**
     * Check if command is valid , created the response and sends it on sender
     * (if any response should be sent)
     * 
     * @param receivedCommand
     *            received command
     * @return response command based on received one
     * @throws WarnCommandException
     * @throws ErrorCommandException
     */
    public Command commandReceived(Command receivedCommand)
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Command respCommand = null;
        if (receivedCommand == null) {
            throw getProtocolViolatedException("Null command", receivedCommand, lastCommand);
        } else if (receivedCommand instanceof CommandReset) {
            reset();
        } else if (receivedCommand instanceof CommandMessage) {
            TYPE type = ((CommandMessage)receivedCommand).getType();
            if (TYPE.OK.equals(type)) {
                // Do nothing, we just found out that we behaved correctly
            } else if (TYPE.WARN.equals(type)) {
                throw new WarnCommandException(receivedCommand);
            } else if (TYPE.ERROR.equals(type)) {
                throw new ErrorCommandException(receivedCommand);
            }
        } else if (validateCommandType(receivedCommand)) {
            respCommand = responder.getCommandResponse(receivedCommand);
            if (setNextExpectedType(respCommand)) {
                lastCommand = receivedCommand;
            }
        } else {
            // instead of responding with wrong command, throw exception
            throw getProtocolViolatedException("Command invalid", receivedCommand, lastCommand);
        }
        if (respCommand != null && validateResponse(respCommand)) {
            sender.sendCommand(respCommand);
            //reset protocol if we sent a terminal
            if(retrieveAnnotation(respCommand, TerminalCommand.class)!=null){
                reset();
            }
        }
        //reset protocol if we reached a terminal
        if(retrieveAnnotation(receivedCommand, TerminalCommand.class)!=null){
            reset();
        }
        return respCommand;
    }

    private void reset() {
        reset(null);
    }

    protected abstract Class[] getAcceptedCommands();

    protected abstract Class[] getAcceptedResponses();
    
    protected abstract ProtocolViolatedException getProtocolViolatedException(String cause, Command command, Command prevCommand);

    /**
     * reset protocol and send reset command to the sender
     * 
     * @param reset
     *            - reset command that will be sent to the other end
     */
    public void reset(CommandReset reset) {
        lastCommand = null;
        if (reset != null) {
            sender.sendCommand(reset);
        }

    }

    /**
     * A command is valid when it has the expected type based on the last
     * command sent and the command annotations
     * 
     * @param receivedCommand
     * @return if command is valid
     */
    protected boolean validateCommandType(Command receivedCommand) {
        boolean result = true;
        if (receivedCommand == null) {
            result = false;
        }
        if (receivedCommand instanceof CommandMessage) {
            // accept any received message command
            // TODO WHY TO ALWWAYS ACCEPT THEM ???
            return true;
        } else {
            if (lastCommand == null) {
                // accept just StartCommand when this is the fist command
                StartCommand ann = (StartCommand)retrieveAnnotation(receivedCommand, StartCommand.class);
                if (ann != null) {
                    result = true;
                } else {
                    result = false;
                }
            }
            Object ann = retrieveAnnotation(receivedCommand, commandAnnotation);
            if (ann != null) {
                if (result) {
                    result = result && checkCurentCommandAgainstLast(receivedCommand.getClass());
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    private boolean checkCurentCommandAgainstLast(Class receivedClass) {
        if (lastCommand == null) {
            return true;
        }
        Class[] accepted = getAcceptedCommands();
        // we do not accept any response type when none is specified
        if (accepted == null || accepted.length == 0) {
            // TODO maybe we should report this, since the protocol seems to be
            // badly written
            return false;
        }
        for (Class clazz : accepted) {
            if (receivedClass.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    protected boolean setNextExpectedType(Command respCommand) {
        if (respCommand instanceof CommandMessage) {
            TYPE type = ((CommandMessage)respCommand).getType();
            if (TYPE.OK.equals(type)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    protected boolean validateResponse(Command respCommand) {
        // any message response is considered valid
        if (respCommand instanceof CommandMessage) {
            return true;
        }
        Class[] accepted = getAcceptedResponses();
        Class responseClass = respCommand.getClass();
        if (accepted == null || accepted.length == 0) {
            return false;
        }
        for (Class clazz : accepted) {
            if (responseClass.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    private Object retrieveAnnotation(Object object, Class annotationClass) {
        Object ann = object.getClass().getAnnotation(annotationClass);
        return ann;
    }

}
