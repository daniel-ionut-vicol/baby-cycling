package ro.develbox;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;

public class GenerateCommandImpProcessor extends CommandsProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
//		// Itearate over all @ServerCommand annotated elements
//				for (Element annotatedElement : env.getElementsAnnotatedWith(annotationClass)) {
//					found = true;
//					// Check if a class has been annotated with @ServerCommand
//					if (annotatedElement.getKind() != ElementKind.CLASS) {
//						error(annotatedElement, "Only classes can be annotated with @%s", ServerCommand.class.getSimpleName());
//						return true; // Exit processing
//					}
//
//					// We can cast it, because we know that it of ElementKind.CLASS
//					TypeElement typeElement = (TypeElement) annotatedElement;
//					serverClasses.addClass(typeElement);
//					if (!isValidClass(typeElement)) {
//						return true; // Error message printed, exit processing
//					}
//				}
		return false;
	}

}
