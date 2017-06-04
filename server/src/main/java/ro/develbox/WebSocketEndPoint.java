package ro.develbox;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandAuth;
import ro.develbox.commands.CommandLogin;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.CommandRegister;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.commands.string.CommandConstructorString;
import ro.develbox.model.User;
import ro.develbox.protocol.ICommandSender;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.ServerProtocol;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

@ServerEndpoint(value = "/cyclingWSE")
public class WebSocketEndPoint implements ICommandSender, IProtocolResponse {

    private ServerProtocol serverProtocol;
    private Session session;
    private boolean authed;
    private User user;

    private ICommandContructor commandConstr = new CommandConstructorString();
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Open");
        this.session = session;
        serverProtocol = new ServerProtocol(this, this);
        authed = false;
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close");
        this.session = null;
        serverProtocol = null;
        authed = false;
    }

    @OnMessage
    public void onMessage(String message, Session userSession) {
        System.out.println("MessageReceived : " + message);
        Command command = commandConstr.constructCommand(message);
        try {
            serverProtocol.commandReceived(command);
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

    @OnError
    public void onError(Session session, Throwable e) {
        System.out.println("ERROR:");
        e.printStackTrace(System.out);
    }

    @Override
    public void sendCommand(Command command) {
        if (session != null) {
            try {
                session.getBasicRemote().sendText(command.toNetwork());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Command getCommandResponse(Command command) {
        Command ret = null;
        if (!authed) {
            System.out.println("NOT AUTHAED");
            if (!(command instanceof CommandAuth)) {
                ret = commandConstr.contructMessageCommand(TYPE.ERROR, "NOT AUTHED");
                System.out.println("INCORECTR TYPE");
            } else {
                // TODO AUTH
                authed = true;
            }
        } else {
            if (command instanceof CommandRegister) {
                // TODO register user
                User user = new User();
                CommandRegister registerCm = (CommandRegister)command;
                user.setEmail(registerCm.getEmail());
                user.setName(registerCm.getNickName());
                user.setRegid(registerCm.getRegistrationId());
                // TODO persist user
                this.user = user;
                ret = commandConstr.contructMessageCommand(TYPE.OK, "USER REGISTERED");
            } else if (command instanceof CommandLogin) {
                // TODO login user
                // TODO check user
                CommandLogin loginCm = (CommandLogin)command;
                User user = new User();
                user.setEmail(loginCm.getEmail());
                ret = commandConstr.contructMessageCommand(TYPE.OK, "USER LOGGED IN");
            }
        }

        return ret;
    }

}
