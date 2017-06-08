package ro.develbox.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("rawtypes")
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientCommand {
    
    /**
     * next command accepted 
     */
    Class[] nextCommandType() default {};

    /**
     * expected response type
     */
    Class[] responseCommandType() default {};
    
}
