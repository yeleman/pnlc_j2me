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
public class TTFixeForm extends Form implements CommandListener {

    
    private static final Command CMD_EXIT = new Command ("Retour",  Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);

    private PNLCMIDlet midlet;
    Displayable returnTo;


    private String ErrorMessage = "";
    private static final int MAX_SIZE = 4;
    private Configuration config;
    private SMSStore store;
    private TextField user_password;
    private TextField consultation_male;
    private TextField consultation_female;
    private TextField surgery_male;
    private TextField surgery_female;
    private TextField refusal_male;
    private TextField refusal_female;
    private TextField recidivism_male;
    private TextField recidivism_female;
    private DateField operation_date;

    Date now = new Date();
    String sep = " ";
    String region_code;
    String district_code;
    
    
    public TTFixeForm(PNLCMIDlet midlet) {
        super("Fixe TT");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();
        
        region_code = config.get("region_code");
        district_code = config.get("district_code");

        //Text
        user_password = new TextField("Mot de passe:", null, 20, TextField.ANY);
        consultation_male = new TextField(Constants.MALE, null, MAX_SIZE, TextField.DECIMAL);
        consultation_female = new TextField(Constants.FEMALE, null, MAX_SIZE, TextField.DECIMAL);
        surgery_male = new TextField(Constants.MALE, null, MAX_SIZE, TextField.DECIMAL);
        surgery_female = new TextField(Constants.FEMALE, null, MAX_SIZE, TextField.DECIMAL);
        refusal_male = new TextField(Constants.MALE, null, MAX_SIZE, TextField.DECIMAL);
        refusal_female = new TextField(Constants.FEMALE, null, MAX_SIZE, TextField.DECIMAL);
        recidivism_male = new TextField(Constants.MALE, null, MAX_SIZE, TextField.DECIMAL);
        recidivism_female = new TextField(Constants.FEMALE, null, MAX_SIZE, TextField.DECIMAL);

        //date
        operation_date =  new DateField("Date opération:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        operation_date.setDate(now);

        append(operation_date);
        append("Nb consultés");
        append(consultation_male);
        append(consultation_female);
        append("Nb opérés");
        append(surgery_male);
        append(surgery_female);
        append("Nb refus");
        append(refusal_male);
        append(refusal_female);
        append("Nb récidives");
        append(recidivism_male);
        append(recidivism_female);
        append(user_password);

        addCommand(CMD_SEND);
        addCommand(CMD_EXIT);
        addCommand(CMD_HELP);

        this.setCommandListener (this);

    }
    public boolean isComplete(){
        
        // all fields are required to be filled.
        if (user_password.getString().length() == 0 ||
            user_password.getString().length() == 0 ||
            consultation_male.getString().length() == 0 ||
            consultation_female.getString().length() == 0 ||
            surgery_male.getString().length() == 0 ||
            surgery_female.getString().length() == 0 ||
            refusal_male.getString().length() == 0 ||
            refusal_female.getString().length() == 0 ||
            recidivism_male.getString().length() == 0 ||
            recidivism_female.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        ErrorMessage = "La date indiquée est dans le futur.";

        int consultation_m = Integer.parseInt(consultation_male.getString());
        int consultation_fe = Integer.parseInt(consultation_female.getString());
        int surgery_m = Integer.parseInt(surgery_male.getString());
        int surgery_fe = Integer.parseInt(surgery_female.getString());
        int refusal_m = Integer.parseInt(refusal_male.getString());
        int refusal_fe = Integer.parseInt(refusal_female.getString());
        int recidivism_m = Integer.parseInt(recidivism_male.getString());
        int recidivism_fe = Integer.parseInt(recidivism_female.getString());

        // operés <= consulté
        // (Hommes)
        if (surgery_m > consultation_m) {
            ErrorMessage = "[Erreur] Nombre d'opérés hommes (" +  surgery_m +
                           ") ne peut être supérieur au nombre de " +
                           "consultés hommes (" + consultation_m + ").";
            return false;
        }
         // (femmes)
        if (surgery_fe > consultation_fe) {
            ErrorMessage =  "[Erreur] Nombre d'opérés femmes (" +  surgery_fe +
                           ") ne peut être supérieur au nombre de " +
                           "consultés femmes (" + consultation_fe + ").";
            return false;
        }

        // refus <= consultés
        // (Hommes)
        if (refusal_m > consultation_m) {
            ErrorMessage = "[Erreur] Nombre de refus hommes (" +  refusal_m +
                           ") ne peut être supérieur au nombre de " +
                           "consultés hommes (" + consultation_m + ").";
            return false;
        }
        // (femmes)
        if (refusal_fe > consultation_fe) {
            ErrorMessage =  "[Erreur] Nombre de refus femmes (" +  refusal_fe +
                           ") ne peut être supérieur au nombre de " +
                           "consultés femmes (" + consultation_fe + ").";
            return false;
        }

        // récidives <= opérés
        // (Hommes)
        if (recidivism_m > surgery_m) {
            ErrorMessage = "[Erreur] Nombre de récidives hommes (" + recidivism_m +
                           ") ne peut être supérieur au nombre d'opérés" +
                           " hommes (" + surgery_m + ").";
            return false;
        }
        // (femmes)
        if (recidivism_fe > surgery_fe) {
            ErrorMessage = "[Erreur] Nombre de récidives femmes (" + recidivism_fe +
                           ") ne peut être supérieur au nombre d'opérés" +
                           " femmes (" + surgery_fe + ").";
            return false;
        }

        // opérés + refus <= consultés
        // (Hommes)
        if ((surgery_m + refusal_m) > consultation_m) {
            ErrorMessage = "[Erreur] La somme des opérés et refus hommes (" +
                            surgery_m + refusal_m +
                           ") ne peut être supérieur au nombre de " +
                           "consultés hommes (" + consultation_m + ").";
            return false;
        }
        // (femmes)
        if ((surgery_fe + refusal_fe) > consultation_fe) {
            ErrorMessage = "[Erreur] La somme des opérés et refus femmes (" +
                            surgery_fe + refusal_fe +
                           ") ne peut être supérieur au nombre de " +
                           "consultés femmes (" + consultation_fe + ").";
            return false;
        }
        // Checks Dates
        // Date d'arrivée ne peut pas être dans le future.
        if (SharedChecks.isDateValide(operation_date.getDate()) != true) {
            ErrorMessage = "[Date d'opération] " + ErrorMessage;
            return false;
        }
        return true;
    }

    public String toSMSFormat() {

        String user_name = config.get("user_name");

        int operation_date_array[] = SharedChecks.formatDateString(operation_date.getDate());
        String operation_date_str = String.valueOf(operation_date_array[2])
                             + SharedChecks.addzero(operation_date_array[1])
                             + SharedChecks.addzero(operation_date_array[0]);
        
        /**SMS Text: tt visit user_name user_password village_code
                     consultation_male consultation_female surgery_male surgery_female
                     refusal_male refusal_female recidivism_male operation_date
        example:tt fixe fad hdjjd G272 00 0 0 0 0 00 0 0 20151001 **/

        return Constants.KEY_TT + sep + "fixe"
                                + sep + user_name.replace(' ', Constants.CLEANER)
                                + sep + user_password.getString().replace(' ', Constants.CLEANER)
                                + sep + district_code
                                + sep + consultation_male.getString()
                                + sep + consultation_female.getString()
                                + sep + surgery_male.getString()
                                + sep + surgery_female.getString()
                                + sep + refusal_male.getString()
                                + sep + refusal_female.getString()
                                + sep + recidivism_male.getString()
                                + sep + recidivism_female.getString()
                                + sep + operation_date_str;
    }

    public String toText() {
        String sep_date = "-";
        int operation_date_array[] = SharedChecks.formatDateString(operation_date.getDate());

        return "[" + operation_date_array[0]  + sep_date +
                     operation_date_array[1] + sep_date +
                     operation_date_array[2] + "] TT fixe " + district_code ;
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "Trachoma_fixe");
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
