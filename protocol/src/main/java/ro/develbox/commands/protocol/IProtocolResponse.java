package ro.develbox.commands.protocol;

import ro.develbox.commands.Command;

public interface IProtocolResponse {

    public Command getCommandResponse(Command command);

}
