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

    public void generateClientCode(Elements elementUtils, Filer filer) throws IOException {

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
            
            //method parameters
            List<? extends Element> enclosing = element.getEnclosedElements();
            for (Element enclosed : enclosing){
                if(enclosed.getKind() == ElementKind.FIELD && !enclosed.getModifiers().contains(Modifier.STATIC)){
                    TypeMirror type = enclosed.asType();
                    TypeName tn = TypeName.get(type);
                    String paramName = enclosed.toString();
                    ParameterSpec param = ParameterSpec.builder(tn, paramName).build();
                    parameters.add(param);
                }
            }
            com.squareup.javapoet.MethodSpec.Builder builder = MethodSpec.methodBuilder(name);

            builder.addModifiers(Modifier.PUBLIC).returns(void.class).addParameters(parameters)
                    .addException(WarnCommandException.class).addException(ErrorCommandException.class)
                    .addException(ProtocolViolatedException.class).addException(IOException.class).build();

            //method body 
            
//            CommandLogin login = (CommandLogin) client.createCommand(CommandLogin.COMMAND);
//    		login.setEmail(email);
//    		login.setPassword(password);
//    		client.startCommandSequence(login);
            ClassName className = ClassName.get(element);
            builder.addStatement("$T comm = ($T) client.createCommand($T.COMMAND);", className,className,className);
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
    
    private TypeName getTypeName(Elements elementUtils,String canonicalClassName){
    	TypeElement commChannel = elementUtils.getTypeElement("ro.develbox.protocol.client.ClientProtocol");
        TypeName type = TypeName.get(commChannel.asType());
        return type;
    }
}
