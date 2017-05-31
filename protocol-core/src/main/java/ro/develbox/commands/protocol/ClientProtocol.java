package ro.develbox.commands.protocol;

public class ClientProtocol extends Protocol {

    public ClientProtocol(IProtocolResponse responder, ICommandSender sender) {
        super(responder, sender, false);
    }

}
