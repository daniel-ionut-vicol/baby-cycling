package ro.develbox.protocol;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import mockit.Mocked;
import mockit.Verifications;
import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.ICommandContructor;

public class NetworkProtocolTest {

	@Mocked
	IProtocolResponse responder;
	@Mocked
	ICommunicationChannel commChannel;
	@Mocked
	ICommandContructor commandConstrutor;

	@Test
	public void initializationTest() throws Exception {
		NetworkProtocolImplMock npm = new NetworkProtocolImplMock(responder, commChannel, commandConstrutor, null);
		assertFalse(npm.connected);
		assertFalse(npm.sequenceStarted);
	}

	@Test
	public void connectTest() throws Exception {
		NetworkProtocolImplMock npm = new NetworkProtocolImplMock(responder, commChannel, commandConstrutor, null);
		npm.connect();
		assertTrue(npm.afterConnectCalled);
		assertTrue(npm.connected);
		new Verifications() {
			{
				commChannel.connect();
				times = 1;
			}
		};
	}

	@Test
	public void disconnectTest() throws Exception {
		NetworkProtocolImplMock npm = new NetworkProtocolImplMock(responder, commChannel, commandConstrutor, null);
		npm.disconnect();
		assertFalse(npm.connected);
		new Verifications() {
			{
				commChannel.disconnect();
				times = 1;
			}
		};
	}

	@Test
	public void createCommandTest() throws Exception {
		NetworkProtocolImplMock npm = new NetworkProtocolImplMock(responder, commChannel, commandConstrutor, null);
		npm.createCommand(CommandMessage.COMMAND);
		new Verifications() {
			{
				commandConstrutor.createCommandInstance(CommandMessage.COMMAND);
				times = 1;

			}
		};
	}

	public Command getTestCommand() {
		return new Command("test") {
			@Override
			public String toNetwork() {
				return "";
			}

			@Override
			public void fromNetwork(String networkRep) {
			}
		};
	}

}
