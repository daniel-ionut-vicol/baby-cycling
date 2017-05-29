package ro.develbox.annotation;

public @interface CommandType {
	boolean server() default false;
	boolean client() default false;
	Class[] nextCommandType() default {};
	Class[] responseCommandType() default {};
}
