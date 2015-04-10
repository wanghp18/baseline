package baseline.idfdata;

import baseline.generator.EplusObject;

/**
 * a generic thermal zone interface that represents different types of thermal zone representations defined in EnergyPlus
 * The thermal zone contains most of a space's thermal related information (exclude coordinates) including:
 * zone full name, the block / floor the zone belongs to, zone type, zone identification, HVAC zone, zone cooling load
 * zone heating load, and zone outdoor ventilation requirement
 * 
 * @author Weili
 *
 */
public interface ThermalZone {
    
    /**
     * get the full name of the thermal zone
     * @return
     */
    public String getFullName();
    
    /**
     * set the block of the thermal zone
     * @param block
     */
    public void setBlock(String block);
    
    /**
     * set the floor of the thermal zone
     * @param block
     */
    public void setFloor(String floor);
    
    /**
     * set the zone type of the thermal zone
     * @param block
     */
    public void setZoneType(String zoneType);
    
    /**
     * set the zone identification of the thermal zone
     * @param block
     */
    public void setZoneIdentification(String zoneIdentification);
    
    /**
     * set the hvac system of the thermal zone
     * @param block
     */
    public void setHVACZone(String hvacZone);
    
    /**
     * set the cooling load of this thermal zone
     * @param load
     */
    public void setCoolingLoad(Double load);
    
    /**
     * set the heating load of this thermal zone
     * @param load
     */
    public void setHeaingLoad(Double load);
    
    /**
     * Set the ventilaiton method, currently, methods only includes Sum, Maximum
     * Flow/Person, Flow/Area, Flow/Zone and AirChange/Hour
     * @param method
     */
    public void setVentilationMethod(String method);
    
    /**
     * Set the ventilation rate for the thermal zone
     * @param method
     * @param rate
     */
    public void setVentilationRate(String method, String rate);
    
    /**
     * get the block of the thermal zone
     * @param block
     */
    public String getBlock();
    
    /**
     * get the floor of the thermal zone
     * @param block
     */
    public String getFloor();
    
    /**
     * get the zone type of the thermal zone
     * @param block
     */
    public String getZoneType();
        
    /**
     * get the zone identification of the thermal zone
     * @param block
     */
    public String getZoneIdentification();
    
    /**
     * get the hvac system of the thermal zone
     * @param block
     */
    public String getHVACZone();
    
    /**
     * get the cooling load of this thermal zone
     * @return
     */
    public Double getCoolingLoad();
    
    /**
     * get the heating load of this thermal zone
     * @return
     */
    public Double getHeatingLoad();
    
    /**
     * get the built outdoor air ventilation object in EnergyPlus
     * @return
     */
    public EplusObject getOutdoorAirObject();

}
