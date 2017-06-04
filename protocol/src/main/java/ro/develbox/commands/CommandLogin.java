package ro.develbox.commands;

import ro.develbox.annotation.ServerCommand;

@ServerCommand
public abstract class CommandLogin extends Command {

    public static final String COMMAND = "login:";

    private String email;
    
    private String password;

    public CommandLogin() {
        super(COMMAND);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    

}
