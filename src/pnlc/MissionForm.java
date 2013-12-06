package pnlc;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import pnlc.Configuration.*;
import pnlc.Constants.*;
import pnlc.SharedChecks.*;
import java.util.Date;

/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author fadiga
 */


public class MissionForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);

    PNLCMIDlet midlet;
    Displayable returnTo;
    private String ErrorMessage = "";

    private Configuration config;
    private SMSStore store;

    //Mission
    private DateField arrived_or_left_on;
    private static final String[] FMA = {Constants.MOBILE, Constants.ADVANCED, Constants.FIXED};
    private TextField user_password;
    private ChoiceGroup strategy;

    Date now = new Date();
    String sep = " ";

    public MissionForm(PNLCMIDlet midlet) {
        super("Mission");
        this.midlet = midlet;
        config = new Configuration();
        store = new SMSStore();
        // date
        arrived_or_left_on =  new DateField("Date:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        arrived_or_left_on.setDate(now);
        // text
        user_password = new TextField("Mot de passe:", null, 20, TextField.ANY);

        append(arrived_or_left_on);
        if (this.midlet.mission.equals(Constants.START)){
            strategy = new ChoiceGroup("Strategie:", ChoiceGroup.POPUP, FMA, null);
            append(strategy);
        }
        append(user_password);

        addCommand(CMD_EXIT);
        addCommand(CMD_SEND);
        addCommand(CMD_HELP);
        this.setCommandListener (this);
    }

    public boolean isComplete() {
        // all fields are required to be filled.
        if (user_password.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        if (SharedChecks.isDateValide(arrived_or_left_on.getDate()) != true) {
            ErrorMessage = "[Date] La date indiquée est dans le futur.";
            return false;
        }
        return true;
    }

    public String toSMSFormat() {

        String message;
        String mission = Constants.END;
        String type_strategy = null;
        String user_name = config.get("user_name");
        String district_code = config.get("district_code");
        String operator_type = config.get("operator_type");

        int arrived_or_left_on_array[] = SharedChecks.formatDateString(arrived_or_left_on.getDate());
        String arrived_or_left = String.valueOf(arrived_or_left_on_array[2])
                               + SharedChecks.addzero(arrived_or_left_on_array[1])
                               + SharedChecks.addzero(arrived_or_left_on_array[0]);

        message = user_name.replace(' ', '_')
                  + sep + user_password.getString().replace(' ', '_')
                  + sep + district_code
                  + sep + operator_type
                  + sep + arrived_or_left;

        if (this.midlet.mission.equals(Constants.START)){

            mission = Constants.START;
            if (strategy.getString(strategy.getSelectedIndex()).equals(Constants.FIXED))
                type_strategy = "fixed";
            if (strategy.getString(strategy.getSelectedIndex()).equals(Constants.MOBILE))
                type_strategy = "mobile";
            if (strategy.getString(strategy.getSelectedIndex()).equals(Constants.ADVANCED))
                type_strategy = "advanced";
            message = message + sep + type_strategy;
        }

       return "tt" + sep + mission + sep + message;
    }

    public String toText() {
        String sepa = "-";
        int arrived_or_left_on_array[] = SharedChecks.formatDateString(arrived_or_left_on.getDate());

        return "[" + arrived_or_left_on_array[0]
                   + sepa + arrived_or_left_on_array[1]
                   + sepa + arrived_or_left_on_array[2] + "] "
                   + this.midlet.mission;
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
