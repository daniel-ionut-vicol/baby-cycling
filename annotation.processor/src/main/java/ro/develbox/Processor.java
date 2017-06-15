package ro.develbox;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;

public class Processor extends AbstractProcessor {

	private Types typeUtils;
	private Elements elementUtils;
	private Filer filer;
	private Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		super.init(env);
		typeUtils = env.getTypeUtils();
		elementUtils = env.getElementUtils();
		filer = env.getFiler();
		messager = env.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
		generateApiFile(env, ServerCommand.class, "ro.develbox.protocol.client", "ClientProtocolApiWrapper",
				"ro.develbox.protocol.client.ClientProtocol");
		generateApiFile(env, ClientCommand.class, "ro.develbox.protocol.server", "ServerProtocolApiWrapper",
				"ro.develbox.protocol.client.ServerProtocol");
		return false;
	}

	public boolean generateApiFile(RoundEnvironment env, Class<? extends Annotation> annotationClass,
			String packageName, String generatedClass, String wrappedClass) {
		ProtocolApiGenerator serverClasses = new ProtocolApiGenerator();
		boolean found = false;
		// Itearate over all @ServerCommand annotated elements
		for (Element annotatedElement : env.getElementsAnnotatedWith(annotationClass)) {
			found = true;
			// Check if a class has been annotated with @ServerCommand
			if (annotatedElement.getKind() != ElementKind.CLASS) {
				error(annotatedElement, "Only classes can be annotated with @%s", ServerCommand.class.getSimpleName());
				return true; // Exit processing
			}

			// We can cast it, because we know that it of ElementKind.CLASS
			TypeElement typeElement = (TypeElement) annotatedElement;
			serverClasses.addClass(typeElement);
			if (!isValidClass(typeElement)) {
				return true; // Error message printed, exit processing
			}
		}
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

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotataions = new LinkedHashSet<String>();
		annotataions.add(ServerCommand.class.getCanonicalName());
		annotataions.add(ClientCommand.class.getCanonicalName());
		return annotataions;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	private boolean isValidClass(TypeElement classElement) {

		if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
			error(classElement, "The class %s is not public.", classElement.getQualifiedName().toString());
			return false;
		}

		// Check if it's an abstract class
		if (!classElement.getModifiers().contains(Modifier.ABSTRACT)) {
			error(classElement, "The class %s is not abstract. You can't annotate non abstract classes with @%",
					classElement.getQualifiedName().toString(), ServerCommand.class.getSimpleName());
			return false;
		}

		// Check if an empty public constructor is given
		for (Element enclosed : classElement.getEnclosedElements()) {
			if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
				ExecutableElement constructorElement = (ExecutableElement) enclosed;
				if (constructorElement.getParameters().size() == 0
						&& constructorElement.getModifiers().contains(Modifier.PUBLIC)) {
					// Found an empty constructor
					return true;
				}
			}
		}

		// No empty constructor found
		error(classElement, "The class %s must provide an public empty default constructor",
				classElement.getQualifiedName().toString());
		return false;
	}

	private void error(Element e, String msg, Object... args) {
		if (e != null) {
			messager.printMessage(Kind.ERROR, String.format(msg, args), e);
		} else {
			messager.printMessage(Kind.ERROR, String.format(msg, args));
		}
	}
}
