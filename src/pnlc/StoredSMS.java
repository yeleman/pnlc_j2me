
package pnlc;

import javax.microedition.rms.*;
import java.io.*;
import pnlc.Constants.*;


public class StoredSMS {

    public String name;
    public String sms;
    public int storage_index;

    public StoredSMS() {
        this.name = "";
        this.sms = "";
        this.storage_index = -1;
    }
}