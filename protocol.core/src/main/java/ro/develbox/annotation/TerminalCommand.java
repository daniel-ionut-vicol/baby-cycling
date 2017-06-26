package ro.develbox.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a command as terminal ( after this comand is received, protocol resets)
 * @author vdi
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TerminalCommand {
    
}
