package ro.develbox.commands;

import ro.develbox.annotation.CommandType;

/**
 * Command sent to authenticate the client ( validate that client is using this client)
 * @author danielv
 *
 */
@CommandType(server = true, nextCommandType = {})
public abstract class CommandAuth extends Command {

    public static final String COMMAND = "auth:";

    /**
     * Client key
     */
    private String key = "asdasdasd";

    public CommandAuth() {
        super(COMMAND);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
