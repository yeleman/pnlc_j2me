
package pnlc;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import pnlc.Configuration.*;
import pnlc.Constants.*;
import pnlc.Entities.*;
import pnlc.SharedChecks.*;
import java.util.Date;

/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author fadiga
 */


public class MissionForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 2);

    PNLCMIDlet midlet;
    Displayable returnTo;
    private String ErrorMessage = "";

    private Configuration config;
    private SMSStore store;

    //Mission
    private DateField mission_date;
    private static final String[] startend = {Constants.start, Constants.end};
    private TextField user_pass;
    private ChoiceGroup status_mission;

    Date now = new Date();
    String sep = " ";

    public MissionForm(PNLCMIDlet midlet) {
        super("Mission");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();
        //date
        mission_date =  new DateField("Date:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        mission_date.setDate(now);
        //text
        status_mission = new ChoiceGroup("Début ou fin:", ChoiceGroup.POPUP, startend, null);
        user_pass = new TextField("Mon de passe:", null, 20, TextField.ANY);

        append(mission_date);
        append(status_mission);
        append(user_pass);

        addCommand(CMD_EXIT);
        addCommand(CMD_SEND);
        addCommand(CMD_HELP);

        this.setCommandListener (this);
    }

    public boolean isComplete() {

        // all fields are required to be filled.
        if (user_pass.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        //TODO: Si c'est Autre qui est choisi comme lieu de naissance le change précision devient obligatoire.
        //      la date de naissance ne doit pas être superière à la date d'enregistrement.
        ErrorMessage = "La date indiquée est dans le futur.";

        if (SharedChecks.isDateValide(mission_date.getDate()) != true) {
            ErrorMessage = "[Date] " + ErrorMessage;
            return false;
        }
        return true;
    }

    public String toSMSFormat() {

        int mission;

        int reporting_date_array[] = SharedChecks.formatDateString(mission_date.getDate());
        String reporting_d = String.valueOf(reporting_date_array[2])
                             + SharedChecks.addzero(reporting_date_array[1])
                             + SharedChecks.addzero(reporting_date_array[0]);

        String user_name = config.get("user_name");

        if (status_mission.getString(status_mission.getSelectedIndex()).equals(Constants.start))
            mission = 1;
        else
            mission = 0;

        return "pnlc mission" + sep + user_name + sep + user_pass.getString() + sep + reporting_d
                            + sep + mission;
    }

    public String toText() {
        int mission_array[] = SharedChecks.formatDateString(mission_date.getDate());

        return "N-" + mission_array[0] + "] " + status_mission.getSelectedIndex();
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "mission");
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
                this.midlet.startApp();
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            }

        }
    }
}
