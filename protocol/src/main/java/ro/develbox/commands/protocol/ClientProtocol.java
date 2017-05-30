package ro.develbox.commands.protocol;

import ro.develbox.commands.ICommandContructor;

public class ClientProtocol extends Protocol {

    public ClientProtocol(IProtocolResponse responder, ICommandSender sender, ICommandContructor commandConstr) {
        super(responder, sender, commandConstr, false);
    }

}
