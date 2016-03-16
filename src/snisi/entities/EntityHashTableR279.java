
package snisi.entities;

import java.util.Vector;

import snisi.entities.EntityHashTable;

/**
 * List of static codes and names for Entities/Locations
 * Automatically generated.
 */


public class EntityHashTableR279 extends EntityHashTable {

    public EntityHashTableR279() {
        this.code = "R279";
        this.name = "COMMUNE1";
        this.children = new Vector();

        
        EntityHashTable hZHCK0 = new EntityHashTable("ZHCK0", "COMMUNE 1 CSREF");

        

        this.children.addElement(hZHCK0);
        
        EntityHashTable hZBXE2 = new EntityHashTable("ZBXE2", "ASACOBA");

        
        EntityHashTable v91091001 = new EntityHashTable("91091001", "BANCONI");
		hZBXE2.children.addElement(v91091001);
       	

        this.children.addElement(hZBXE2);
        
        EntityHashTable hZ3X68 = new EntityHashTable("Z3X68", "ASACOBOUL1");

        
        EntityHashTable v91091112 = new EntityHashTable("91091112", "BOULKASSOUMBOUGOU");
		hZ3X68.children.addElement(v91091112);
       	

        this.children.addElement(hZ3X68);
        
        EntityHashTable hZNM44 = new EntityHashTable("ZNM44", "ASACOBOUL2");

        

        this.children.addElement(hZNM44);
        
        EntityHashTable hZ39F6 = new EntityHashTable("Z39F6", "ASACODJAN");

        

        this.children.addElement(hZ39F6);
        
        EntityHashTable hZPX23 = new EntityHashTable("ZPX23", "ASACODJE");

        
        EntityHashTable v91091223 = new EntityHashTable("91091223", "DIELIBOUGOU");
		hZPX23.children.addElement(v91091223);
       	

        this.children.addElement(hZPX23);
        
        EntityHashTable hZF4F7 = new EntityHashTable("ZF4F7", "ASACODOU");

        
        EntityHashTable v91091334 = new EntityHashTable("91091334", "DOUMAZANA");
		hZF4F7.children.addElement(v91091334);
       	

        this.children.addElement(hZF4F7);
        
        EntityHashTable hZCCM1 = new EntityHashTable("ZCCM1", "ASACOFADJI");

        
        EntityHashTable v91091445 = new EntityHashTable("91091445", "FADJIGUILA");
		hZCCM1.children.addElement(v91091445);
       	

        this.children.addElement(hZCCM1);
        
        EntityHashTable hZ95T5 = new EntityHashTable("Z95T5", "ASACOKOSA");

        
        EntityHashTable v91091667 = new EntityHashTable("91091667", "KOROFINA SUD");
		hZ95T5.children.addElement(v91091667);
       	

        this.children.addElement(hZ95T5);
        
        EntityHashTable hZWH67 = new EntityHashTable("ZWH67", "ASACOMSI");

        

        this.children.addElement(hZWH67);
        
        EntityHashTable hZ6F26 = new EntityHashTable("Z6F26", "ASACONORD");

        
        EntityHashTable v91091556 = new EntityHashTable("91091556", "KOROFINA NORD");
		hZ6F26.children.addElement(v91091556);
       	

        this.children.addElement(hZ6F26);
        
        EntityHashTable hZ8M97 = new EntityHashTable("Z8M97", "ASACOSISOU");

        
        EntityHashTable v91091778 = new EntityHashTable("91091778", "SIKORONI (MEKIN SICORO)");
		hZ8M97.children.addElement(v91091778);
       	

        this.children.addElement(hZ8M97);
        
        EntityHashTable hZ24J0 = new EntityHashTable("Z24J0", "SOTUBA");

        
        EntityHashTable v91091889 = new EntityHashTable("91091889", "SOTUBA");
		hZ24J0.children.addElement(v91091889);
       	

        this.children.addElement(hZ24J0);
        
    }

}