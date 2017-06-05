package ro.develbox.protocol;

import ro.develbox.commands.ICommandContructor;

@SuppressWarnings("rawtypes")
public abstract class NetworkProtocol extends Protocol implements INetworkCommandReceiver{

    protected NetworkProtocol(IProtocolResponse responder, ICommandSender sender, ICommandContructor commandConstructor,
            Class commandAnnotation) {
        super(responder, sender, commandConstructor, commandAnnotation);
    }

}
