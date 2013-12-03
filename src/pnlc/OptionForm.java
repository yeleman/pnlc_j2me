
package pnlc;

import javax.microedition.lcdui.*;
import pnlc.Configuration.*;
import pnlc.Constants.*;
import pnlc.HelpForm.*;
import pnlc.Entities.*;

/**
 * J2ME Form allowing Server number, health center and hc_code editing.
 * Saves the new number into <code>Configuration</code>
 * Saves the new health center into <code>Configuration</code>
 * Saves the new hc_code into <code>Configuration</code>
 * @author fadiga
 */

public class OptionForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Enreg.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);
    private static final Command CMD_CONTINUE = new Command ("Continuer", Command.HELP, 2);

    private Configuration config;
    private String[] districts;

    private TextField numberField;
    private TextField user_nameField;
    private ChoiceGroup regionField;
    private ChoiceGroup districtField;
    private String ErrorMessage = "";

    PNLCMIDlet midlet;

public OptionForm(PNLCMIDlet midlet) {
    super("Paramètres de transmission");
    this.midlet = midlet;

    config = new Configuration();
    // districts = Constants.codes_district();
    districts = Entities.cercles_codes();

    // retrieve phone number from config
    // if not present, use constant
    String phone_number = "";
    phone_number = config.get("server_number");
    if (phone_number.equals("")) {
        phone_number = Constants.server_number;
    }

    numberField = new TextField ("Numéro du serveur:", phone_number, 8, TextField.PHONENUMBER);
    user_nameField = new TextField("Nom d'utilisation", config.get("user_name"), 20, TextField.ANY);
    regionField = new ChoiceGroup("Région", ChoiceGroup.POPUP, Entities.cercles_names(), null);

    int dis_index = 0;
    String my_dis = config.get("district_code");
    for (int i=0; i<districts.length;i++) {
        if (districts[i].equals(my_dis)) {
            dis_index = i;
            break;
        }
    }

    // profileField.setSelectedIndex(sel, true);
    regionField.setSelectedIndex(dis_index, true);

    System.out.println("Option From");
    append(numberField);
    append(regionField);
    append(user_nameField);

    addCommand(CMD_CONTINUE);
    addCommand(CMD_EXIT);
    addCommand(CMD_HELP);
    this.setCommandListener (this);
  }

    /*
     * Whether all required fields are filled
     * @return <code>true</code> is all fields are filled
     * <code>false</code> otherwise.
     */
    public boolean isComplete() {
        // all fields are required to be filled.
        if (numberField.getString().length() == 0) {
            return false;
        }
        if (user_nameField.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid() {
        if (numberField.getString().length() < 8) {
            ErrorMessage = "[Numéro du serveur] n'est pas un numéro n'est pas valide";
            return false;
        }
        return true;
    }

    public void commandAction(Command c, Displayable d) {
        // Help command displays Help Form
         if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "option");
            this.midlet.display.setCurrent(h);
        }

        // exit command goes back to Main Menu
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        if (c == CMD_CONTINUE) {
            districtField = new ChoiceGroup("Districts", ChoiceGroup.POPUP, Entities.communes_names(districts[regionField.getSelectedIndex()]), null);

            append(districtField);
            removeCommand(CMD_CONTINUE);
            addCommand(CMD_SAVE);
        }

        // save command stores new number in config or display errors.
        if (c == CMD_SAVE) {

            Alert alert;
            // check whether all fields have been completed
            // if not, we alert and don't do anything else.
            if (!this.isComplete()) {
                alert = new Alert("Données manquantes", "Tous les champs doivent être remplis!", null, AlertType.ERROR);
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


            String region_code = districts[regionField.getSelectedIndex()];
            String district_code = Entities.communes_codes(region_code)[districtField.getSelectedIndex()];

            if (config.set("server_number", numberField.getString()) &&
                    config.set("user_name", user_nameField.getString()) &&
                    config.set("region_code", region_code) &&
                    config.set("district_code", district_code)) {

                alert = new Alert ("Confirmation!", "Votre modification a bien été enregistrée.", null, AlertType.CONFIRMATION);
                this.midlet.startApp();
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
                alert = new Alert ("Échec", "Impossible d'enregistrer cette modification.", null, AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
