package ro.develbox.protocol;

import ro.develbox.protocol.ICommandReceiver;

public interface INetworkCommandReceiver extends ICommandReceiver{

    public void errorReceived(Throwable e);

    public void connected();

    public void disconnected(String reason);

}
