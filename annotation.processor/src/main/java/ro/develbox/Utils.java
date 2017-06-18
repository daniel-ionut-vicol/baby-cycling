package ro.develbox;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

public class Utils {

	public static List<VariableElement> filterStaticFields(List<VariableElement> fields) {
		List<VariableElement> filtered = new ArrayList<>();
		for (VariableElement field : fields) {
			if (!field.getModifiers().contains(Modifier.STATIC)) {
				filtered.add(field);
			}
		}
		return filtered;
	}

	public static void checkGetterAndSetters(Element element) throws Exception {
		List<VariableElement> fields = filterStaticFields(ElementFilter.fieldsIn(element.getEnclosedElements()));
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
		Utils.checkSetters(fields, methods);
		Utils.checkGetters(fields, methods);
	}

	public static void checkSetters(List<VariableElement> fields, List<ExecutableElement> methods) throws Exception {
		checkMethod("set", fields, methods);
	}

	public static void checkGetters(List<VariableElement> fields, List<ExecutableElement> methods) throws Exception {
		checkMethod("get", fields, methods);
	}

	public static void checkMethod(String type, List<VariableElement> fields, List<ExecutableElement> methods)
			throws Exception {
		for (VariableElement field : fields) {
			checkMethod(type, field.getSimpleName().toString(), methods);
		}
	}

	public static void checkMethod(String type, String field, List<ExecutableElement> methods) throws Exception {
		String expectedName = Utils.getMethod(type, field);
		for (Element method : methods) {
			if (method.getSimpleName().toString().equals(expectedName)) {
				return;
			}
		}
		throw new Exception("Could not find setter(" + expectedName + ") for field : " + field);
	}

	public static String getSetterForField(String field) {
		return getMethod("set", field);
	}

	public static String getGetterForField(String field) {
		return getMethod("get", field);
	}

	public static String getMethod(String type, String field) {
		String firstLetter = field.substring(0, 1);
		String rest = field.substring(1);
		return type + firstLetter.toUpperCase() + rest;
	}

}
