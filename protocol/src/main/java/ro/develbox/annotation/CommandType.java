package ro.develbox.annotation;

/**
 * Annotation for commands  
 * @author danielv
 *
 */
public @interface CommandType {
    /**
     * if command should be received by the server
     */
    boolean server() default false;

    /**
     * if command should be received by the client
     */
    boolean client() default false;

    /**
     * next command accepted 
     */
    Class[] nextCommandType() default {};

    /**
     * expected response type
     */
    Class[] responseCommandType() default {};
}
