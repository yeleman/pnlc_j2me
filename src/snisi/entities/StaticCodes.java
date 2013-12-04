
package snisi.entities;

import java.util.Vector;

import snisi.entities.EntityHashTable;
import snisi.entities.EntityHashTable8R92;

/**
 * List of static codes and names for Entities/Locations
 * Automatically generated.
 * @author reg
 */


public class StaticCodes {

    public EntityHashTable root;

    public Vector getRegions() {
        return root.children;
    }

    public StaticCodes() {

        root = new EntityHashTable("mali", "Mali");

        EntityHashTable r26P7 = new EntityHashTable("26P7", "SIKASSO");
        root.children.addElement(r26P7);
        EntityHashTable r2732 = new EntityHashTable("2732", "SEGOU");

        EntityHashTable8R92 d8R92 = new EntityHashTable8R92();
        r2732.children.addElement(d8R92); // MARKALA

        root.children.addElement(r2732);
        EntityHashTable r4JN7 = new EntityHashTable("4JN7", "KOULIKORO");
        root.children.addElement(r4JN7);
        EntityHashTable r8EA4 = new EntityHashTable("8EA4", "TOMBOUCTOU");
        root.children.addElement(r8EA4);
        EntityHashTable r9GR8 = new EntityHashTable("9GR8", "BAMAKO");
        root.children.addElement(r9GR8);
        EntityHashTable rGJM3 = new EntityHashTable("GJM3", "KAYES");
        root.children.addElement(rGJM3);
        EntityHashTable rSSH3 = new EntityHashTable("SSH3", "MOPTI");
        root.children.addElement(rSSH3);
        EntityHashTable rZ3A5 = new EntityHashTable("Z3A5", "KIDAL");
        root.children.addElement(rZ3A5);
        EntityHashTable rZBE9 = new EntityHashTable("ZBE9", "GAO");
        root.children.addElement(rZBE9);

    }

}
