
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
        
        
    }

}
