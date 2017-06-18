package ro.develbox;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class CommandImplGenerator {

	TypeElement superElement;
	
	String implName;
	
	public CommandImplGenerator(TypeElement superElement,String implName) {
		super();
		this.superElement = superElement;
		this.implName = implName;
	}
	
	public void generateCode(Elements elementUtils){
	    String elementName = superElement.getSimpleName().toString();
	    String newName = elementName+implName;
	    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(newName);
	    classBuilder.addModifiers(Modifier.PUBLIC);
	    classBuilder.superclass(TypeName.get(superElement.asType()));
	    
//	    @Override
//	    public String toNetwork() {
//	        return COMMAND+getKey();
//	    }
//
//	    @Override
//	    public void fromNetwork(String networkRep) {
//	        setKey(networkRep);
//	    }
	    
	    MethodSpec.Builder toNetworkMethod = MethodSpec.methodBuilder("toNetwork");
	    toNetworkMethod.addModifiers(Modifier.PUBLIC);
	    toNetworkMethod.returns(String.class);
	    toNetworkMethod.addAnnotation(Override.class);
	    
	    //check getters 
	    
	    //actual implementation
	    List<VariableElement> fields = ElementFilter.fieldsIn(superElement.getEnclosedElements());
	    for(VariableElement field : fields){
	        
	    }
	    
	}
	
}
