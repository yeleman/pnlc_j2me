package pnlc;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import pnlc.Configuration.*;
import pnlc.Constants.*;
import snisi.entities.Utils.*;
import pnlc.SharedChecks.*;
import java.util.Date;

/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author Fad
 */

public class VisitVillageForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",  Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);
    private static final Command CMD_CONTINUE = new Command ("Continuer", Command.HELP, 2);


    PNLCMIDlet midlet;
    Displayable returnTo;

    private String ErrorMessage = "";
    private static final int MAX_SIZE = 4;

    private Configuration config;
    private SMSStore store;

    //register
    private TextField user_password;
    private TextField consultation_male;
    private TextField consultation_female;
    private TextField surgery_male;
    private TextField surgery_female;
    private TextField refusal_male;
    private TextField refusal_female;
    private TextField recidivism_male;
    private TextField recidivism_female;
    private static final String[] YesNon = {Constants.NON, Constants.OUI};
    private ChoiceGroup commutity_assistance;
    private DateField arrived_on;
    private DateField left_on;
    private ChoiceGroup locationField;
    private ChoiceGroup health_centerField;

    Date now = new Date();
    String sep = " ";
    String region_code;
    String district_code;
    String last_health_center;
    String health_center_code;
    String village_code;

    public VisitVillageForm(PNLCMIDlet midlet) {
        super("Visite village");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();

        region_code = config.get("region_code");
        district_code = config.get("district_code");
        last_health_center = config.get("last_health_center");

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
        arrived_on =  new DateField(" arrivée:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        arrived_on.setDate(now);
        left_on =  new DateField(" départ:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        left_on.setDate(now);
        health_centerField = new ChoiceGroup("Aire de santé:",
                                             ChoiceGroup.POPUP,
                                             snisi.entities.Utils.hcenters_names(region_code, district_code), null);
        health_centerField.setSelectedIndex(Integer.parseInt(last_health_center), true);

        //choice
        commutity_assistance = new ChoiceGroup("Aide du relais:", ChoiceGroup.POPUP, YesNon, null);
        append(health_centerField);


        addCommand(CMD_CONTINUE);
        addCommand(CMD_EXIT);
        addCommand(CMD_HELP);

        this.setCommandListener (this);
    }

    public boolean isComplete() {

        // all fields are required to be filled.
        if (user_password.getString().length() == 0) {
            return false;
        }
        if (consultation_male.getString().length() == 0) {
            return false;
        }
        if (consultation_female.getString().length() == 0) {
            return false;
        }
        if (surgery_male.getString().length() == 0) {
            return false;
        }
        if (surgery_female.getString().length() == 0) {
            return false;
        }
        if (refusal_male.getString().length() == 0) {
            return false;
        }
        if (refusal_female.getString().length() == 0) {
            return false;
        }
        if (recidivism_male.getString().length() == 0) {
            return false;
        }
        if (recidivism_female.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        ErrorMessage = "La date indiquée est dans le futur.";

        int consultation = Integer.parseInt(consultation_male.getString()) +
                           Integer.parseInt(consultation_female.getString());
        int surgery = Integer.parseInt(surgery_male.getString()) +
                      Integer.parseInt(surgery_female.getString());
        int refusal = Integer.parseInt(refusal_male.getString()) +
                      Integer.parseInt(refusal_female.getString());
        int recidivism = Integer.parseInt(recidivism_male.getString()) +
                         Integer.parseInt(recidivism_female.getString());

        if (surgery > consultation) {
            ErrorMessage = "[Erreur] Nombre total opérés ne peut être superière au nombre total consulté.";
            return false;
        }
        if (refusal > consultation) {
            ErrorMessage = "[Erreur] Nombre total refus ne peut être superière au nombre total consulté.";
            return false;
        }
        if (recidivism > consultation) {
            ErrorMessage = "[Erreur] Nombre total récidives ne peut être superière au nombre total consulté.";
            return false;
        }

        if ((surgery + refusal + recidivism) > consultation) {
            ErrorMessage = "[Erreur] La somme des totaux de opérés, refus et récidives ne peut être superière au nombre total consulté.";
            return false;
        }

        // Checks Dates
        if (SharedChecks.isDateValide(arrived_on.getDate()) != true) {
            ErrorMessage = "[Date d'arrivée] " + ErrorMessage;
            return false;
        }

        if (SharedChecks.isDateValide(left_on.getDate()) != true) {
            ErrorMessage = "[Date de départ] " + ErrorMessage;
            return false;
        }
        if (SharedChecks.compareleftarrived(arrived_on.getDate(), left_on.getDate()) == true) {
            ErrorMessage = "[Erreur] la date de départ ne peut pas être antérieure à la date d'arrivée.";
            return false;
        }

        return true;
    }

    public String toSMSFormat() {
        int relay;

        int arrived_on_array[] = SharedChecks.formatDateString(arrived_on.getDate());
        String arrived_on_d = String.valueOf(arrived_on_array[2])
                             + SharedChecks.addzero(arrived_on_array[1])
                             + SharedChecks.addzero(arrived_on_array[0]);

        int left_on_array[] = SharedChecks.formatDateString(left_on.getDate());
        String left_on_d = String.valueOf(left_on_array[2])
                             + SharedChecks.addzero(left_on_array[1])
                             + SharedChecks.addzero(left_on_array[0]);

        if (commutity_assistance.getString(commutity_assistance.getSelectedIndex()).equals(Constants.OUI))
            relay = 1;
        else
            relay = 0;

        String user_name = config.get("user_name");
        String health_center_code_index = String.valueOf(health_centerField.getSelectedIndex());

        // On sauvegarde l'index du centre de santé pour l'ulitiser par defaut après
        config.set("last_health_center", health_center_code_index);

        // village location
        village_code = snisi.entities.Utils.villages_codes(region_code, district_code, health_center_code)[locationField.getSelectedIndex()];

        return "tt visit" + sep + user_name.replace(' ', '_')
                          + sep + user_password.getString().replace(' ', '_')
                          + sep + village_code
                          + sep + consultation_male.getString()
                          + sep + consultation_female.getString()
                          + sep + surgery_male.getString()
                          + sep + surgery_female.getString()
                          + sep + refusal_male.getString()
                          + sep + refusal_female.getString()
                          + sep + recidivism_male.getString()
                          + sep + recidivism_female.getString()
                          + sep + relay
                          + sep + arrived_on_d
                          + sep + left_on_d;
    }

    public String toText() {
        String sepa = "-";
        int left_on_array[] = SharedChecks.formatDateString(left_on.getDate());

        return "[" + left_on_array[0] + sepa + left_on_array[1]
                   + sepa + left_on_array[2] + "] " + locationField.getString(locationField.getSelectedIndex());
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "visite_village");
            this.midlet.display.setCurrent(h);
        }

        // exit commands comes back to main menu.
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        if (c == CMD_CONTINUE) {
            health_center_code = snisi.entities.Utils.hcenters_codes(region_code, district_code)[health_centerField.getSelectedIndex()];
            locationField = new ChoiceGroup("Village:",
                                            ChoiceGroup.POPUP,
                                            snisi.entities.Utils.villages_names(region_code,
                                                                    district_code,
                                                                    health_center_code), null);

            append(locationField);
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
            append(commutity_assistance);
            append("Dates :");
            append(arrived_on);
            append(left_on);
            append(user_password);
            removeCommand(CMD_CONTINUE);
            addCommand(CMD_SEND);
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
                this.midlet.startApp();
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            }

        }
    }
}
