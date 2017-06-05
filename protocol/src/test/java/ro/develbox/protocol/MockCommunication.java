package ro.develbox.protocol;

import ro.develbox.commands.Command;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

/**
 * redirect received messages to designated protocol impl
 * @author vdi
 *
 */
public class MockCommunication implements ICommandSender {

    private NetworkProtocol protocol ;

    public MockCommunication(NetworkProtocol protocol) {
        super();
        this.protocol = protocol;
    }

    @Override
    public void sendCommand(Command command) {
        try {
            protocol.commandReceived(command);
        } catch (WarnCommandException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ErrorCommandException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolViolatedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
}
