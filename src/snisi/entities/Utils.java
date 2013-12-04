package snisi.entities;

import java.util.Vector;
import java.util.Enumeration;
import snisi.entities.StaticCodes;

/**
 * StaticCodes Management
 * @author reg
 */

public class Utils {

    public Utils() {

    }

    public static EntityHashTable getTableFromVector(String code, Vector vector) {

        for(Enumeration elem = vector.elements(); elem.hasMoreElements();) {
            EntityHashTable elem_ht = (EntityHashTable)elem.nextElement();
            if (elem_ht.code.equals(code)) {
                return elem_ht;
            }
        }

        EntityHashTable dummy = new EntityHashTable("dummy", "Dummy");
        return dummy;
    }

    public static String[] getListOfValuesFromVector(Vector vector, boolean return_name) {
        int num = vector.size();
        String[] values = new String[num];
        int i = 0;

        for(Enumeration elem = vector.elements(); elem.hasMoreElements();) {
            EntityHashTable elem_ht = (EntityHashTable)elem.nextElement();
            if (return_name) {
                values[i] = elem_ht.name;
            } else {
                values[i] = elem_ht.code;
            }
            i++;
        }
        return values;
    }

    public static String[] regions_codes() {
        StaticCodes static_codes = new StaticCodes();
        return getListOfValuesFromVector(static_codes.getRegions(), false);
    }

    public static String[] regions_names() {
        StaticCodes static_codes = new StaticCodes();
        return getListOfValuesFromVector(static_codes.getRegions(), true);
    }


    public static String[] districts_codes(String region_code) {
        StaticCodes static_codes = new StaticCodes();
        EntityHashTable myregion = getTableFromVector(region_code,
                                                      static_codes.getRegions());
        return getListOfValuesFromVector(myregion.children, false);
    }

    public static String[] districts_names(String region_code) {
        StaticCodes static_codes = new StaticCodes();
        EntityHashTable myregion = getTableFromVector(region_code,
                                                      static_codes.getRegions());
        return getListOfValuesFromVector(myregion.children, true);
    }

    public static String[] hcenters_codes(String region_code,
                                          String district_code) {
        StaticCodes static_codes = new StaticCodes();

        EntityHashTable myregion = getTableFromVector(region_code,
                                                      static_codes.getRegions());
        EntityHashTable mydistrict = getTableFromVector(district_code,
                                                        myregion.children);
        return getListOfValuesFromVector(mydistrict.children, false);
    }

    public static String[] hcenters_names(String region_code,
                                          String district_code) {
        StaticCodes static_codes = new StaticCodes();
        EntityHashTable myregion = getTableFromVector(region_code,
                                                      static_codes.getRegions());
        EntityHashTable mydistrict = getTableFromVector(district_code,
                                                        myregion.children);

        return getListOfValuesFromVector(mydistrict.children, true);
    }

    public static String[] villages_codes(String region_code,
                                          String district_code,
                                          String hcenter_code) {
        StaticCodes static_codes = new StaticCodes();
        EntityHashTable myregion = getTableFromVector(region_code,
                                                      static_codes.getRegions());
        EntityHashTable mydistrict = getTableFromVector(district_code,
                                                        myregion.children);
        EntityHashTable myhcenter = getTableFromVector(hcenter_code,
                                                       mydistrict.children);

        return getListOfValuesFromVector(myhcenter.children, false);
    }

    public static String[] villages_names(String region_code,
                                          String district_code,
                                          String hcenter_code) {
        StaticCodes static_codes = new StaticCodes();
        EntityHashTable myregion = getTableFromVector(region_code,
                                                      static_codes.getRegions());
        EntityHashTable mydistrict = getTableFromVector(district_code,
                                                        myregion.children);
        EntityHashTable myhcenter = getTableFromVector(hcenter_code,
                                                       mydistrict.children);

        return getListOfValuesFromVector(myhcenter.children, true);
    }

}
