package ro.develbox.protocol;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mockit.Expectations;
import mockit.MockUp;
import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.ClientTypeTestCommand;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.MockAuth;
import ro.develbox.commands.ServerTypeTestCommand;
import ro.develbox.commands.TestTypeCommand;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.client.ClientProtocol;
import ro.develbox.protocol.client.ClientProtocolApi;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;
import ro.develbox.protocol.server.ServerProtocol;
import ro.develbox.protocol.server.ServerProtocolApi;

public class ProtocolImplTest extends ProtocolTest {
	
	private ICommunicationChannel channel;
	
	@BeforeClass
	public void setup(){
		super.setup();
		channel = new MockUp<ICommunicationChannel>(){}.getMockInstance();
	}
	
    @Test
    public void testServerProtocolConstructor() {
        Protocol serverP = new ServerProtocolApi(responder, channel){};
        Assert.assertTrue(serverP.commandAnnotation == ServerCommand.class);
    }

    @Test
    public void testClientProtocolConstructor() {
        Protocol clientP = new ClientProtocolApi(responder, channel){};
        Assert.assertTrue(clientP.commandAnnotation == ClientCommand.class);
    }

    @Test(expectedExceptions = {
            ProtocolViolatedException.class }, expectedExceptionsMessageRegExp = ".*Command invalid.*")
    public void testClientCommandRejectedOnServer() throws Exception {
        ServerProtocol serverP = new ServerProtocolApi(responder, channel){};
        serverP.validateAndRespond(new ClientTypeTestCommand());
    }

    @Test(expectedExceptions = {
            ProtocolViolatedException.class }, expectedExceptionsMessageRegExp = ".*Command invalid.*")
    public void testServerCommandRejectedOnClient() throws Exception {
        ClientProtocol clientP = new ClientProtocolApi(responder, channel){};
        clientP.validateAndRespond(new ServerTypeTestCommand());
    }

    @DataProvider(name = "comandValidationDp")
    public Object[][] comandValidationDp() {
        return new Object[][] { { new ServerProtocolApi(responder, channel){}, new ClientTypeTestCommand() },
                { new ClientProtocolApi(responder, channel){}, new ServerTypeTestCommand() } };
    }

    @Test(dataProvider = "comandValidationDp")
    public void testNextExpectedCommandGoodValidation(NetworkProtocol protocol, Command badCommand)
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        TestTypeCommand command = new TestTypeCommand();
        protocol.lastCommand = command;
        assertTrue(protocol.validateCommandType(command));
        assertFalse(protocol.validateCommandType(badCommand));
    }

    @Test(dataProvider = "comandValidationDp")
    public void testResponseCommandValidation(NetworkProtocol protocol, Command badCommand) {
        TestTypeCommand command = new TestTypeCommand();
        protocol.lastCommand = command;
        assertTrue(protocol.validateResponse(command));
        assertFalse(protocol.validateResponse(badCommand));
    }

    @DataProvider(name = "protocolImpls")
    public Object[][] protocolImpls() {
        return new Object[][] { { new ServerProtocolApi(responder, channel){} }, { new ClientProtocolApi(responder, channel){} } };
    }

    @Test(dataProvider = "protocolImpls", expectedExceptions = {
            RuntimeException.class }, expectedExceptionsMessageRegExp = "Not authenticated")
    public void testCommandRejectedWhenNoAuthReceived(NetworkProtocol protocol)
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Command received = new TestTypeCommand();
        Command response = protocol.validateAndRespond(received);
        assertTrue(response == responderCommand);
        assertTrue(protocol.lastCommand == received);
    }

    @Test(dataProvider = "protocolImpls")
    public void testCommandResponseSentAfterAutentication(NetworkProtocol protocol)
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException, IOException {
        new Expectations() {
            {
                channel.sendCommand((CommandMessage)any);
                times = 1;
            }
        };
        protocol.validateAndRespond(new MockAuth().getMockInstance());
    }

    @Test(dataProvider = "protocolImpls")
    public void testCommandAcceptedAfterAuthReceived(NetworkProtocol protocol)
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException, IOException {

        new Expectations() {
            {
                channel.sendCommand(responderCommand);
                times = 1;
            }
        };
        Command received = new TestTypeCommand();
        protocol.validateAndRespond(new MockAuth().getMockInstance());
        Command response = protocol.validateAndRespond(received);
        assertTrue(response == responderCommand);
        assertTrue(protocol.lastCommand == received);
    }

}
