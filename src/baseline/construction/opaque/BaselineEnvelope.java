package baseline.construction.opaque;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import baseline.generator.EplusObject;
import baseline.generator.IdfReader;
import baseline.generator.IdfReader.ValueNode;
import baseline.util.ClimateZone;

public class BaselineEnvelope {
    private final IdfReader baselineModel;
    private final OpaqueEnvelopeParser envelope;
    
    private final ClimateZone zone;
    
    //EnergyPlus Objects that require to be removed
    private static final String MATERIAL="Material";
    private static final String MATERIALMASS="Material:NoMass";
    private static final String CONSTRUCTION="Construction";
    private static final String SIMPLE_WINDOW = "WindowMaterial:SimpleGlazingSystem";
    
    //Baseline Construction Name (to replace the original constructions)
    private static final String EXTERNAL_WALL = "Project Wall";
    private static final String ROOF = "Project Roof";
    private static final String PARTITION = "Project Partition";
    private static final String INTERNAL_FLOOR = "Project Internal Floor";
    private static final String BG_WALL = "Project Below Grade Wall";
    private static final String EXTERNAL_FLOOR = "Project External Floor";
    private static final String SOG_FLOOR = "Project Slab On Grade Floor";
    private static final String WINDOW="Project Window";
    private static final String CURTAIN_WALL="Project Curtain Wall";
    private static final String SKYLIGHT="Project Skylight";
    
    //EnergyPlus objects that relates to the construction changes
    private static final String BLDG_SURFACE = "BuildingSurface:Detailed";
    private static final String BLDG_FENE = "FenestrationSurface:Detailed";

    public BaselineEnvelope(IdfReader m, ClimateZone c){
	baselineModel = m;
	zone=c;
	envelope = new OpaqueEnvelopeParser(zone);
    }
    
    public void execute(){
	//remove all the building envelope related objects
	baselineModel.removeEnergyPlusObject(MATERIAL);
	baselineModel.removeEnergyPlusObject(MATERIALMASS);
	baselineModel.removeEnergyPlusObject(CONSTRUCTION);
	baselineModel.removeEnergyPlusObject(SIMPLE_WINDOW);
	
	//add new building envelope related objects
	ArrayList<EplusObject> objects = envelope.getObjects(); //retrieve the data from database
	for(EplusObject o: objects){
	    String[] objectValues = new String[o.getSize()];
	    String[] objectDes = new String[o.getSize()];
	    //loop over the key-value pairs
	    for(int i=0; i<objectValues.length; i++){
		objectValues[i]=o.getKeyValuePair(i).getValue();
		objectDes[i] = o.getKeyValuePair(i).getKey();
	    }
	    //add the object to the baseline model
	    baselineModel.addNewEnergyPlusObject(o.getObjectName(), objectValues, objectDes);
	}
	
	//change the EnergyPlus object that relates to the constructions
	replaceBuildingSurface();
	replaceFenestrationSurface();
    }
    
    /**
     * replace the fenestration surface
     */
    private void replaceFenestrationSurface(){
	HashMap<String, HashMap<String, ArrayList<ValueNode>>> surfaces = baselineModel.getObjectList(BLDG_FENE);
	Set<String> elementCount = surfaces.get(BLDG_FENE).keySet();
	Iterator<String> elementIterator = elementCount.iterator();
	while(elementIterator.hasNext()){
	    String count = elementIterator.next();
	    ArrayList<ValueNode> fenestrationList = surfaces.get(BLDG_FENE).get(count);
	    String surfaceType = null;
	    
	    //first loop, find the criteria for the selection
	    for(ValueNode v: fenestrationList){
		if(v.getDescription().equalsIgnoreCase("SURFACE TYPE")){
		    surfaceType = v.getAttribute();
		}
	    }
	    
	    //set the construction name for the fenestration
	    String constructionName = null;
	    if(surfaceType!=null){
		if(surfaceType.equalsIgnoreCase("WINDOW")){
		    constructionName = WINDOW;
		}
	    }
	    
	    //second loop, change the construction name
	    for(ValueNode v: fenestrationList){
		if(v.getDescription().equalsIgnoreCase("CONSTRUCTION NAME")){
		    v.setAttribute(constructionName);
		    break;
		}
	    }
	}
    }
    
    /**
     * replace the building surface constructions
     */
    private void replaceBuildingSurface(){
	HashMap<String, HashMap<String, ArrayList<ValueNode>>> surfaces = baselineModel.getObjectList(BLDG_SURFACE);
	Set<String> elementCount = surfaces.get(BLDG_SURFACE).keySet();
	Iterator<String> elementIterator = elementCount.iterator();
	while(elementIterator.hasNext()){
	    String count = elementIterator.next();
	    ArrayList<ValueNode> surfaceList = surfaces.get(BLDG_SURFACE).get(count);
	    String surfaceType = null;
	    String outsideBoundary = null;
	    
	    //first loop, find the criteria for the selection
	    for(ValueNode v: surfaceList){
		if(v.getDescription().equalsIgnoreCase("SURFACE TYPE")){
		    surfaceType = v.getAttribute();
		}
		if(v.getDescription().equalsIgnoreCase("OUTSIDE BOUNDARY CONDITION")){
		    outsideBoundary = v.getAttribute();
		}
	    }
	    
	    String constructionName = null;
	    if(surfaceType!=null && outsideBoundary!=null){
		if(surfaceType.equalsIgnoreCase("WALL")&&outsideBoundary.equalsIgnoreCase("OUTDOORS")){
		    constructionName = EXTERNAL_WALL;
		}else if(surfaceType.equalsIgnoreCase("WALL")&&outsideBoundary.equalsIgnoreCase("SURFACE")){
		    constructionName = PARTITION;
		}else if(surfaceType.equalsIgnoreCase("WALL")&&outsideBoundary.equalsIgnoreCase("GROUND")){
		    constructionName = BG_WALL;
		}else if(surfaceType.equalsIgnoreCase("FLOOR")&&outsideBoundary.equalsIgnoreCase("SURFACE")){
		    constructionName = INTERNAL_FLOOR;
		}else if(surfaceType.equalsIgnoreCase("FLOOR")&&outsideBoundary.equalsIgnoreCase("OUTDOORS")){
		    constructionName = EXTERNAL_FLOOR;
		}else if(surfaceType.equalsIgnoreCase("FLOOR")&&outsideBoundary.equalsIgnoreCase("GROUND")){
		    constructionName = SOG_FLOOR;
		}else if(surfaceType.equalsIgnoreCase("Ceiling")){
		    constructionName = INTERNAL_FLOOR;
		}else if(surfaceType.equalsIgnoreCase("ROOF")){
		    constructionName = ROOF;
		}else{
		    constructionName = ""; //hopefully never gets to this point
		}
	    }

	    //second loop, modify the construction name
	    for(ValueNode v: surfaceList){
		if(v.getDescription().equalsIgnoreCase("CONSTRUCTION NAME")){
		    v.setAttribute(constructionName);
		    break;
		}
	    }
	}
    }
}
