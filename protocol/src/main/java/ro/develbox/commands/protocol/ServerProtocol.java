package ro.develbox.commands.protocol;

import ro.develbox.commands.ICommandContructor;

public class ServerProtocol extends Protocol {

    public ServerProtocol(IProtocolResponse responder, ICommandSender sender, ICommandContructor commandConstr) {
        super(responder, sender, commandConstr ,true);
    }
}
