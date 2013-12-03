/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pnlc;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import pnlc.SMSStore.*;
import pnlc.StoredSMS.*;
import pnlc.OptionForm.*;
import pnlc.Constants.*;
import pnlc.Configuration.*;
import pnlc.MissionForm.*;
import pnlc.VisitVillageForm.*;
import pnlc.SendSavedReports.*;


/**
 * @author FAD
 */
public class PNLCMIDlet extends MIDlet implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Quitter", Command.EXIT, 1);
    private static final Command CMD_VERSION = new Command ("Version", Command.SCREEN, 2);
    private static final Command CMD_SRVNUM = new Command ("Configuration", Command.SCREEN, 4);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 5);

    public Display display;
    public List mainMenu;
    private Configuration config;

    public PNLCMIDlet() {
        display = Display.getDisplay(this);
    }
    public void startApp() {

        config = new Configuration();
        SMSStore store = new SMSStore();

        String[] mainMenu_ = {"Mission", "Formulaire TT",
                              "Renvoi form. (" + store.count() + ")"};

        if(config.get("user_name").equals("")){
            OptionForm f = new OptionForm(this);
            display.setCurrent(f);
        } else{
            mainMenu = new List("Formulaires", Choice.IMPLICIT, mainMenu_, null);
            // setup menu
            mainMenu.setCommandListener (this);
            mainMenu.addCommand (CMD_EXIT);
            mainMenu.addCommand (CMD_HELP);
            mainMenu.addCommand (CMD_VERSION);
            mainMenu.addCommand (CMD_SRVNUM);
            display.setCurrent(mainMenu);

        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {

        // if it originates from the MainMenu list
        if (s.equals (mainMenu)) {
            // and is a select command
            if (c == List.SELECT_COMMAND) {

                switch (((List) s).getSelectedIndex ()) {
                // Mark the beginning or end of a mission
                case 0:
                    MissionForm mission_form = new MissionForm(this);
                    display.setCurrent (mission_form);
                    break;
                // End of the visit of a village
                case 1:
                    VisitVillageForm visit_village_form = new VisitVillageForm(this);
                    display.setCurrent (visit_village_form);
                    break;
                // submit stored messages
                case 2:
                    SendSavedReports saved_reports = new SendSavedReports(this);
                    display.setCurrent (saved_reports);
                    break;
                }
            }
        }

        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this, this.mainMenu, "mainmenu");
            display.setCurrent(h);
        }

        // version command displays Help Form for "version"
        if (c == CMD_VERSION) {
            HelpForm v = new HelpForm(this, this.mainMenu, "version");
            display.setCurrent(v);
        }

        // srvnum command displays Edit Number Form.
        if (c == CMD_SRVNUM) {
            OptionForm f = new OptionForm(this);
            display.setCurrent(f);
        }

        // exit commands exits application completely.
        if (c == CMD_EXIT) {
            destroyApp(false);
            notifyDestroyed();
        }
    }
}