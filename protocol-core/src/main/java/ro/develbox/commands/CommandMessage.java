package ro.develbox.commands;

import ro.develbox.annotation.CommandInfo;

@CommandInfo(client = true, server = true)
public abstract class CommandMessage extends Command {

    public static final String COMMAND = "message:";

    public static enum TYPE {
        OK, WARN, ERROR
    };

    private String message;
    private TYPE type;

    public CommandMessage() {
        super(COMMAND);
    }

    public CommandMessage(TYPE type, String message) {
        super(COMMAND);
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    protected String getStringType() {
        return type.name();
    }

    protected void setType(String type) {
        this.type = TYPE.valueOf(type);
    }

}
