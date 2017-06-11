package ro.develbox.protocol;

public interface INetworkProtocol {

    public void connect() throws Exception;

    public void disconnect() throws Exception;

}
