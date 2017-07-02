package ro.develbox.generator.command;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import com.squareup.javapoet.MethodSpec;

import ro.develbox.Utils;
import ro.develbox.processor.stringImpl.StringImpConstants;

public class CommandMethodsGenerator {

	List<VariableElement> fields;

	public CommandMethodsGenerator(List<VariableElement> fields) {
		super();
		this.fields = fields;
	}

	public MethodSpec generateToNetwork() {
		MethodSpec.Builder toNetworkMethod = MethodSpec.methodBuilder("toNetwork");
		toNetworkMethod.addModifiers(Modifier.PUBLIC);
		toNetworkMethod.returns(String.class);
		toNetworkMethod.addAnnotation(Override.class);

		StringBuilder statement = new StringBuilder();
		statement.append("return COMMAND");
		for (VariableElement field : fields) {
			statement.append("+" + StringImpConstants.STRING_SEPARATOR + "+");
			statement.append(Utils.getGetterForField(field)+"()");
		}
		toNetworkMethod.addStatement(statement.toString());
		return toNetworkMethod.build();
	}

	public MethodSpec generateFromNetwork() {
		MethodSpec.Builder fromNetworkMethod = MethodSpec.methodBuilder("fromNetwork");
		fromNetworkMethod.addModifiers(Modifier.PUBLIC);
		fromNetworkMethod.addAnnotation(Override.class);
		fromNetworkMethod.addParameter(String.class, "networkRep");

		fromNetworkMethod.addStatement("String [] split = networkRep.split("+StringImpConstants.STRING_SEPARATOR+")");
		int var = 0 ;
		for (VariableElement field : fields) {
			fromNetworkMethod.addStatement(Utils.getSetterForField(field)+"(split["+var+"])");
			var++;
		}

		return fromNetworkMethod.build();
	}

	public List<MethodSpec> generateMethods() {
		List<MethodSpec> methods = new ArrayList<>();
		methods.add(generateToNetwork());
		methods.add(generateFromNetwork());
		return methods;
	}

}
