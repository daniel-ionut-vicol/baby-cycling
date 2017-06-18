package ro.develbox;

import java.util.List;

import javax.lang.model.element.Element;

public class Utils {

    public static String getMethod(String type, String field) {
        String firstLetter = field.substring(0, 1);
        String rest = field.substring(1);
        return type + firstLetter.toUpperCase() + rest;
    }

    public static void checkMethod(String type, List<String> fields, List<Element> methods) throws Exception {
        for (String field : fields) {
            checkMethod(type, field, methods);
        }
    }

    public static void checkMethod(String type, String field, List<Element> methods) throws Exception {
        String expectedName = Utils.getMethod(type, field);
        for (Element method : methods) {
            if (method.getSimpleName().toString().equals(expectedName)) {
                return;
            }
        }
        throw new Exception("Could not find setter(" + expectedName + ") for field : " + field);
    }

    public static void checkSetters(List<String> fields, List<Element> methods) throws Exception {
        checkMethod("set", fields, methods);
    }

    public static String getSetterForField(String field) {
        return getMethod("set", field);
    }

    public static String getGetterForField(String field) {
        return getMethod("get", field);
    }
    
    public static void checkSetter(String field, List<Element> methods) throws Exception {
        checkMethod("set", field, methods);
    }
    
    public static void checkGetters(List<String> fields, List<Element> methods) throws Exception {
        checkMethod("get", fields, methods);
    }

    public static void checkgetter(String field, List<Element> methods) throws Exception {
        checkMethod("get", field, methods);
    }
}
