package ro.develbox.protocol;

import ro.develbox.commands.Command;
import ro.develbox.commands.ICommandContructor;

public interface IProtocolResponse {

    public Command getCommandResponse(Command command);

    public void setCommandConstr(ICommandContructor commandConstr);
}
