package ro.develbox.generator.command;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.commands.CommandMessage.TYPE;

public class CommandConstructorGenerator {
	private static final String fileName= "CommandConstructorString";
	
	List<TypeElement> elements ;

	public CommandConstructorGenerator(List<TypeElement> elements) {
		super();
		this.elements = elements;
	}
	
	public void generate(Elements elementUtils,Filer filer) throws IOException{
		TypeSpec.Builder classBuilder= TypeSpec.classBuilder(fileName);
		classBuilder.addSuperinterface(ICommandContructor.class);
		
		FieldSpec.Builder staticSepField = FieldSpec.builder(String.class, "PARAMSEP");
		staticSepField.addModifiers(Modifier.STATIC,Modifier.PUBLIC,Modifier.FINAL);
		staticSepField.initializer("\"0012300\"");
		
		classBuilder.addField(staticSepField.build());
		
		MethodSpec.Builder contructMessageCommand = MethodSpec.methodBuilder("contructMessageCommand");
		contructMessageCommand.addAnnotation(Override.class);
		contructMessageCommand.addModifiers(Modifier.PUBLIC);
		contructMessageCommand.addParameter(TYPE.class, "type");
		contructMessageCommand.addParameter(String.class, "message");
		contructMessageCommand.returns(CommandMessage.class);
		contructMessageCommand.addStatement("return new CommandMessageString(type,message)");
		
		classBuilder.addMethod(contructMessageCommand.build());
		
		MethodSpec.Builder constructCommand = MethodSpec.methodBuilder("constructCommand");
		constructCommand.addAnnotation(Override.class);
		constructCommand.addModifiers(Modifier.PUBLIC);
		constructCommand.addParameter(String.class,"strCommand");
		constructCommand.returns(Command.class);
        
		constructCommand.beginControlFlow("if (strCommand == null)").addStatement("return null").endControlFlow();
		constructCommand.addStatement("Command command = null");
		constructCommand.addStatement("String commandParams = null");
				
		constructCommand.beginControlFlow("if (strCommand.startsWith(CommandMessage.COMMAND))")
				.addStatement("command = new CommandMessageString()")
				.addStatement("commandParams = strCommand.substring(CommandMessage.COMMAND.length())")
				;
		for (TypeElement element : elements){
			String elementName = element.getSimpleName().toString();
			constructCommand.nextControlFlow("else if (strCommand.startsWith("+elementName+".COMMAND))")
				.addStatement("command = new "+elementName+"string()")
				.addStatement("commandParams = strCommand.substring("+elementName+".COMMAND.length())");
		}
		constructCommand.nextControlFlow("else");
		constructCommand.addStatement("return null");
		
		constructCommand.endControlFlow();
		
        constructCommand.addStatement("command.fromNetwork(commandParams)");
        constructCommand.addStatement("return command");
        classBuilder.addMethod(constructCommand.build());
        
		String packageName = "ro.develbox.commands.string";
	    JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();
		JavaFileObject jfo = filer.createSourceFile(packageName + "." + fileName);
		Writer writer = jfo.openWriter();
		javaFile.writeTo(writer);
		writer.flush();
		writer.close();
	}
}
