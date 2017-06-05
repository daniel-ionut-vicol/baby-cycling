package ro.develbox.protocol;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mockit.Expectations;
import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.ClientTypeTestCommand;
import ro.develbox.commands.Command;
import ro.develbox.commands.ServerTypeTestCommand;
import ro.develbox.commands.TestTypeCommand;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.client.ClientProtocol;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;
import ro.develbox.protocol.server.ServerProtocol;

public class ProtocolImplTest extends ProtocolTest{

    @Test
    public void testServerProtocolConstructor() {
        Protocol serverP = new ServerProtocol(responder, sender);
        Assert.assertTrue(serverP.commandAnnotation == ServerCommand.class);
    }

    @Test
    public void testClientProtocolConstructor() {
        Protocol clientP = new ClientProtocol(responder, sender);
        Assert.assertTrue(clientP.commandAnnotation == ClientCommand.class);
    }

    @Test(expectedExceptions = {
            ProtocolViolatedException.class }, expectedExceptionsMessageRegExp = ".*Command invalid.*")
    public void testClientCommandRejectedOnServer() throws Exception {
        ServerProtocol serverP = new ServerProtocol(responder, sender);
        serverP.commandReceived(new ClientTypeTestCommand());
    }
    
    @Test(expectedExceptions = {
            ProtocolViolatedException.class }, expectedExceptionsMessageRegExp = ".*Command invalid.*")
    public void testServerCommandRejectedOnClient() throws Exception {
        ClientProtocol clientP = new ClientProtocol(responder, sender);
        clientP.commandReceived(new ServerTypeTestCommand());
    }

    @DataProvider(name = "comandValidationDp")
    public Object[][] comandValidationDp() {
        return new Object[][] { { new ServerProtocol(responder, sender), new ClientTypeTestCommand() },
                { new ClientProtocol(responder, sender), new ServerTypeTestCommand() } };
    }

    @Test(dataProvider = "comandValidationDp")
    public void testNextExpectedCommandGoodValidation(Protocol protocol, Command badCommand)
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        TestTypeCommand command = new TestTypeCommand();
        protocol.lastCommand = command;
        assertTrue(protocol.validateCommandType(command));
        assertFalse(protocol.validateCommandType(badCommand));
    }
    
    @Test(dataProvider = "comandValidationDp")
    public void testResponseCommandValidation(Protocol protocol, Command badCommand) {
        TestTypeCommand command = new TestTypeCommand();
        protocol.lastCommand = command;
        assertTrue(protocol.validateResponse(command));
        assertFalse(protocol.validateResponse(badCommand));
    }


    @DataProvider(name = "protocolImpls")
    public Object[][] protocolImpls() {
        return new Object[][] { { new ServerProtocol(responder, sender) }, { new ClientProtocol(responder, sender) } };
    }

    @Test(dataProvider = "protocolImpls")
    public void testCommandReceived(Protocol protocol)
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {

        new Expectations(sender) {
            {
                sender.sendCommand(responderCommand);
                times = 1;
            }
        };
        Command received = new TestTypeCommand();
        Command response = protocol.commandReceived(received);
        assertTrue(response == responderCommand);
        assertTrue(protocol.lastCommand == received);
    }
    
}
