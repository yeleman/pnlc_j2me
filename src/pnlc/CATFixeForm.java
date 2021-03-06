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
public class CATFixeForm extends Form implements CommandListener {


    private static final Command CMD_EXIT = new Command ("Retour",  Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);

    private PNLCMIDlet midlet;
    Displayable returnTo;

    private String ErrorMessage = "";

    private Configuration config;
    private SMSStore store;

    private static final String[] EYE = {Constants.OD, Constants.OG};
    private static final String[] SEXE = {Constants.MALE, Constants.FEMALE};
    private TextField user_password;
    private TextField ageField;
    private TextField numField;
    private ChoiceGroup health_centerField;
    private ChoiceGroup eyeField;
    private ChoiceGroup sexField;
    private DateField dateField;

    Date now = new Date();
    String sep = " ";
    String district_code;


    public CATFixeForm(PNLCMIDlet midlet) {
        super("Cat. fixe");
        this.midlet = midlet;

        config = new Configuration();
        store = new SMSStore();
        district_code = config.get("district_code");
        //Text
        numField = new TextField("Numéro:", null, 3, TextField.NUMERIC);
        user_password = new TextField("Mot de passe:", null, 20, TextField.ANY);
        ageField = new TextField("Age:", null, 4, TextField.NUMERIC);
        eyeField = new ChoiceGroup("Oeil:", ChoiceGroup.POPUP, EYE, null);
        sexField = new ChoiceGroup("Sexe:", ChoiceGroup.POPUP, SEXE, null);
        //date
        dateField =  new DateField("Date:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        dateField.setDate(now);
        append(dateField);
        append(numField);
        append(sexField);
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
        if (numField.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        ErrorMessage = "La date indiquée est dans le futur.";
        // Checks Dates
        // Date d'arrivée ne peut pas être dans le future.
        if (SharedChecks.isDateValide(dateField.getDate()) != true) {
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
        int date_array[] = SharedChecks.formatDateString(dateField.getDate());
        String operation_date_str = String.valueOf(date_array[2])
                             + SharedChecks.addzero(date_array[1])
                             + SharedChecks.addzero(date_array[0]);

        /** SMS Text: cat fixe user_name user_password district_code operation_date sex eye age
            example: cat fixe fad pass G272 20151002 M right 4 23
        **/
        return Constants.KEY_CAT + sep + "fixe"
                                 + sep + user_name.replace(' ', Constants.CLEANER)
                                 + sep + user_password.getString().replace(' ', Constants.CLEANER)
                                 + sep + district_code
                                 + sep + operation_date_str
                                 + sep + Tools.getSexeValue(sexField)
                                 + sep + Tools.getEyeValue(eyeField)
                                 + sep + ageField.getString()
                                 + sep + numField.getString();
    }

    public String toText() {
        String sep_date = "-";
        int date_array[] = SharedChecks.formatDateString(dateField.getDate());

        return "[" + date_array[0]  + sep_date +
                     date_array[1] + sep_date +
                     date_array[2] + "] CAT fixe ";
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "fixe_cat");
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
