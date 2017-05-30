package ro.develbox.commands.protocol;

import ro.develbox.annotation.CommandType;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;

public abstract class Protocol {

    protected static CommandMessage unexpectedCommand = new CommandMessage(TYPE.ERROR, "Unexpected command");
    protected static CommandMessage wrongCommand = new CommandMessage(TYPE.ERROR, "Wrong command");

    protected IProtocolResponse responder;
    protected ICommandSender sender;

    protected Command lastCommand;

    private boolean server;

    protected Protocol(IProtocolResponse responder, ICommandSender sender, boolean server) {
        this.responder = responder;
        this.sender = sender;
        this.server = server;
    }

    public Command commandReceived(Command receivedCommand) throws WarnCommandException, ErrorCommandException {
        Command respCommand = null;
        if (receivedCommand == null) {
            respCommand = wrongCommand;
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
            respCommand = unexpectedCommand;
        }
        if (respCommand != null) {
            sender.sendCommand(respCommand);
        }
        return respCommand;
    }

    protected boolean validateCommandType(Command receivedCommand) {
        boolean result = true;
        if (receivedCommand == null) {
            result = false;
        } else {
            CommandType ann = (CommandType)receivedCommand.getClass().getAnnotation(CommandType.class);
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
        CommandType last = (CommandType)lastCommand.getClass().getAnnotation(CommandType.class);
        Class[] accepted = last.nextCommandType();
        // we do not accept any response type when node is specified
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
        if (respCommand instanceof CommandMessage) {
            return true;
        }
        CommandType last = (CommandType)lastCommand.getClass().getAnnotation(CommandType.class);
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
