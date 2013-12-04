package snisi.entities;

import java.util.Vector;

import snisi.entities.EntityHashTable;

/**
 * List of static codes and names for Entities/Locations
 * Automatically generated.
 */


public class EntityHashTable8R92 extends EntityHashTable {

    public EntityHashTable8R92() {
        this.code = "8R92";
        this.name = "MARKALA";
        this.children = new Vector();

        EntityHashTable diamarabougou = new EntityHashTable("diam", "Diamarabougou");
        EntityHashTable dioro = new EntityHashTable("diorov", "Dioro Village");

        EntityHashTable diorohc = new EntityHashTable("diorohc", "CSCOM Dioro");
        diorohc.children.addElement(diamarabougou);
        diorohc.children.addElement(dioro);

        this.children.addElement(diorohc);
    }

}