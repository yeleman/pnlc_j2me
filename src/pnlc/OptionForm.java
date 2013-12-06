
package pnlc;

import javax.microedition.lcdui.*;
import pnlc.Configuration.*;
import pnlc.Constants.*;
import pnlc.HelpForm.*;
import snisi.entities.Utils.*;

/**
 * J2ME Form allowing Server number, user_name region and district.
 * @author Fad
 */

public class OptionForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Enreg.", Command.OK, 2);
    private static final Command CMD_CONTINUE = new Command ("Continuer", Command.HELP, 3);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 4);

    PNLCMIDlet midlet;
    //Displayable returnTo;
    private String ErrorMessage = "";

    private Configuration config;
    private String[] regions;

    private static final String[] TypeOp = {"AMO", "TSO", "OPT"};
    private TextField numberField;
    private TextField user_nameField;
    //choice
    private ChoiceGroup regionField;
    private ChoiceGroup districtField;
    private ChoiceGroup operator_typeField;
    String last_region;


public OptionForm(PNLCMIDlet midlet) {
    super("Paramètres de transmission");
    this.midlet = midlet;

    config = new Configuration();
    regions = snisi.entities.Utils.regions_codes();

    // retrieve phone number from config
    // if not present, use constant
    String phone_number = "";
    phone_number = config.get("server_number");
    if (phone_number.equals("")) {
        phone_number = Constants.server_number;
    }

    int op_index = 0;
    String my_type_op = config.get("operator_type");
    for (int i = 0; i<TypeOp.length ; i++) {
        if (TypeOp[i].equals(my_type_op)) {
            op_index = i;
            break;
        }
    }

    numberField = new TextField ("Numéro du serveur:", phone_number, 8, TextField.PHONENUMBER);
    user_nameField = new TextField("Votre Identifiant", config.get("user_name"), 20, TextField.ANY);
    regionField = new ChoiceGroup("Votre Région", ChoiceGroup.POPUP, snisi.entities.Utils.regions_names(), null);
    regionField.setSelectedIndex(Integer.parseInt(config.get("last_region")), true);
    operator_typeField = new ChoiceGroup("Votre Profil", ChoiceGroup.POPUP, TypeOp, null);
    operator_typeField.setSelectedIndex(op_index, true);

    append(numberField);
    append(user_nameField);
    append(operator_typeField);
    append(regionField);

    addCommand(CMD_CONTINUE);
    addCommand(CMD_EXIT);
    addCommand(CMD_HELP);

    this.setCommandListener(this);
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
            ErrorMessage = "[Numéro du serveur] n'est pas un numéro valide";
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
            return;
        }

        if (c == CMD_CONTINUE) {

            String last_region_index = String.valueOf(regionField.getSelectedIndex());
            config.set("last_region", last_region_index);

            int dis_index = 0;
            String my_dis = config.get("district_code");
            for (int i=0; i<regions.length;i++) {
                if (regions[i].equals(my_dis)) {
                    dis_index = i;
                    break;
                }
            }

            districtField = new ChoiceGroup("Votre District", ChoiceGroup.POPUP,
                                            snisi.entities.Utils.districts_names(regions[regionField.getSelectedIndex()]), null);
            districtField.setSelectedIndex(dis_index, true);
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

            String region_code = regions[regionField.getSelectedIndex()];
            String district_code = snisi.entities.Utils.districts_codes(region_code)[districtField.getSelectedIndex()];

            if (config.set("server_number", numberField.getString()) &&
                    config.set("user_name", user_nameField.getString()) &&
                    config.set("region_code", region_code) &&
                    config.set("operator_type", operator_typeField.getString(operator_typeField.getSelectedIndex())) &&
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
