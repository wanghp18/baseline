package baseline.hvac;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import baseline.generator.EplusObject;
import baseline.generator.KeyValuePair;

/**
 * This class extracts system template from database <file>hvacsystem.xml<file>
 * according to the specified system type
 * 
 * @author Weili
 *
 */
public class SystemParser {
    
    private final SAXBuilder builder;
    private final File system;
    private Document document;
    
    private ArrayList<EplusObject> objects;
    
    private String systemType;
    
    private static final String FILE_NAME = "hvacsystem.xml";
    
    public SystemParser(String systemType){
	builder = new SAXBuilder();
	system = new File(FILE_NAME);
	
	this.systemType = systemType;
	
	//read the file
	try{
	    document = (Document) builder.build(system);
	}catch(Exception e){
	    e.printStackTrace();
	}
	
	objects = new ArrayList<EplusObject>();
	systemBuilder();
    }
    
    public ArrayList<EplusObject> getSystem(){
	return objects;
    }
    
    private void systemBuilder(){
	Element root = document.getRootElement();
	builderHelper(root);
    }
    
    private void builderHelper(Element current){
	List<Element> children = current.getChildren();
	Iterator<Element> iterator = children.iterator();
	while(iterator.hasNext()){
	    Element child = iterator.next();
	    // if there is an object, find the correct system type dataset
	    if(child.getName().equals("dataset") && child.getAttributeValue("setname").equalsIgnoreCase(systemType)){
		buildObject(child);
	    }
	}
    }
    
    private void buildObject(Element current){
	List<Element> children = current.getChildren();
	Iterator<Element> iterator = children.iterator();
	while (iterator.hasNext()){
	    Element child = iterator.next();
	    String category = child.getAttributeValue("description");
	    String reference = child.getAttributeValue("reference");
	    
	    EplusObject ob = new EplusObject(category,reference);
	    processFields(child,ob);
	    objects.add(ob);
	    
	}
    }
    
    private void processFields(Element node, EplusObject object){
	List<Element> children = node.getChildren();
	Iterator<Element> iterator = children.iterator();
	while(iterator.hasNext()){
	    Element child = iterator.next();
	    KeyValuePair pair = new KeyValuePair(child.getAttributeValue("description"),child.getText());
	    object.addField(pair);
	}
    }
}