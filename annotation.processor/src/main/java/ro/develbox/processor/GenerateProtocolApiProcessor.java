package ro.develbox.processor;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.generator.api.ProtocolApiGenerator;

public class GenerateProtocolApiProcessor extends CommandsProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
		try {
			generateApiFile(env, ServerCommand.class, "ro.develbox.protocol.client", "ClientProtocolApiWrapper",
					"ro.develbox.protocol.client.ClientProtocol");
			generateApiFile(env, ClientCommand.class, "ro.develbox.protocol.server", "ServerProtocolApiWrapper",
					"ro.develbox.protocol.client.ServerProtocol");
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	public boolean generateApiFile(RoundEnvironment env, Class<? extends Annotation> annotationClass,
			String packageName, String generatedClass, String wrappedClass) throws Exception {
		ProtocolApiGenerator serverClasses = new ProtocolApiGenerator();
		List<TypeElement> elements = getCommandElements(env, annotationClass);
		serverClasses.addClasses(elements);
		boolean found = !elements.isEmpty();
		if (found) {
			try {
				serverClasses.generateCode(elementUtils, filer, packageName, generatedClass, wrappedClass);
			} catch (Exception e) {
				e.printStackTrace(System.out);
				error(null, "Exception : " + e.getMessage());
			}
		}
		return false;
	}
}
