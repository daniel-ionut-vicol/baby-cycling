package ro.develbox.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for commands  
 * @author danielv
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    /**
     * if command should be received by the server
     */
    boolean server() default false;

    /**
     * if command should be received by the client
     */
    boolean client() default false;

    /**
     * if command is the start of a command chain
     */
    boolean start() default false;
    
    /**
     * if command is the end of a command chain
     */
    boolean terminal() default false;
    
    /**
     * next command accepted 
     */
    Class[] nextCommandType() default {};

    /**
     * expected response type
     */
    Class[] responseCommandType() default {};
}
