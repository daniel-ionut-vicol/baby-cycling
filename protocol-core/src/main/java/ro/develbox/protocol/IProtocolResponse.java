package ro.develbox.protocol;

import ro.develbox.commands.Command;

public interface IProtocolResponse {

    public Command getCommandResponse(Command command);

}
