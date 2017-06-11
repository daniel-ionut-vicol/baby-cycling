package ro.develbox.protocol;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mockit.Expectations;
import mockit.Mocked;
import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.TestAnnotation;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.CommandReset;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.commands.NonStartCommand;
import ro.develbox.commands.StartTestCommand;
import ro.develbox.commands.TerminalTestCommand;
import ro.develbox.commands.TestTypeCommand;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;
import ro.develbox.protocol.exceptions.ProtocolViolatedTestException;

@SuppressWarnings({ "rawtypes" })
public class ProtocolTest {

    IProtocolResponse responder;

    Command responderCommand;

    @BeforeClass
    public void setup() {

        responderCommand = new TestTypeCommand();

        responder = new IProtocolResponse() {

            @Override
            public Command getCommandResponse(Command command) {

                return responderCommand;
            }

			@Override
			public void setCommandConstr(ICommandContructor commandConstr) {
				// TODO Auto-generated method stub
				
			}
        };
    }

    @Test
    public void testProtocolConstructor() {
        Protocol protocol = new Protocol(responder, null, ClientCommand.class) {

            @Override
            protected Class[] getAcceptedCommands() {
                return null;
            }

            @Override
            protected Class[] getAcceptedResponses() {
                return null;
            }

            @Override
            protected ProtocolViolatedException getProtocolViolatedException(String cause, Command command,
                    Command prevCommand) {
                return null;
            }
        };
        assertTrue(protocol.responder == responder);
        assertTrue(protocol.commandAnnotation == ClientCommand.class);
    }

    @Test(expectedExceptions = {
            ProtocolViolatedException.class }, expectedExceptionsMessageRegExp = ".*Null command.*")
    public void testNullCommandRejected() throws Exception {
        Protocol protocol = createProtocol();
        protocol.validateAndRespond(null);
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
            Command resp = protocol.validateAndRespond(message);
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

    @Test
    public void testResetReceived(@Mocked final CommandReset reset)
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Protocol protocol = createProtocol();
        protocol.validateAndRespond(reset);
        assertNull(protocol.lastCommand);
    }

    @Test
    public void testCommandWithStartAccepted()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Protocol protocol = createProtocol();
        protocol.validateAndRespond(new StartTestCommand());
    }

    @Test(expectedExceptions = ProtocolViolatedException.class)
    public void testCommandWithoutStartRejected()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Protocol protocol = createProtocol();
        protocol.validateAndRespond(new NonStartCommand());
    }

    @Test
    public void testTerminalReceivedCommandReset()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Protocol protocol = createProtocol();
        protocol.validateAndRespond(new TerminalTestCommand());
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
        protocol.validateAndRespond(received);
        assertNull(protocol.lastCommand);
    }

    @Test(expectedExceptions = ProtocolViolatedException.class)
    public void testValidationAgainsLastCommandNullAccepted()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Protocol protocol = createProtocol();
        protocol.lastCommand = new TestTypeCommand();
        protocol.validateAndRespond(new TestTypeCommand());
    }

    @Test(expectedExceptions = ProtocolViolatedException.class)
    public void testValidationAgainsLastCommandNotAccepted()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Class[] accepted = { ProtocolTest.class };
        Protocol protocol = createProtocol(accepted);
        protocol.lastCommand = new TestTypeCommand();
        protocol.validateAndRespond(new TestTypeCommand());
    }

    @Test
    public void testValidationAgainsLastCommandAccepted()
            throws WarnCommandException, ErrorCommandException, ProtocolViolatedException {
        Class[] accepted = { TestTypeCommand.class };
        Protocol protocol = createProtocol(accepted);
        protocol.lastCommand = new TestTypeCommand();
        protocol.validateAndRespond(new TestTypeCommand());
    }

    private Protocol createProtocol() {
        return createProtocol(responder, null, null);
    }

    private Protocol createProtocol(Class[] accReqs) {
        return createProtocol(responder, accReqs, null);
    }

    private Protocol createProtocol(IProtocolResponse responder, Class[] accResp) {
        return createProtocol(responder, null, accResp);
    }

    private Protocol createProtocol(IProtocolResponse responder, final Class[] accReqs,
            final Class[] accResp) {
        Protocol protocol = new Protocol(responder, null, TestAnnotation.class) {

            @Override
            protected Class[] getAcceptedCommands() {
                return accReqs;
            }

            @Override
            protected Class[] getAcceptedResponses() {
                return accResp;
            }

            protected ProtocolViolatedException getProtocolViolatedException(String cause, Command command,
                    Command prevCommand) {
                return new ProtocolViolatedTestException(cause, command, prevCommand);
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
