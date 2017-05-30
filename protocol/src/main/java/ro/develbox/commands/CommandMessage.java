package ro.develbox.commands;

import ro.develbox.annotation.CommandType;

@CommandType(client = true, server = true)
public class CommandMessage extends Command {

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

    private String getStringType() {
        return type.name();
    }

    private void setType(String type) {
        this.type = TYPE.valueOf(type);
    }

    @Override
    public void getParametersFromNetwork(String stringParameters) {
        String[] params = stringParameters.split(",");
        setType(params[0]);
        setMessage(params[1]);
    }

    @Override
    protected String toNetworkParameters() {
        return getStringType() + "," + getMessage();
    }

}
