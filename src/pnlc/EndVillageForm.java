
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


public class EndVillageForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Enreg.",
                                                            Command.OK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 2);

    PNLCMIDlet midlet;
    Displayable returnTo;
    private String ErrorMessage = "";

    private Configuration config;
    private SMSStore store;

    //register
    private static final String[] YesNon = {Constants.NON, Constants.OUI};
    private ChoiceGroup relay;
    private DateField arrived_on;
    private DateField left_on;
    Date now = new Date();
    String sep = " ";

    public EndVillageForm(PNLCMIDlet midlet) {
        super("Données de naissance");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();
        //date
        arrived_on =  new DateField("Date d'arrivée:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        arrived_on.setDate(now);
        left_on =  new DateField("Date de départ:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        left_on.setDate(now);

        //text

        //choice
        relay = new ChoiceGroup("Rélais:", ChoiceGroup.POPUP, YesNon, null);

        append(arrived_on);
        append(left_on);

        addCommand(CMD_EXIT);
        addCommand(CMD_SEND);
        addCommand(CMD_HELP);

        this.setCommandListener (this);
    }

    public boolean isComplete() {

        // all fields are required to be filled.
        // if (family_name.getString().length() == 0) {
        //     return false;
        // }
        return true;
    }

    public boolean isValid() {
        //TODO: Si c'est Autre qui est choisi comme lieu de naissance le change précision devient obligatoire.
        //      la date de naissance ne doit pas être superière à la date d'enregistrement.
        ErrorMessage = "La date indiquée est dans le futur.";

        if (SharedChecks.isDateValide(arrived_on.getDate()) != true) {
            ErrorMessage = "[Date de visite] " + ErrorMessage;
            return false;
        }

        if (SharedChecks.isDateValide(left_on.getDate()) != true) {
            ErrorMessage = "[Date de naissance] " + ErrorMessage;
            return false;
        }
        if (SharedChecks.compareDobDod(left_on.getDate(), arrived_on.getDate()) == true) {
            ErrorMessage = "[Erreur] la date de visite ne peut pas être inferieure à la date de la naissance.";
            return false;
        }

        return true;
    }

    public String toSMSFormat() {

        int rel;

        int arrived_on_array[] = SharedChecks.formatDateString(arrived_on.getDate());
        String reporting_d = String.valueOf(arrived_on_array[2])
                             + SharedChecks.addzero(arrived_on_array[1])
                             + SharedChecks.addzero(arrived_on_array[0]);

        int left_on_array[] = SharedChecks.formatDateString(left_on.getDate());
        String left_on_d = String.valueOf(left_on_array[2])
                             + SharedChecks.addzero(left_on_array[1])
                             + SharedChecks.addzero(left_on_array[0]);


        if (relay.getString(relay.getSelectedIndex()).equals(Constants.OUI))
            rel = 1;
        else
            rel = 0;


        return "tt visit" + sep + arrived_on + sep + left_on
                            + sep + rel;
    }

    public String toText() {
        int left_on_array[] = SharedChecks.formatDateString(left_on.getDate());

        return "N-" + left_on_array[0] + "] ";
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "born");
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
