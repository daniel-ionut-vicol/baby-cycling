package ro.develbox;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import com.squareup.javapoet.MethodSpec;

public class CommandMethodsGenerator {

	List<VariableElement> fields;

	public CommandMethodsGenerator(List<VariableElement> fields) {
		super();
		this.fields = fields;
	}

//    @Override
//    public String toNetwork() {
//        return COMMAND+getKey();
//    }
//
//    @Override
//    public void fromNetwork(String networkRep) {
//        setKey(networkRep);
//    }
	
	public MethodSpec generateToNetwork() {
		MethodSpec.Builder toNetworkMethod = MethodSpec.methodBuilder("toNetwork");
		toNetworkMethod.addModifiers(Modifier.PUBLIC);
		toNetworkMethod.returns(String.class);
		toNetworkMethod.addAnnotation(Override.class);

		for(VariableElement field : fields){
			
		}
		toNetworkMethod.addStatement("return \"\"");
		return toNetworkMethod.build();
	}
	
	public MethodSpec generateFromNetwork() {
		MethodSpec.Builder fromNetworkMethod = MethodSpec.methodBuilder("fromNetwork");
	    fromNetworkMethod.addModifiers(Modifier.PUBLIC);
	    fromNetworkMethod.addAnnotation(Override.class);
	    fromNetworkMethod.addParameter(String.class, "networkRep");

	    for(VariableElement field : fields){
			
		}
	    
	    return fromNetworkMethod.build();
	}
	
	public List<MethodSpec> generateMethods(){
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(generateToNetwork());
		methods.add(generateFromNetwork());
		return methods;
	}


}
