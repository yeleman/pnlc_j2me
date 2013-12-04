
package pnlc;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Font;
import pnlc.PNLCMIDlet.*;
import pnlc.Configuration.*;
import pnlc.SMSSender.*;

/**
 * J2ME Form allowing user to change password via SMS
 * Requests username, old password and username
 * then forges and sends an SMS to server.
 * @author FAD
 */
public class ChangePasswordForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SEND = new Command ("Envoyer", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);

    private PNLCMIDlet midlet;

    // private TextField usernameField;
    private TextField oldpasswordField;
    private TextField newpasswordField;

    private Configuration config;
    String username = "";

    public ChangePasswordForm(PNLCMIDlet midlet) {
        super("Changement de mot de passe");
        this.midlet = midlet;

        config = new Configuration();


        username = config.get("user_name");

        // usernameField = new TextField ("Identifiant:", username, 8, TextField.PLAIN);
        oldpasswordField = new TextField ("Ancien mot de passe", "", Constants.password_max_length, TextField.ANY);
        newpasswordField = new TextField ("Nouveau mot de passe", "", Constants.password_max_length, TextField.ANY);

        append("Identifiant: " + username);
        // append(usernameField);
        append(oldpasswordField);
        append(newpasswordField);

        addCommand(CMD_EXIT);
        addCommand(CMD_SEND);
        addCommand(CMD_HELP);
        this.setCommandListener (this);
    }

    /*
     *
     * @return <code>true</code> if fields are properly field for sending
     * <code>false</code> otherwise.
     */
    public boolean canSubmit() {
        if (oldpasswordField.getString().length() >= Constants.password_min_length &&
            //usernameField.getString().length() >= Constants.username_min_length &&
            newpasswordField.getString().length() >= Constants.password_min_length) {
            return true;
        }
        return false;
    }

    /*
     * formats request of password change as SMS text
     * @return SMS text to be sent to request password change
     */
    public String toSMSFormat() {
        String sep = " ";
        return "tt passwd" + sep + username
                           + sep + oldpasswordField.getString()
                           + sep + newpasswordField.getString();
    }

    public void commandAction(Command c, Displayable d) {

        // Help command displays Help Form
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "passwd");
            this.midlet.display.setCurrent(h);
        }

        // Exit command goes back to Main menu.
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        // Send command builds and send SMS or displays errors.
        if (c == CMD_SEND) {
            Alert alert;

            if (!canSubmit()) {
                alert = new Alert ("Informations incorrectes.", "L'identifiant ou les mots de passe ne sont pas renseignés correctement.", null, AlertType.ERROR);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            SMSSender sms = new SMSSender();
            String number = config.get("server_number");

            if (sms.send(number, this.toSMSFormat())) {
                alert = new Alert ("Demande envoyée !", "Vous allez recevoir une confirmation du serveur.", null, AlertType.CONFIRMATION);
            } else {
                alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer la demande par SMS.", null, AlertType.WARNING);
            }

            // save username
            // config.set("username", usernameField.getString());

            this.midlet.display.setCurrent (alert, this);
        }
    }
}
