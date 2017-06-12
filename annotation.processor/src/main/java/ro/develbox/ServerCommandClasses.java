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

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import ro.develbox.commands.exceptions.ErrorCommandException;
import ro.develbox.commands.exceptions.WarnCommandException;
import ro.develbox.protocol.ICommunicationChannel;
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
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameter(ICommunicationChannel.class, "commChannel")
                .addStatement("super(new ClientResponserImpl(), commChannel)").build();
        methods.add(constructor);

        for (TypeElement element : elements) {
            // public void login(String email,String password) throws
            // WarnCommandException, ErrorCommandException,
            // ProtocolViolatedException, IOException {
            // CommandLogin login = (CommandLogin)
            // createCommand(CommandLogin.COMMAND);
            // login.setEmail(email);
            // login.setPassword(password);
            // startCommandSequence(login);
            // }
            String name = element.getSimpleName().toString();

            List<ParameterSpec> parameters = new ArrayList<>();
            
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
            
            // get all fields of the class

            com.squareup.javapoet.MethodSpec.Builder builder = MethodSpec.methodBuilder(name);

            builder.addModifiers(Modifier.PUBLIC).returns(void.class).addParameters(parameters)
                    .addException(WarnCommandException.class).addException(ErrorCommandException.class)
                    .addException(ProtocolViolatedException.class).addException(IOException.class).build();

            MethodSpec method = builder.build();
            methods.add(method);
        }

        TypeSpec clientProtocol = TypeSpec.classBuilder("ClientProtocolImpl22").addModifiers(Modifier.PUBLIC)
                .addMethods(methods).build();

        JavaFile javaFile = JavaFile.builder("ro.develbox", clientProtocol).build();
        JavaFileObject jfo = filer.createSourceFile("ClientProtocolImpl22");
        Writer writer = jfo.openWriter();
        javaFile.writeTo(writer);
        writer.flush();
        writer.close();
    }
}
