
package snisi.entities;

import java.util.Vector;

import snisi.entities.EntityHashTable;

/**
 * List of static codes and names for Entities/Locations
 * Automatically generated.
 */


public class EntityHashTableHG51 extends EntityHashTable {

    public EntityHashTableHG51() {
        this.code = "HG51";
        this.name = "COMMUNE2";
        this.children = new Vector();

        
        EntityHashTable hZ3XC5 = new EntityHashTable("Z3XC5", "COMMUNE 2 CSREF");

        

        this.children.addElement(hZ3XC5);
        
        EntityHashTable hZ9MX1 = new EntityHashTable("Z9MX1", "ABOSAC");

        
        EntityHashTable v91092271 = new EntityHashTable("91092271", "BOZOLA");
		hZ9MX1.children.addElement(v91092271);
       	

        this.children.addElement(hZ9MX1);
        
        EntityHashTable hZJFM7 = new EntityHashTable("ZJFM7", "ASACOHI");

        
        EntityHashTable v91092361 = new EntityHashTable("91092361", "HIPPODROME");
		hZJFM7.children.addElement(v91092361);
       	

        this.children.addElement(hZJFM7);
        
        EntityHashTable hZB3M7 = new EntityHashTable("ZB3M7", "ASACOME");

        
        EntityHashTable v91092451 = new EntityHashTable("91092451", "MEDINA-COURA");
		hZB3M7.children.addElement(v91092451);
       	

        this.children.addElement(hZB3M7);
        
        EntityHashTable hZNW63 = new EntityHashTable("ZNW63", "BENKADY");

        
        EntityHashTable v91092091 = new EntityHashTable("91092091", "BAKARIBOUGOU");
		hZNW63.children.addElement(v91092091);
       	
        EntityHashTable v91092181 = new EntityHashTable("91092181", "BOUGOUBA");
		hZNW63.children.addElement(v91092181);
       	
        EntityHashTable v91092811 = new EntityHashTable("91092811", "T-S-F");
		hZNW63.children.addElement(v91092811);
       	

        this.children.addElement(hZNW63);
        
        EntityHashTable hZWAF0 = new EntityHashTable("ZWAF0", "BONIABA");

        
        EntityHashTable v91092631 = new EntityHashTable("91092631", "NIARELA");
		hZWAF0.children.addElement(v91092631);
       	

        this.children.addElement(hZWAF0);
        
        EntityHashTable hZ23B3 = new EntityHashTable("Z23B3", "ASACOMI");

        
        EntityHashTable v91092541 = new EntityHashTable("91092541", "MISSIRA");
		hZ23B3.children.addElement(v91092541);
       	

        this.children.addElement(hZ23B3);
        
        EntityHashTable hZWYC7 = new EntityHashTable("ZWYC7", "ASACOQUIZ");

        
        EntityHashTable v91092721 = new EntityHashTable("91092721", "QUINZAMBOUGOU");
		hZWYC7.children.addElement(v91092721);
       	

        this.children.addElement(hZWYC7);
        
        EntityHashTable hZJEZ5 = new EntityHashTable("ZJEZ5", "ASACOBAGAD");

        
        EntityHashTable v91092001 = new EntityHashTable("91092001", "BAGADADJI");
		hZJEZ5.children.addElement(v91092001);
       	

        this.children.addElement(hZJEZ5);
        
        EntityHashTable hZ25N8 = new EntityHashTable("Z25N8", "ASACONGOMI");

        

        this.children.addElement(hZ25N8);
        
        EntityHashTable hZDX64 = new EntityHashTable("ZDX64", "ASACOGO");

        

        this.children.addElement(hZDX64);
        
    }

}