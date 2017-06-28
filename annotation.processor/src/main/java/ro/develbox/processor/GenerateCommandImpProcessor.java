package ro.develbox.processor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.generator.command.CommandConstructorGenerator;
import ro.develbox.generator.command.CommandImplGenerator;

public class GenerateCommandImpProcessor extends CommandsProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
	    try {
            List<TypeElement> serverElements = getCommandElements(env, ServerCommand.class);
            List<TypeElement> clientElements = getCommandElements(env, ClientCommand.class);
            Set<TypeElement> setElements = new LinkedHashSet<>();
            setElements.addAll(serverElements);
            setElements.addAll(clientElements);
            
            for (TypeElement element : setElements){
                CommandImplGenerator generator = new CommandImplGenerator(element,"string");
                generator.generateCode(elementUtils,filer);
            }
            CommandConstructorGenerator ccg = new CommandConstructorGenerator(setElements);
            ccg.generate(elementUtils, filer);
        } catch (Exception e) {
            return true;
        }
	    
		return false;
	}

}
