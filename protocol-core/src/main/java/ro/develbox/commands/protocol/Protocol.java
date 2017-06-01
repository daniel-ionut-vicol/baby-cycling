package ro.develbox.commands.protocol;

import ro.develbox.annotation.CommandInfo;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.commands.protocol.exceptions.ProtocolViolatedException;

/**
 * implementing the protocol based on command annotations
 * 
 * @author danielv
 *
 */
public abstract class Protocol {

    protected IProtocolResponse responder;
    protected ICommandSender sender;

    protected Command lastCommand;

    /**
     * if should use server protocol or client one, true for server
     */
    private boolean server;

    protected Protocol(IProtocolResponse responder, ICommandSender sender, boolean server) {
        this.responder = responder;
        this.sender = sender;
        this.server = server;
    }

    public Command getLastCommand() {
        return lastCommand;
    }

    public boolean isServer() {
        return server;
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
            throw new ProtocolViolatedException("Null command", receivedCommand, lastCommand, server);
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
            throw new ProtocolViolatedException("Command invalid", receivedCommand, lastCommand, server);
        }
        if (respCommand != null && validateResponse(respCommand)) {
            sender.sendCommand(respCommand);
        }
        return respCommand;
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
            // accept any received
            return true;
        } else {
            CommandInfo ann = (CommandInfo)receivedCommand.getClass().getAnnotation(CommandInfo.class);
            if (ann != null) {
                result = (ann.server() && server) || (ann.client() && !server);
                if (result) {
                    result = checkCurentCommandAgainstLast(receivedCommand.getClass());
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private boolean checkCurentCommandAgainstLast(Class receivedClass) {
        if (lastCommand == null) {
            return true;
        }
        CommandInfo last = (CommandInfo)lastCommand.getClass().getAnnotation(CommandInfo.class);
        Class[] accepted = last.nextCommandType();
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
        CommandInfo last = (CommandInfo)lastCommand.getClass().getAnnotation(CommandInfo.class);
        Class[] accepted = last.responseCommandType();
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

}
