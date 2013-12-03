
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
                   "Un SMS non envoyé est sauvegardé dans <<Envoi form>>.\n" +
                   "En cas de problème, contactez l'ANTIM.";
        } else if (section.equalsIgnoreCase("epid")) {
            text = "Renseignez uniquement les cas d'épidémie et de décès pour chaque maladie suivie.\n" +
                   "En cas de problème, contactez l'ANTIM.";
        } else {
            text = "Aucune aide disponible pour cet élément.";
        }
        helpText = new StringItem(null, text);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.returnTo);
        }
    }

}
