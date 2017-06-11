package ro.develbox;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class ServerCommandClasses {
	private List<TypeElement> elements ;
	
	public ServerCommandClasses() {
		elements = new ArrayList<>();
	}
	
	public void addClass(TypeElement element){
		elements.add(element);
	}
	
	public void generateClientCode(Elements elementUtils, Filer filer){
		
	}
}
