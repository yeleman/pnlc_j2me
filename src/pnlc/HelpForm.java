
package pnlc;

import javax.microedition.lcdui.*;
import pnlc.Configuration.*;
import pnlc.Constants.*;

/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author alou/fadiga
 */

public class HelpForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                        Command.BACK, 1);

    private StringItem helpText;
    PNLCMIDlet midlet;
    Displayable returnTo;


    public HelpForm(PNLCMIDlet midlet, Displayable d, String section) {
        super("Aide");
        this.midlet = midlet;
        this.returnTo = d;

        this.getContentFromSection(section);

        append(helpText);
        addCommand(CMD_EXIT);
        this.setCommandListener (this);
      }


    private void getContentFromSection(String section) {
        String text;
        if (section.equalsIgnoreCase("mainmenu")) {
            text = "Ouvrez le formulaire qui correspond à votre opération puis " +
                   "renseignez les champs et envoyez.\n" +
                   "Un SMS non envoyé est sauvegardé dans <<Renvoi form>>.";

        } else if (section.equalsIgnoreCase("version")) {
            String msg = "\n\n+ Chirurgie CT \n" +
                         "+ Formulaire resultat CAT \n" +
                         "+ Mise à jour des entités\n" +
                         "+ Formulaire TT fixe" +
                         "+ Chirurgie CAT fixe";
            text = "PNSO - Version " + Constants.version + msg;

        } else if (section.equalsIgnoreCase("Mission")) {
            text = "Ce formulaire permet de déclarer le début ou la fin de "+
                   " mission tout en precisant la date et la stratégie. \n" +
                   "Car chaque rapports doit être obligatoirement lié à une mission.";

        } else if (section.equalsIgnoreCase("visite_village")) {
            text = "Renseignez les champs et envoyez.";

        } else if (section.equalsIgnoreCase("option")) {
            text = "Changez le numéro du serveur uniquement sur " +
                   "demande expresse de " + Constants.HEAD_OF_MAINTENANCE +
                   "\nUn mauvais numéro vous empêchera de transmettre vos" +
                   " rapports.\n";

        } else if (section.equalsIgnoreCase("saved_reports")) {
            text = "Vous pouvez envoyer tous les SMS à la fois ou le faire un à un.";

        } else if (section.equalsIgnoreCase("result")) {
            text = "Id CT : C'est le code reçu du serveur après l'envoi d'un CAT.\n" +
                   "Le resultat une liste 1 à 10 désignant l'acuité visuelle";

        } else if (section.equalsIgnoreCase("visite_health_center")) {
            text = "Renseignez les champs et envoyez.\n"+
                   "Vous recevrez un SMS du serveur confirmant avec un Num. chirugie"+
                   "a noté sur le formulaire CAT.";

        } else if (section.equalsIgnoreCase("passwd")) {
            text = "Renseignez votre ancien mot de passe dans les champs adéquat.\n" +
                   "Ensuite, indiquez le nouveau mot de passe désiré. Celui-ci doit faire au moins 3 caractères.\n" +
                   "Vous recevrez un SMS du serveur confirmant ou non le changement de mot de passe.";

        } else {
            text = "Aucune aide disponible pour cet élément.";

        }

        helpText = new StringItem(null, text +
                                  "\n\nEn cas de problème, contactez " +
                                  Constants.HEAD_OF_MAINTENANCE + ".");
    }

    public void commandAction(Command c, Displayable d) {
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.returnTo);
        }
    }

}
