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
 * @author fadiga
 */
public class ResultForm extends Form implements CommandListener {
    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);

    PNLCMIDlet midlet;
    Displayable returnTo;

    private static final String[] AV = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    private String ErrorMessage = "";
    private static final int MAX_SIZE = 4;
    private Configuration config;
    private SMSStore store;
    private TextField user_password;
    private TextField num_chirugyField;
    private ChoiceGroup avField;
    private DateField result_dateField;

    Date now = new Date();
    
    public ResultForm(PNLCMIDlet midlet) {
        super("Result CAT");
        this.midlet = midlet;
        config = new Configuration();
        store = new SMSStore();
        //date
        result_dateField =  new DateField("Date opération:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        result_dateField.setDate(now);
        user_password = new TextField("Mot de passe:", null, 20, TextField.ANY);
        num_chirugyField = new TextField("Num. chirugie:", null, MAX_SIZE, TextField.ANY);
        avField = new ChoiceGroup("Acuité visuelle:", ChoiceGroup.POPUP, AV, null);
        append(result_dateField);
        append(num_chirugyField);
        append(avField);
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
        if (num_chirugyField.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        ErrorMessage = "La date indiquée est dans le futur.";
        // Checks Dates
        // Date d'arrivée ne peut pas être dans le future.
        if (SharedChecks.isDateValide(result_dateField.getDate()) != true) {
            ErrorMessage = "[Date d'opération] " + ErrorMessage;
            return false;
        }
        return true;
    }

    public String toSMSFormat(){

        String sep = " ";
        String message;
        String user_name = config.get("user_name");

        int result_date_array[] = SharedChecks.formatDateString(result_dateField.getDate());
        String str_result_date = String.valueOf(result_date_array[2])
                             + SharedChecks.addzero(result_date_array[1])
                             + SharedChecks.addzero(result_date_array[0]);

        message = user_name.replace(' ', Constants.CLEANER)
                  + sep + user_password.getString().replace(' ', Constants.CLEANER)
                  + sep + str_result_date
                  + sep + num_chirugyField.getString()
                  + sep + avField.getString(avField.getSelectedIndex());
        //sms: cat result user_password result_date num_chirugy av
        //example: cat result fad mypass 20150930 HA33 3
        return Constants.KEY_CAT
                 + sep +"result"
                 + sep + message;
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "result");
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

    private String toText() {
        String sep_date = "-";
        int result_date_array[] = SharedChecks.formatDateString(result_dateField.getDate());
        return "[" + result_date_array[0]  + sep_date +
                     result_date_array[1] + sep_date +
                     result_date_array[2] +"] resultat pour id : " 
                   + num_chirugyField.getString();
    }
}
