package baseline.hvac.system7;

import java.util.ArrayList;
import java.util.HashMap;

import baseline.generator.EplusObject;
import baseline.hvac.SystemParser;
import baseline.idfdata.EnergyPlusBuilding;

public class HVACSystem7Factory {
    //extract the template system
    private final SystemParser system = new SystemParser("System Type 7");
    
    private HashMap<String,ArrayList<EplusObject>> systemObjects;
    
    private SystemType7 systemType7;
    
    private EnergyPlusBuilding building;
    
    public HVACSystem7Factory(EnergyPlusBuilding building){
	systemObjects = new HashMap<String,ArrayList<EplusObject>>();
	this.building = building;
	processTemplate();
	systemType7 = new HVACSystem7(systemObjects,building);
    }
    
    /**
     * Control the creation of the system type 7
     * @return
     */
    public SystemType7 getSystem(){
	return systemType7;
    }
    
    
    /**
     * Separate the three systems into three data lists.
     */
    private void processTemplate(){
	ArrayList<EplusObject> template = system.getSystem();
	for(EplusObject eo: template){
	    if(eo.getReference().startsWith("Supply Side System")){
		if(!systemObjects.containsKey("Supply Side System")){
		    systemObjects.put("Supply Side System", new ArrayList<EplusObject>());
		}
		systemObjects.get("Supply Side System").add(eo);
	    }else if(eo.getReference().startsWith("Demand Side System")){
		if(!systemObjects.containsKey("Demand Side System")){
		    systemObjects.put("Demand Side System", new ArrayList<EplusObject>());
		}
		systemObjects.get("Demand Side System").add(eo);
	    }else if(eo.getReference().startsWith("Plant")){
		if(!systemObjects.containsKey("Plant")){
		    systemObjects.put("Plant", new ArrayList<EplusObject>());
		}
		systemObjects.get("Plant").add(eo);
	    }
	}
    }
}