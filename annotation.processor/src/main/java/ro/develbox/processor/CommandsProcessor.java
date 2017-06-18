package ro.develbox.processor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
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

public abstract class CommandsProcessor extends AbstractProcessor {

	protected Types typeUtils;
	protected Elements elementUtils;
	protected Filer filer;
	protected Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		super.init(env);
		typeUtils = env.getTypeUtils();
		elementUtils = env.getElementUtils();
		filer = env.getFiler();
		messager = env.getMessager();
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

	protected List<TypeElement> getCommandElements(RoundEnvironment env, Class<? extends Annotation> annotationClass) throws Exception{
		List<TypeElement> elements = new ArrayList<>();
		// Itearate over all @ServerCommand annotated elements
		for (Element annotatedElement : env.getElementsAnnotatedWith(annotationClass)) {
			// Check if a class has been annotated with @ServerCommand
			if (annotatedElement.getKind() != ElementKind.CLASS) {
				error(annotatedElement, "Only classes can be annotated with @%s", ServerCommand.class.getSimpleName());
				throw new Exception(); // Exit processing
			}

			// We can cast it, because we know that it of ElementKind.CLASS
			TypeElement typeElement = (TypeElement) annotatedElement;
			if (!isValidClass(typeElement)) {
				throw new Exception(); // Error message printed, exit processing
			}
			elements.add(typeElement);
		}
		return elements;
	}

	protected boolean isValidClass(TypeElement classElement) {

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

		Element commandClass = elementUtils.getTypeElement("ro.develbox.commands.Command");

		// check if it extends Command
		if (!classElement.getSuperclass().equals(commandClass.asType())) {
			error(classElement, "The class %s does not extends ro.develbox.commands.Command",
					classElement.getQualifiedName().toString());
			return false;
		}
		classElement.getSuperclass();
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

	protected void error(Element e, String msg, Object... args) {
		if (e != null) {
			messager.printMessage(Kind.ERROR, String.format(msg, args), e);
		} else {
			messager.printMessage(Kind.ERROR, String.format(msg, args));
		}
	}
}
