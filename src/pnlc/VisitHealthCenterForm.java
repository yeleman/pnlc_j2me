/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pnlc;


import javax.microedition.lcdui.*;
import java.util.TimeZone;
import pnlc.Configuration.*;
import pnlc.Constants.*;
import java.util.Date;
/**
 *
 * @author fad
 */
public class VisitHealthCenterForm extends Form implements CommandListener {

    
    private static final Command CMD_EXIT = new Command ("Retour",  Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);

    private PNLCMIDlet midlet;
    Displayable returnTo;
    
    private String ErrorMessage = "";

    private Configuration config;
    private SMSStore store;

    private static final String[] EYE = {Constants.OD, Constants.OG};
    private static final String[] SEXE = {"M", "F"};
    private TextField user_password;
    private TextField ageField;
    private ChoiceGroup health_centerField;
    private ChoiceGroup eyeField;
    private ChoiceGroup sexeField;
    private DateField operation_date;

    Date now = new Date();
    String sep = " ";
    String region_code;
    String district_code;
    String last_health_center;
    String health_center_code;
    
    
    public VisitHealthCenterForm(PNLCMIDlet midlet) {
        super("Visite Aire");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();
        
        region_code = config.get("region_code");
        district_code = config.get("district_code");
        last_health_center = config.get("last_health_center");

        //Text
        user_password = new TextField("Mot de passe:", null, 20, TextField.ANY);
        ageField = new TextField("Age:", null, 4, TextField.DECIMAL);
        eyeField = new ChoiceGroup("Oeil:", ChoiceGroup.POPUP, EYE, null);
        sexeField = new ChoiceGroup("Sexe:", ChoiceGroup.POPUP, SEXE, null);

        //date
        operation_date =  new DateField("Date opération:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        operation_date.setDate(now);
        health_centerField = new ChoiceGroup("Aire de santé:",
                                             ChoiceGroup.POPUP,
                                             snisi.entities.Utils.hcenters_names(district_code), null);
        health_centerField.setSelectedIndex(Integer.parseInt(last_health_center), true);

        append(health_centerField);
        append(operation_date);
        append(sexeField);
        append(eyeField);
        append(ageField);
        append(user_password);

        addCommand(CMD_SEND);
        addCommand(CMD_EXIT);
        addCommand(CMD_HELP);

        this.setCommandListener (this);

    }
    public boolean isComplete(){
        
        // all fields are required to be filled.
        if (user_password.getString().length() == 0) {
            return false;
        }
        // all fields are required to be filled.
        if (ageField.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        ErrorMessage = "La date indiquée est dans le futur.";
        // Checks Dates
        // Date d'arrivée ne peut pas être dans le future.
        if (SharedChecks.isDateValide(operation_date.getDate()) != true) {
            ErrorMessage = "[Date d'opération] " + ErrorMessage;
            return false;
        }

        if (Integer.parseInt(ageField.getString()) > 140) {
            ErrorMessage = "[Erreur] l'age est supérieur 140";
            return false;
        }
        return true;
    }

    public String toSMSFormat() {

        String user_name = config.get("user_name");
        // village location
        health_center_code = snisi.entities.Utils.hcenters_codes(
                district_code)[health_centerField.getSelectedIndex()];

        int operation_date_array[] = SharedChecks.formatDateString(operation_date.getDate());
        String str_operation_date = String.valueOf(operation_date_array[2])
                             + SharedChecks.addzero(operation_date_array[1])
                             + SharedChecks.addzero(operation_date_array[0]);

        //SMS Text: ct visit Fad TTTT Z7R24 20150928 55 M OD
        return "ct visit" + sep +
               user_name.replace(' ', Constants.CLEANER) + sep +
               user_password.getString().replace(' ', Constants.CLEANER) + sep +
               health_center_code + sep +
               str_operation_date + sep +
               sexeField.getString(sexeField.getSelectedIndex()) + sep +
               eyeField.getString(eyeField.getSelectedIndex()) + sep +
               ageField.getString();
    }

    public String toText() {
        String sep_date = "-";
        int operation_date_array[] = SharedChecks.formatDateString(operation_date.getDate());

        return "[" + operation_date_array[0]  + sep_date +
                     operation_date_array[1] + sep_date +
                     operation_date_array[2] + "] visite air " + sep +
                     health_centerField.getString(health_centerField.getSelectedIndex()) ;
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "visite_health_center");
            this.midlet.display.setCurrent(h);
        }

        // exit commands comes back to main menu.
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        // save command
        if (c == CMD_SEND) {
            Alert alert;

            if (!this.isComplete()) {
                alert = new Alert("Données manquantes", "Tous les champs " +
                                  "requis doivent être remplis!", null,
                                   AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            if (!this.isValid()) {
                alert = new Alert("Données incorrectes!", this.ErrorMessage,
                                  null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            // sends the sms and reply feedback
            SMSSender sms = new SMSSender();
            String number = config.get("server_number");
            if (sms.send(number, this.toSMSFormat())) {
                alert = new Alert ("Demande envoyée !", "Vous allez recevoir" +
                                   " une confirmation du serveur.",
                                   null, AlertType.CONFIRMATION);
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
               if (store.add(this.toText(), this.toSMSFormat())) {
                    alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer" +
                                       " la demande par SMS. Le rapport a été enregistré dans le téléphone.", null,
                                       AlertType.WARNING);
                } else {
                    alert = new Alert ("Échec d'enregistrement", "Impossible d'envoyer ni d'enregistrer dans le téléphone.", null,
                                       AlertType.WARNING);
                }
                alert.setTimeout(Alert.FOREVER);
                this.midlet.startApp();
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            }
        }
    }
}
