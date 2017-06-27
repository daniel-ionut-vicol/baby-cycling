package ro.develbox.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a command as start ( protocol accepts only this type of comamands after a reset )
 * @author vdi
 *
 */
@Retention(RetentionPolicy.RUNTIME)
//@Inherited
public @interface StartCommand {
    
}
