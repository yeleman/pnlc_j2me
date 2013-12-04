package snisi.entities;

import java.util.Vector;

/**
 * List of static codes and names for Entities/Locations
 * Automatically generated.
 * @author reg
 */


public class EntityHashTable {

    String code;
    String name;
    Vector children = new Vector();

    public EntityHashTable() {
    }

    public EntityHashTable(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public EntityHashTable(String code, String name, Vector children) {
        this.code = code;
        this.name = name;
        this.children = children;
    }

}