package ro.develbox.generator.command;

import java.io.Writer;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import ro.develbox.Utils;

public class CommandImplGenerator {

	TypeElement superElement;
	
	String implName;
	
	public CommandImplGenerator(TypeElement superElement,String implName) {
		super();
		this.superElement = superElement;
		this.implName = implName;
	}
	
	public void generateCode(Elements elementUtils,Filer filer) throws Exception{
		String elementName = superElement.getSimpleName().toString();
		String newName = elementName+implName;
	    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(newName);
	    classBuilder.addModifiers(Modifier.PUBLIC);
	    classBuilder.superclass(TypeName.get(superElement.asType()));
	    
	    Utils.checkGetterAndSetters(superElement);
	    
	    List<VariableElement> fields = ElementFilter.fieldsIn(superElement.getEnclosedElements());
	    CommandMethodsGenerator methGener = new CommandMethodsGenerator(fields);
	    classBuilder.addMethods(methGener.generateMethods());
	    
	    String packageName = superElement.getQualifiedName().toString().replaceAll(elementName, "")+implName;
	    JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();
		JavaFileObject jfo = filer.createSourceFile(packageName + "." + newName);
		Writer writer = jfo.openWriter();
		javaFile.writeTo(writer);
		writer.flush();
		writer.close();
	}
	
}
