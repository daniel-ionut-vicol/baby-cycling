package ro.develbox.generator.api;

import java.io.Writer;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import ro.develbox.commands.Command;
import ro.develbox.commands.CommandMessage;
import ro.develbox.commands.ICommandContructor;
import ro.develbox.protocol.IProtocolResponse;

public class RespondersGenerator {
	
	private static final String commandConstrFiled = "commandConstr"; 
	
	private List<TypeElement> elements;

	public RespondersGenerator() {
		elements = new ArrayList<>();
	}

	public void addClass(TypeElement element) {
		elements.add(element);
	}
	
	public void addClasses(List<TypeElement> elements){
		this.elements.addAll(elements);
	}

	public void clear() {
		elements.clear();
	}

	public void generateCode(Elements elementUtils, Filer filer, String packageName, String generatedClass) throws Exception {

		// class fields
		TypeSpec.Builder responderApi = TypeSpec.classBuilder(generatedClass).addModifiers(Modifier.PUBLIC);
		responderApi.addModifiers(Modifier.ABSTRACT);
		responderApi.addSuperinterface(IProtocolResponse.class);
		
		responderApi.addFields(getFields());

		responderApi.addMethod(setCommandConstrMethod());
		responderApi.addMethod(createCommandResponse());
		responderApi.addMethod(getCommandResponseMethod());
		responderApi.addMethods(getHandlersMethods());

		JavaFile javaFile = JavaFile.builder(packageName, responderApi.build()).build();
		JavaFileObject jfo = filer.createSourceFile(packageName + "." + generatedClass);
		Writer writer = jfo.openWriter();
		javaFile.writeTo(writer);
		writer.flush();
		writer.close();
	}
	
	private List<MethodSpec> getHandlersMethods(){
		List<MethodSpec> handlers = new ArrayList<>();
		for (TypeElement element:elements){
			if(shouldExcludeElement(element)){
				continue;
			}
			handlers.add(getHandlerMethod(element));
		}
		return handlers;
	}
	
	private MethodSpec getHandlerMethod(TypeElement element){
		MethodSpec.Builder handleMethod = MethodSpec.methodBuilder(getHandlerMethodName(element));
		handleMethod.addModifiers(Modifier.ABSTRACT,Modifier.PROTECTED);
		handleMethod.addParameter(TypeName.get(element.asType()),"command");
		handleMethod.returns(Command.class);
		return handleMethod.build();
	}
	
	private MethodSpec getCommandResponseMethod(){
		MethodSpec.Builder getCommandResponse = MethodSpec.methodBuilder("getCommandResponse");
		getCommandResponse.addAnnotation(Override.class);
		getCommandResponse.addModifiers(Modifier.PUBLIC);
		getCommandResponse.addModifiers(Modifier.FINAL);
		getCommandResponse.returns(Command.class);
		String param = "command";
		getCommandResponse.addParameter(Command.class,param);
		for(TypeElement element : elements){
			if(shouldExcludeElement(element)){
				continue;
			}
			getCommandResponse.beginControlFlow("if($L instanceof $T)",param,element);
			getCommandResponse.addStatement("return "+getHandlerMethodName(element)+"(($T)$L)",element,param);
			getCommandResponse.endControlFlow();
		}
		getCommandResponse.addStatement("throw new RuntimeException(\"Unknown command type\")");
		return getCommandResponse.build();
	}
	
	private MethodSpec setCommandConstrMethod(){
		MethodSpec.Builder setCommandConstr = MethodSpec.methodBuilder("setCommandConstr");
		setCommandConstr.addModifiers(Modifier.PUBLIC);
		setCommandConstr.addParameter(ICommandContructor.class,"commandConstr");
		setCommandConstr.addStatement("this."+commandConstrFiled+"=commandConstr");
		return setCommandConstr.build();
	}
	
	private MethodSpec createCommandResponse(){
		MethodSpec.Builder createCommandResponse = MethodSpec.methodBuilder("createCommand");
		createCommandResponse.addModifiers(Modifier.PUBLIC);
		createCommandResponse.addParameter(String.class, "type");
		createCommandResponse.addStatement("return "+commandConstrFiled+".createCommandInstance(type)");
		createCommandResponse.returns(Command.class);
		return createCommandResponse.build();
	}
	
	private List<FieldSpec> getFields(){
		List<FieldSpec> fields = new ArrayList<>();
		FieldSpec.Builder commandConstr = FieldSpec.builder(ICommandContructor.class, commandConstrFiled);
		commandConstr.addModifiers(Modifier.PRIVATE);
		fields.add(commandConstr.build());
		return fields;
	}
	
	private boolean shouldExcludeElement(TypeElement element){
		//TODO exclude CommandMessage subtypes
		return element.getSimpleName().toString().contains("Message");
	}
	
	private static String getHandlerMethodName(TypeElement element){
		return "handle"+element.getSimpleName().toString().replace("Command", "");
	}
	
}
