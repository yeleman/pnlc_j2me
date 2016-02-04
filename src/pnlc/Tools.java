/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pnlc;

import javax.microedition.lcdui.ChoiceGroup;

/**
 *
 * @author f
 */
public class Tools {

    public static String getEyeValue(ChoiceGroup eyeField) {
       String eye = "left";
     if (eyeField.getString(eyeField.getSelectedIndex()).equals(Constants.OD)){
            eye = "right";
     }
     return eye;
    }
    public static String getSexeValue(ChoiceGroup sexField) {
       String sex = "M";
     if (sexField.getString(sexField.getSelectedIndex()).equals(Constants.FEMALE)){
            sex = "F";
     }
     return sex;
    }
}
