package ro.develbox;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import ro.develbox.processor.stringImpl.GenerateCommandStringImpProcessor;

public class Test {

    public static void main(String[] args) throws Exception {
       String source = "d:/baby-cycling/protocol/src/main/java";
//       String compiled = "d:/baby-cycling/protocol/build/classes/main";

       Iterable<JavaFileObject> files = getSourceFiles(source);
//       Iterable<JavaFileObject> compiledFiles = getSourceFiles(compiled);

       List<JavaFileObject> allfiles = new ArrayList<>();
       Iterator<JavaFileObject> i = files.iterator();
       while(i.hasNext()){
    	   allfiles.add(i.next());
       }
//        i = compiledFiles.iterator();
//       while(i.hasNext()){
//    	   allfiles.add(i.next());
//       }
       
       JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

       List<String> optionList = new ArrayList<String>();
       optionList.add("-classpath");
       optionList.add(System.getProperty("java.class.path"));
       
       CompilationTask task = compiler.getTask(new PrintWriter(System.out), null, null, optionList, null, allfiles);
       task.setProcessors(Arrays.asList(new GenerateCommandStringImpProcessor()));

       task.call();
    }

    private static Iterable<JavaFileObject> getSourceFiles(String p_path) throws Exception {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null);

      files.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(p_path)));
      Set<Kind> fileKinds = Collections.singleton(Kind.SOURCE);
      
      return files.list(StandardLocation.SOURCE_PATH, "", fileKinds, true);
    }
    
    private static Iterable<JavaFileObject> getCompileFiles(String p_path) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null);

        files.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File(p_path)));
        Set<Kind> fileKinds = Collections.singleton(Kind.CLASS);
        return files.list(StandardLocation.CLASS_OUTPUT, "", fileKinds, true);
      }
}
