package ro.develbox.processor.stringImpl;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.generator.api.ProtocolApiGenerator;
import ro.develbox.generator.api.RespondersGenerator;
import ro.develbox.processor.CommandsProcessor;

public class GenerateRespondersSuperClass extends CommandsProcessor {

	private static volatile boolean processedSources = false;
	
	@Override
	public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
		if(processedSources){
			return false;
		}
		processedSources = true;
		try {
			generateResponderClass(env, ClientCommand.class, "ro.develbox.protocol.client", "ClientResponderApi");
			generateResponderClass(env, ServerCommand.class, "ro.develbox.protocol.server", "ServerResponderApi");
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	public boolean generateResponderClass(RoundEnvironment env, Class<? extends Annotation> annotationClass,
			String packageName, String generatedClass) throws Exception {
		RespondersGenerator responder = new RespondersGenerator();
		List<TypeElement> elements = getCommandElements(env, annotationClass);
		responder.addClasses(elements);
		boolean found = !elements.isEmpty();
		if (found) {
			try {
				responder.generateCode(elementUtils, filer, packageName, generatedClass);
			} catch (Exception e) {
				e.printStackTrace(System.out);
				error(null, "Exception : " + e.getMessage());
			}
		}
		return false;
	}
}
