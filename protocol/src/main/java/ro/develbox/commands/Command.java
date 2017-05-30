package ro.develbox.commands;

/**
 * Super class of all protocol commands 
 * @author danielv
 *
 */
public abstract class Command {

    /**
     * Command name
     */
    private String command;

    public Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    /**
     * Method called before sending this command on network
     * @return representation of this command that will be shared on network
     */
    public abstract String toNetwork();

    /**
     * Method called after message is received from network
     * @param stringParameters - the network representation of the command
     */
    abstract public void fromNetwork(String networkRep);

}
