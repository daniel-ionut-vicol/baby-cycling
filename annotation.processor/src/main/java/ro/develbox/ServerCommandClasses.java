package ro.develbox;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.exceptions.ProtocolViolatedException;

public class ServerCommandClasses {
    private List<TypeElement> elements;

    public ServerCommandClasses() {
        elements = new ArrayList<>();
    }

    public void addClass(TypeElement element) {
        elements.add(element);
    }

    public void clear() {
        elements.clear();
    }

    public void generateClientCode(Elements elementUtils, Filer filer) throws Exception {

        List<MethodSpec> methods = new ArrayList<>();
        
        TypeName clientProtocolType = getTypeName(elementUtils, "ro.develbox.protocol.client.ClientProtocol");
        
        //class fields
        TypeSpec.Builder clientProtocol = TypeSpec.classBuilder("ClientProtocolApiWrapper").addModifiers(Modifier.PUBLIC).addField(clientProtocolType, "client");
        
        //class constructors
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameter(clientProtocolType, "client")
                .addStatement("this.client=client").build();
        methods.add(constructor);

        //class methods
        for (TypeElement element : elements) {
            String name = element.getSimpleName().toString();

            List<ParameterSpec> parameters = new ArrayList<>();

            //collection of all fields names 
            List<String> fieldsName = new ArrayList<>();

            //collection of all public methods of this element
            //used to make sure that each fields has setter defined
            List<Element> elementMethods = new ArrayList<>();
            
            //method parameters
            List<? extends Element> enclosing = element.getEnclosedElements();
            for (Element enclosed : enclosing){
                if(enclosed.getKind() == ElementKind.FIELD && !enclosed.getModifiers().contains(Modifier.STATIC)){
                    TypeMirror type = enclosed.asType();
                    TypeName tn = TypeName.get(type);
                    String paramName = enclosed.toString();
                    fieldsName.add(paramName);
                    ParameterSpec param = ParameterSpec.builder(tn, paramName).build();
                    parameters.add(param);
                } else if (enclosed.getKind() == ElementKind.METHOD
                        && !enclosed.getModifiers().contains(Modifier.STATIC)
                        && !enclosed.getModifiers().contains(Modifier.PRIVATE)) {
                    elementMethods.add(enclosed);
                }
            }
            com.squareup.javapoet.MethodSpec.Builder builder = MethodSpec.methodBuilder(name);

            builder.addModifiers(Modifier.PUBLIC).returns(void.class).addParameters(parameters)
                    .addException(WarnCommandException.class).addException(ErrorCommandException.class)
                    .addException(ProtocolViolatedException.class).addException(IOException.class).build();

            try{
                checkSetters(fieldsName,elementMethods);
            }catch(Exception e){
                //add class name to exception message
                throw new Exception(e.getMessage()+" class " + element.getSimpleName());
            }
            
            ClassName className = ClassName.get(element);
            String varName = "comm";
            builder.addStatement("$T "+varName+" = ($T) client.createCommand($T.COMMAND);", className,className,className);
            for(String field : fieldsName ){
                builder.addStatement(varName+"."+getSetterForField(field)+"("+field+")");
            }
            builder.addStatement("client.startCommandSequence(comm)");
            MethodSpec method = builder.build();
            methods.add(method);
        }

        clientProtocol.addMethods(methods);

        JavaFile javaFile = JavaFile.builder("ro.develbox", clientProtocol.build()).build();
        JavaFileObject jfo = filer.createSourceFile("ro.develbox.ClientProtocolApiWrapper");
        Writer writer = jfo.openWriter();
        javaFile.writeTo(writer);
        writer.flush();
        writer.close();
    }
    
    private void checkSetters(List<String> fields, List<Element> methods) throws Exception{
        for (String field: fields){
            checkSetter(field,methods);
        }
    }
    
    private void checkSetter(String field, List<Element> methods)throws Exception{
    	String expectedName = getSetterForField(field);
        for(Element method : methods){
            if(method.getSimpleName().toString().equals(expectedName)){
                return;
            }
        }
        throw new Exception("Could not find setter("+expectedName+") for field : " + field );
    }
    
    private String getSetterForField(String field){
        String firstLetter = field.substring(0,1);
        String rest = field.substring(1);
        return "set"+firstLetter.toUpperCase()+rest;
    }
    
    private TypeName getTypeName(Elements elementUtils,String canonicalClassName){
    	TypeElement commChannel = elementUtils.getTypeElement("ro.develbox.protocol.client.ClientProtocol");
        TypeName type = TypeName.get(commChannel.asType());
        return type;
    }
}
