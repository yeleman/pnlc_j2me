
package snisi.entities;

import java.util.Vector;

import snisi.entities.EntityHashTable;

import snisi.entities.EntityHashTable4Z85;

import snisi.entities.EntityHashTableN696;

import snisi.entities.EntityHashTableW6D2;

import snisi.entities.EntityHashTableS9W6;

import snisi.entities.EntityHashTableRN42;

import snisi.entities.EntityHashTable8P65;

import snisi.entities.EntityHashTableD2K8;

import snisi.entities.EntityHashTableG272;

import snisi.entities.EntityHashTableZWT5;

import snisi.entities.EntityHashTableX922;

import snisi.entities.EntityHashTableR279;

import snisi.entities.EntityHashTableHG51;

import snisi.entities.EntityHashTableGA67;

import snisi.entities.EntityHashTable7GK0;

import snisi.entities.EntityHashTableXRY9;

import snisi.entities.EntityHashTable5SS7;


/**
 * List of static codes and names for Entities/Locations
 * Automatically generated.
 */


public class StaticCodes {

    public EntityHashTable root;

    public Vector getRegions() {
        return root.children;
    }

    public StaticCodes() {

        root = new EntityHashTable("mali", "Mali");

        
        EntityHashTable r4JN7 = new EntityHashTable("4JN7", "KOULIKORO");
        root.children.addElement(r4JN7);

        
        r4JN7.children_names.put("4Z85", "BANAMBA");
        
        r4JN7.children_names.put("N696", "DIOILA");
        
        r4JN7.children_names.put("W6D2", "FANA");
        
        r4JN7.children_names.put("S9W6", "KANGABA");
        
        r4JN7.children_names.put("RN42", "KATI");
        
        r4JN7.children_names.put("8P65", "KOLOKANI");
        
        r4JN7.children_names.put("D2K8", "KOULIKORO");
        
        r4JN7.children_names.put("G272", "NARA");
        
        r4JN7.children_names.put("ZWT5", "OUELESSEBOUGOU");
        
        r4JN7.children_names.put("X922", "KALABAN KORO");
        
        
        EntityHashTable r9GR8 = new EntityHashTable("9GR8", "BAMAKO");
        root.children.addElement(r9GR8);

        
        r9GR8.children_names.put("R279", "COMMUNE1");
        
        r9GR8.children_names.put("HG51", "COMMUNE2");
        
        r9GR8.children_names.put("GA67", "COMMUNE3");
        
        r9GR8.children_names.put("7GK0", "COMMUNE4");
        
        r9GR8.children_names.put("XRY9", "COMMUNE5");
        
        r9GR8.children_names.put("5SS7", "COMMUNE6");
        
        
    }

}
