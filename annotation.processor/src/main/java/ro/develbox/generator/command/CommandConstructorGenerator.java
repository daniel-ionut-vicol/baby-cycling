package ro.develbox.generator.command;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.ClassName;
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
	
	Set<TypeElement> elements ;

	public CommandConstructorGenerator(Set<TypeElement> elements) {
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
		
		classBuilder.addMethod(contructMessageCommandMethod());
		
        classBuilder.addMethod(constructCommandMethod());
        
        classBuilder.addMethod(createCommandInstanceMethod());
        
		String packageName = "ro.develbox.commands.string";
	    JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();
		JavaFileObject jfo = filer.createSourceFile(packageName + "." + fileName);
		Writer writer = jfo.openWriter();
		javaFile.writeTo(writer);
		writer.flush();
		writer.close();
	}
	
	private MethodSpec contructMessageCommandMethod(){
		MethodSpec.Builder contructMessageCommand = MethodSpec.methodBuilder("contructMessageCommand");
		contructMessageCommand.addAnnotation(Override.class);
		contructMessageCommand.addModifiers(Modifier.PUBLIC);
		contructMessageCommand.addParameter(TYPE.class, "type");
		contructMessageCommand.addParameter(String.class, "message");
		contructMessageCommand.returns(CommandMessage.class);
		contructMessageCommand.addStatement("return new CommandMessageString(type,message)");
		return contructMessageCommand.build();
	}
	
	private MethodSpec constructCommandMethod(){
		String paramName = "strCommand";
		String returnName = "command";
		MethodSpec.Builder constructCommand = MethodSpec.methodBuilder("constructCommand");
		constructCommand.addAnnotation(Override.class);
		constructCommand.addModifiers(Modifier.PUBLIC);
		constructCommand.addParameter(String.class,paramName);
		constructCommand.returns(Command.class);
        
		constructCommand.beginControlFlow("if ($L == null)",paramName).addStatement("return null").endControlFlow();
		constructCommand.addStatement("$T $L = null",Command.class,returnName);
		constructCommand.addStatement("String commandParams = null");
				
		constructCommand.beginControlFlow("if ($L.startsWith(CommandMessage.COMMAND))",paramName)
				.addStatement("$L = new CommandMessageString()",returnName)
				.addStatement("commandParams = strCommand.substring(CommandMessage.COMMAND.length())")
				;
		for (TypeElement element : elements){
			ClassName cName = ClassName.get(element);
			String elementName = element.getSimpleName().toString();
			constructCommand.nextControlFlow("else if ($L.startsWith($T.COMMAND))",paramName,cName)
				.addStatement("$L = new "+elementName+"string()",returnName)
				.addStatement("commandParams = strCommand.substring($T.COMMAND.length())",cName);
		}
		constructCommand.nextControlFlow("else");
		constructCommand.addStatement("return null");
		
		constructCommand.endControlFlow();
		
        constructCommand.addStatement("$L.fromNetwork(commandParams)",returnName);
        constructCommand.addStatement("return $L",returnName);
        return constructCommand.build();
	}
	
	private MethodSpec createCommandInstanceMethod(){
		MethodSpec.Builder createCommandInstance = MethodSpec.methodBuilder("createCommandInstance");
		createCommandInstance.addAnnotation(Override.class);
		createCommandInstance.addModifiers(Modifier.PUBLIC);
		createCommandInstance.addParameter(String.class,"commandName");
		createCommandInstance.returns(Command.class);
        
		createCommandInstance.addStatement("Command command = null");
				
		createCommandInstance.beginControlFlow("if (commandName.equals(CommandMessage.COMMAND))")
				.addStatement("command = new CommandMessageString()")
				;
		for (TypeElement element : elements){
			String elementName = element.getSimpleName().toString();
			createCommandInstance.nextControlFlow("else if (commandName.equals("+elementName+".COMMAND))")
				.addStatement("command = new "+elementName+"string()");
		}
		createCommandInstance.endControlFlow();
		
        createCommandInstance.addStatement("return command");
        return createCommandInstance.build();
	}
}