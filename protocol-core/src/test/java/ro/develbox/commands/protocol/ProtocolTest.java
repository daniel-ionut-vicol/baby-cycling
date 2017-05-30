package ro.develbox.commands.protocol;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mockit.Deencapsulation;
import ro.develbox.annotation.CommandType;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.commands.protocol.exceptions.ProtocolViolatedException;
import ro.develbox.commands.CommandMessage.TYPE;

public class ProtocolTest {

    IProtocolResponse responder;
    ICommandSender sender;
    ICommandContructor commandConstr;

    @BeforeClass
    public void setup() {
        responder = new IProtocolResponse() {

            @Override
            public Command getCommandResponse(Command command) {

                return null;
            }
        };
        sender = new ICommandSender() {

            @Override
            public void sendCommand(Command command) {
                // Do nothing
            }
        };

        commandConstr = new ICommandContructor() {

            @Override
            public CommandMessage contructMessageCommand(TYPE type, String message) {
                return new CommandMessage() {
                    @Override
                    public String toNetwork() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public void fromNetwork(String networkRep) {
                        // TODO Auto-generated method stub

                    }
                };
            }

            @Override
            public Command constructCommand(String strCommand) {
                return null;
            }
        };
    }

    @Test
    public void testServerProtocolConstructor() {
        Protocol serverP = new ServerProtocol(responder, sender, commandConstr);
        Assert.assertTrue(serverP.isServer());
    }

    @Test
    public void testClientProtocolConstructor() {
        Protocol clientP = new ClientProtocol(responder, sender, commandConstr);
        Assert.assertFalse(clientP.isServer());
    }

    @Test(expectedExceptions = {
            ProtocolViolatedException.class }, expectedExceptionsMessageRegExp = ".*Command invalid.*")
    public void testClientCommandRejectedOnServer() throws Exception {
        ServerProtocol serverP = new ServerProtocol(responder, sender, commandConstr);
        serverP.commandReceived(new ClientTypeTestCommand());
    }

    @Test(expectedExceptions = {
            ProtocolViolatedException.class }, expectedExceptionsMessageRegExp = ".*Command invalid.*")
    public void testServerCommandRejectedOnClient() throws Exception {
        ClientProtocol clientP = new ClientProtocol(responder, sender, commandConstr);
        clientP.commandReceived(new ServerTypeTestCommand());
    }
    
    @Test(expectedExceptions = {
            ProtocolViolatedException.class }, expectedExceptionsMessageRegExp = ".*Null command.*")
    public void testNullCommandRejected() throws Exception {
        ClientProtocol clientP = new ClientProtocol(responder, sender, commandConstr);
        clientP.commandReceived(null);
    }

}
