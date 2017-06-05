package ro.develbox.protocol;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandAuth;
import ro.develbox.commands.CommandMessage.TYPE;
import ro.develbox.commands.ICommandContructor;

public class ProtocolResponseAuthWrapper implements IProtocolResponse{

    protected IProtocolResponse wrapped;
    protected ICommandContructor commandConstrutor;
    private boolean authenticated;

    public ProtocolResponseAuthWrapper(IProtocolResponse wrapped,ICommandContructor commandConstrutor) {
        this.wrapped = wrapped;
        this.commandConstrutor = commandConstrutor;
        authenticated = false;
    }

    @Override
    public Command getCommandResponse(Command command) {
        if(command instanceof CommandAuth){
            authenticated = authenticate((CommandAuth)command);
            if(authenticated){
                return commandConstrutor.contructMessageCommand(TYPE.OK, "Authenticated");
            }else{
                return commandConstrutor.contructMessageCommand(TYPE.ERROR, "CHEATER!!!");
            }
        }
        if(!authenticated){
            throw new RuntimeException("Not authenticated");
        }
        return wrapped.getCommandResponse(command);
    }

    protected boolean authenticate(CommandAuth auth){
        //do some logic here
        return true;
    }
    
}
