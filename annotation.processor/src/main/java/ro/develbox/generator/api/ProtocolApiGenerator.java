package ro.develbox.generator.api;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import ro.develbox.Utils;
import ro.develbox.commands.Command;
import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.processor.stringImpl.StringImpConstants;
import ro.develbox.protocol.IProtocolResponse;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

public class ProtocolApiGenerator {
	private List<TypeElement> elements;

	public ProtocolApiGenerator() {
		elements = new ArrayList<>();
	}

	public void addClass(TypeElement element) {
		elements.add(element);
	}
	
	public void addClasses(List<TypeElement> elements){
		this.elements.addAll(elements);
	}

	public void clear() {
		elements.clear();
	}

	public void generateCode(Elements elementUtils, Filer filer, String packageName, String generatedClass,
			String superClass) throws Exception {

		List<MethodSpec> methods = new ArrayList<>();

		TypeName superProtocolType = getTypeName(elementUtils, superClass);
		TypeName communicationChannel = getTypeName(elementUtils, "ro.develbox.protocol.ICommunicationChannel");

		// class fields
		TypeSpec.Builder protocolApi = TypeSpec.classBuilder(generatedClass).addModifiers(Modifier.PUBLIC);
		protocolApi.superclass(superProtocolType);
		// class constructors
		MethodSpec constructor = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameter(IProtocolResponse.class, "responder")
				.addParameter(communicationChannel,"commChannel")
				.addStatement("super($L,$L,$L)","responder","commChannel", "new " + StringImpConstants.PACKAGE+"."+StringImpConstants.CONSTR_CLASS+"()")
				.addStatement("responder.setCommandConstr(new " + StringImpConstants.PACKAGE+"."+StringImpConstants.CONSTR_CLASS+"())")
				.addStatement("commChannel.addListener(this)")
				.build();
		methods.add(constructor);

		// class methods
		for (TypeElement element : elements) {
			String name = "send"+element.getSimpleName().toString();

			List<ParameterSpec> parameters = new ArrayList<>();

			// method parameters
			List<? extends Element> enclosing = element.getEnclosedElements();
			for (Element enclosed : enclosing) {
				if (enclosed.getKind() == ElementKind.FIELD && !enclosed.getModifiers().contains(Modifier.STATIC)) {
					TypeMirror type = enclosed.asType();
					TypeName tn = TypeName.get(type);
					String paramName = enclosed.toString();
					ParameterSpec param = ParameterSpec.builder(tn, paramName).build();
					parameters.add(param);
				}
			}
			com.squareup.javapoet.MethodSpec.Builder builder = MethodSpec.methodBuilder(name);

			builder.addModifiers(Modifier.PUBLIC).returns(void.class).addParameters(parameters)
					.returns(Command.class)
					.addException(WarnCommandException.class).addException(ErrorCommandException.class)
					.addException(ProtocolViolatedException.class).addException(IOException.class).build();

			List<VariableElement> fields = Utils.filterStaticFields(ElementFilter.fieldsIn(element.getEnclosedElements()));
			List<ExecutableElement> elementMethods = ElementFilter.methodsIn(element.getEnclosedElements());
			try {
				Utils.checkSetters(fields, elementMethods);
			} catch (Exception e) {
				// add class name to exception message
				throw new Exception(e.getMessage() + " class " + element.getSimpleName());
			}

			ClassName className = ClassName.get(element);
			String varName = "comm";
			builder.addStatement("$T " + varName + " = ($T) createCommand($T.COMMAND);", className, className,
					className);
			for (VariableElement field : fields) {
				builder.addStatement(varName + "." + Utils.getSetterForField(field.getSimpleName().toString()) + "(" + field + ")");
			}
			builder.addStatement("return startCommandSequence(comm)");
			MethodSpec method = builder.build();
			methods.add(method);
		}

		protocolApi.addMethods(methods);

		JavaFile javaFile = JavaFile.builder(packageName, protocolApi.build()).build();
		JavaFileObject jfo = filer.createSourceFile(packageName + "." + generatedClass);
		Writer writer = jfo.openWriter();
		javaFile.writeTo(writer);
		writer.flush();
		writer.close();
	}

	private TypeName getTypeName(Elements elementUtils, String canonicalClassName) {
		TypeElement commChannel = elementUtils.getTypeElement(canonicalClassName);
		TypeName type = TypeName.get(commChannel.asType());
		return type;
	}
}
