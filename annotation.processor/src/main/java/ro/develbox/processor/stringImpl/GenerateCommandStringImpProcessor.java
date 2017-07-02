package ro.develbox.processor.stringImpl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import ro.develbox.annotation.ClientCommand;
import ro.develbox.annotation.ServerCommand;
import ro.develbox.generator.command.CommandConstructorGenerator;
import ro.develbox.generator.command.CommandImplGenerator;
import ro.develbox.processor.CommandsProcessor;

public class GenerateCommandStringImpProcessor extends CommandsProcessor {
	
	private static volatile boolean processedSources = false;
	
	@Override
	public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
		if(processedSources){
			return false;
		}
		processedSources = true;
		try {
            List<TypeElement> serverElements = getCommandElements(env, ServerCommand.class);
            List<TypeElement> clientElements = getCommandElements(env, ClientCommand.class);
            Set<TypeElement> setElements = new LinkedHashSet<>();
            setElements.addAll(serverElements);
            setElements.addAll(clientElements);
            
            for (TypeElement element : setElements){
                // do not generate implementation for other implementations
            	if(element.getModifiers().contains(Modifier.ABSTRACT)){
	            	CommandImplGenerator generator = new CommandImplGenerator(element,StringImpConstants.PACKAGE,StringImpConstants.IMPL_NAME);
	                generator.generateCode(elementUtils,filer);
                }
            }
            //TODO this generator must be moved to run in second sun after those classes are generated
            CommandConstructorGenerator ccg = new CommandConstructorGenerator(setElements);
            ccg.generate(elementUtils, filer);
        } catch (Exception e) {
            error(null, "Error :" + e.getMessage());
        	return true;
        }
	    
		return false;
	}

}
