package pnlc;

import javax.microedition.rms.*;
import java.io.*;
import pnlc.Constants.*;

/**
 * Store defined values into RMS
 * @author fadiga
 */

public class Configuration {

    // index 0 is invalid
    private int server_number_index = 1;
    private int user_name_index = 2;
    private int district_code_index = 3;
    private int region_code_index = 4;
    private int operator_type_index = 5;
    private int last_health_center_index = 6;
    private int last_region_index = 7;

    private static final String database = "configuration";
    private RecordStore recordstore = null;

    public Configuration() {
        RecordEnumeration recordEnumeration = null;
        try {
            recordstore = RecordStore.openRecordStore(Configuration.database, true);
            recordEnumeration = recordstore.enumerateRecords(null, null, false);
            recordstore.closeRecordStore();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (recordEnumeration.numRecords() < 5) {
            // the following has to be in order of indexes.
            // Todo change les valeurs par defaut pour
            this.set("server_number", Constants.server_number, true);
            this.set("user_name", "", true);
            this.set("region_code", "", true);
            this.set("district_code", "", true);
            this.set("operator_type", "", true);
            this.set("last_health_center", "0", true);
            this.set("last_region", "0", true);
        }
    }

    /*
     * Fetch value from DB
     * @param variable the slug of the data to retrieve
     * @return a <code>String</code> of the stored data
     */
    public String get(String variable) {

        String value = "";

        int index = this.index_for(variable);
        if (index < 0) {
            return value;
        }

        try {
        // open record store
        recordstore = RecordStore.openRecordStore(Configuration.database, true);

        // record is internally a byte array
        byte[] byteInputData = new byte[1024];

        // we'll retrieve data in a stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteInputData);
        DataInputStream inputDataStream = new DataInputStream(inputStream);

        // actually retrieve data
        recordstore.getRecord(index, byteInputData, 0);

        value = inputDataStream.readUTF();

        // close streams
        inputStream.reset();
        inputStream.close();
        inputDataStream.close();

        // close connection
        recordstore.closeRecordStore();
      }
      catch (Exception error)
      {
          return value;
      }
      return value;
    }

    /*
     * the index of a variable in the RMS record
     * @param variable the slug of the data to query
     * @return the index
     */
    private int index_for(String variable) {
        int index;
        if (variable.equals("server_number")) {
            index = server_number_index;
        } else if (variable.equals("user_name")) {
            index = user_name_index;
        } else if (variable.equals("district_code")) {
            index = district_code_index;
        } else if (variable.equals("region_code")) {
            index = region_code_index;
        } else if (variable.equals("operator_type")) {
            index = operator_type_index;
        } else if (variable.equals("last_health_center")) {
            index = last_health_center_index;
        } else if (variable.equals("last_region")) {
            index = last_region_index;
        } else {
            index = -1;
        }
        return index;
    }

    /*
     * Stores a value associated with a variable in the DB
     * @param variable the slug of the data
     * @param value the actual <code>String</code> value
     * @param add whether to add (<code>true</code>) or to update DB row
     */
    public boolean set(String variable, String value, boolean add) {

        int index = this.index_for(variable);
        if (index < 0) {
            return false;
        }

        try
        {
        // open record store
        recordstore = RecordStore.openRecordStore(Configuration.database, true );

        // record is internaly a byte array
        byte[] outputRecord;

        // store all data in a stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream outputDataStream = new DataOutputStream(outputStream);

        // add all fields to the stream
        outputDataStream.writeUTF(value);

        // finish preparing stream
        outputDataStream.flush();
        outputRecord = outputStream.toByteArray();

        // actual record storage
        if (add) {
            recordstore.addRecord(outputRecord, 0, outputRecord.length);
        } else {
            recordstore.setRecord(index, outputRecord, 0, outputRecord.length);
        }

        // close stream
        outputStream.reset();
        outputStream.close();
        outputDataStream.close();

        // close connection
        recordstore.closeRecordStore();
      }
      catch ( Exception error)
      {
          return false;
      }
      return true;
    }

    /*
     * Overrides <code>set()</code> to save the <code>add</code> param.
     * @see <code>set()</code>
     */
    public boolean set(String variable, String value) {
        return set(variable, value, false);
    }
}
