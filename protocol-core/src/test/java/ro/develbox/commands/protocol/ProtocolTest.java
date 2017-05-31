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
import mockit.Verifications;
import mockit.internal.expectations.TestOnlyPhase;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.commands.protocol.exceptions.ProtocolViolatedException;

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
    public void testProtocolConstructor(){
        Protocol protocol = new Protocol(responder, sender,true) {
        };
        assertTrue(protocol.responder==responder);
        assertTrue(protocol.sender==sender);
        assertTrue(protocol.isServer());
    }
    
    @Test
    public void testServerProtocolConstructor() {
        Protocol serverP = new ServerProtocol(responder, sender);
        Assert.assertTrue(serverP.isServer());
    }

    @Test
    public void testClientProtocolConstructor() {
        Protocol clientP = new ClientProtocol(responder, sender);
        Assert.assertFalse(clientP.isServer());
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
        Protocol protocol = new Protocol(responder, sender, true) {
        };
        protocol.commandReceived(null);
    }

    @Test
    public void testNextExpectedCommandGoodValidation()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Protocol protocol = new Protocol(responder, sender, true) {
        };
        TestTypeCommand command = new TestTypeCommand();
        protocol.lastCommand = command;
        assertTrue(protocol.validateCommandType(command));
        assertFalse(protocol.validateCommandType(new ServerTypeTestCommand()));
    }

    @Test
    public void testResponseCommandValidation() {
        Protocol protocol = new Protocol(responder, sender, true) {
        };
        TestTypeCommand command = new TestTypeCommand();
        protocol.lastCommand = command;
        assertTrue(protocol.validateResponse(command));
        assertFalse(protocol.validateResponse(new ServerTypeTestCommand()));
    }

    @DataProvider(name = "testMessageCommandsData")
    public Object[][] testMessageCommandsData() {
        return new Object[][] { { TYPE.OK, null }, { TYPE.WARN, WarnCommandException.class },
                { TYPE.ERROR, ErrorCommandException.class }, };
    }

    @Test(dataProvider = "testMessageCommandsData")
    public void testMessageCommands(TYPE type, Class expectedException) {
        Protocol protocol = new Protocol(responder, sender, true) {
        };
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

    @Test
    public void testCommandReceived() throws WarnCommandException, ErrorCommandException, ProtocolViolatedException{
        
        new Expectations(sender) {{
            sender.sendCommand(responderCommand);times=1;
        }};
        
        Protocol protocol = new Protocol(responder, sender, true) {
        };
        Command received = new TestTypeCommand();
        Command response = protocol.commandReceived(received);
        assertTrue(response == responderCommand);
        assertTrue(protocol.lastCommand==received);
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
