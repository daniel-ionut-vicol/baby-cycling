package ro.develbox.commands.protocol;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.internal.expectations.TestOnlyPhase;
import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.CommandReset;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.commands.protocol.exceptions.ProtocolViolatedException;

@SuppressWarnings({ "rawtypes" })
public class ProtocolTest {

    IProtocolResponse responder;
    ICommandSender sender;

    Command responderCommand;

    @BeforeClass
    public void setup() {

        responderCommand = new TestTypeCommand();

        responder = new IProtocolResponse() {

            @Override
            public Command getCommandResponse(Command command) {

                return responderCommand;
            }
        };
        sender = new ICommandSender() {

            @Override
            public void sendCommand(Command command) {
                // Do nothing
            }
        };
    }

    @Test
    public void testProtocolConstructor() {
        Protocol protocol = new Protocol(responder, sender, ClientCommand.class) {

            @Override
            protected Class[] getAcceptedCommands() {
                return null;
            }

            @Override
            protected Class[] getAcceptedResponses() {
                return null;
            }
        };
        assertTrue(protocol.responder == responder);
        assertTrue(protocol.sender == sender);
        assertTrue(protocol.commandAnnotation == ClientCommand.class);
    }

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

    @Test(expectedExceptions = {
            ProtocolViolatedException.class }, expectedExceptionsMessageRegExp = ".*Null command.*")
    public void testNullCommandRejected() throws Exception {
        Protocol protocol = createProtocol();
        protocol.commandReceived(null);
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

    @DataProvider(name = "testMessageCommandsData")
    public Object[][] testMessageCommandsData() {
        return new Object[][] { { TYPE.OK, null }, { TYPE.WARN, WarnCommandException.class },
                { TYPE.ERROR, ErrorCommandException.class }, };
    }

    @Test(dataProvider = "testMessageCommandsData")
    public void testMessageCommands(TYPE type, Class expectedException) {
        Protocol protocol = createProtocol();
        Command message = createMessage(type);
        try {
            Command resp = protocol.commandReceived(message);
            if (expectedException == null) {
                assertNull(resp);
            } else {
                fail("Message with type : " + type + " did not cause exception");
            }
        } catch (Exception e) {
            assertEquals(e.getClass(), expectedException);
        }
    }

    @DataProvider(name = "testNextMessageResponseData")
    public Object[][] testNextMessageResponseData() {
        return new Object[][] { { TYPE.OK, true }, { TYPE.WARN, false }, { TYPE.ERROR, false } };
    }

    @Test(dataProvider = "testNextMessageResponseData")
    public void testSetNextOnMessageResponse(TYPE type, boolean result) {
        Protocol protocol = createProtocol();
        CommandMessage resp = createMessage(type);
        assertTrue(protocol.setNextExpectedType(resp) == result);
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

    @Test
    public void testReset(@Mocked final CommandReset reset, @Mocked final ICommandSender sender) {
        Protocol protocol = createProtocol(sender);
        protocol.reset(reset);

        assertNull(protocol.lastCommand);
        new Verifications() {
            {
                sender.sendCommand(reset);
                times = 1;
            }
        };
    }

    @Test
    public void testCommandWithStartAccepted()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Protocol protocol = createProtocol();
        protocol.commandReceived(new StartTestCommand());
    }

    @Test(expectedExceptions = ProtocolViolatedException.class)
    public void testCommandWithoutStartRejected()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Protocol protocol = createProtocol();
        protocol.commandReceived(new NonStartCommand());
    }

    @Test
    public void testTerminalReceivedCommandReset()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Protocol protocol = createProtocol();
        protocol.commandReceived(new TerminalTestCommand());
        assertNull(protocol.lastCommand);
    }

    @Test
    public void testTerminalSentCommandreset(@Mocked final IProtocolResponse responder)
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        final Command received = new TestTypeCommand();
        final Command response = new TerminalTestCommand();
        new Expectations() {
            {
                responder.getCommandResponse(received);
                result = response;
            }
        };
        Class[] accresp = { TerminalTestCommand.class };
        Protocol protocol = createProtocol(responder, accresp);
        protocol.commandReceived(received);
        assertNull(protocol.lastCommand);
    }

    private Protocol createProtocol() {
        return createProtocol(sender);
    }

    private Protocol createProtocol(ICommandSender sender) {
        return createProtocol(responder, sender, null);
    }

    private Protocol createProtocol(IProtocolResponse responder, Class[] accResp) {
        return createProtocol(responder, sender, accResp);
    }

    private Protocol createProtocol(IProtocolResponse responder, ICommandSender sender, final Class[] accResp) {
        Protocol protocol = new Protocol(responder, sender, TestAnnotation.class) {

            @Override
            protected Class[] getAcceptedCommands() {
                return null;
            }

            @Override
            protected Class[] getAcceptedResponses() {
                return accResp;
            }
        };
        return protocol;
    }

    public CommandMessage createMessage(final TYPE type) {
        return new CommandMessage(type, "") {

            @Override
            public String toNetwork() {
                return null;
            }

            @Override
            public void fromNetwork(String networkRep) {
            }
        };
    }

}
