
package snisi.entities;

import java.util.Vector;

import snisi.entities.EntityHashTable;

/**
 * List of static codes and names for Entities/Locations
 * Automatically generated.
 */


public class EntityHashTableXRY9 extends EntityHashTable {

    public EntityHashTableXRY9() {
        this.code = "XRY9";
        this.name = "COMMUNE5";
        this.children = new Vector();

        
        EntityHashTable hZXBY5 = new EntityHashTable("ZXBY5", "COMMUNE 5 CSREF");

        

        this.children.addElement(hZXBY5);
        
        EntityHashTable hZM4M4 = new EntityHashTable("ZM4M4", "ADASCO");

        

        this.children.addElement(hZM4M4);
        
        EntityHashTable hZTX81 = new EntityHashTable("ZTX81", "ASACOBADA PMI");

        
        EntityHashTable v91095100 = new EntityHashTable("91095100", "BADALABOUGOU");
		hZTX81.children.addElement(v91095100);
       	
        EntityHashTable v91095595 = new EntityHashTable("91095595", "QUARTIER SEMA");
		hZTX81.children.addElement(v91095595);
       	

        this.children.addElement(hZTX81);
        
        EntityHashTable hZ2860 = new EntityHashTable("Z2860", "ASACODA");

        
        EntityHashTable v91095199 = new EntityHashTable("91095199", "FLABOUGOU-DAOUDABOUGOU");
		hZ2860.children.addElement(v91095199);
       	

        this.children.addElement(hZ2860);
        
        EntityHashTable hZE8T0 = new EntityHashTable("ZE8T0", "ASACOGA");

        
        EntityHashTable v91095900 = new EntityHashTable("91095900", "GARANTIGUIBOUGOU");
		hZE8T0.children.addElement(v91095900);
       	

        this.children.addElement(hZE8T0);
        
        EntityHashTable hZ4B69 = new EntityHashTable("Z4B69", "ASACOKAL");

        
        EntityHashTable v91095397 = new EntityHashTable("91095397", "KALABANCOURA");
		hZ4B69.children.addElement(v91095397);
       	

        this.children.addElement(hZ4B69);
        
        EntityHashTable hZ5543 = new EntityHashTable("Z5543", "ASACOSAB I");

        
        EntityHashTable v91095694 = new EntityHashTable("91095694", "SABALIBOUGOU");
		hZ5543.children.addElement(v91095694);
       	

        this.children.addElement(hZ5543);
        
        EntityHashTable hZR2R4 = new EntityHashTable("ZR2R4", "ASACOSAB II");

        
        EntityHashTable v91095793 = new EntityHashTable("91095793", "SEMA II");
		hZR2R4.children.addElement(v91095793);
       	

        this.children.addElement(hZR2R4);
        
        EntityHashTable hZDMC3 = new EntityHashTable("ZDMC3", "ASACOSAB III");

        

        this.children.addElement(hZDMC3);
        
        EntityHashTable hZJF51 = new EntityHashTable("ZJF51", "ASACOTOQUA");

        
        EntityHashTable v91095496 = new EntityHashTable("91095496", "QUARTIER MALI");
		hZJF51.children.addElement(v91095496);
       	
        EntityHashTable v91095892 = new EntityHashTable("91095892", "TOROKOROBOUGOU");
		hZJF51.children.addElement(v91095892);
       	

        this.children.addElement(hZJF51);
        
        EntityHashTable hZGWS9 = new EntityHashTable("ZGWS9", "ASCOMBACODJI");

        
        EntityHashTable v91095001 = new EntityHashTable("91095001", "BACO-DJIKORONI");
		hZGWS9.children.addElement(v91095001);
       	

        this.children.addElement(hZGWS9);
        
        EntityHashTable hZN3C7 = new EntityHashTable("ZN3C7", "ASCODA");

        

        this.children.addElement(hZN3C7);
        
        EntityHashTable hZZ4E3 = new EntityHashTable("ZZ4E3", "ASACOKAL ACI");

        

        this.children.addElement(hZZ4E3);
        
    }

}