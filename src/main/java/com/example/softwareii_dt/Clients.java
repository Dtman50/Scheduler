package com.example.softwareii_dt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author dariustaylor
 */
public class Clients {
     public static ObservableList<Record> allClients = FXCollections.observableArrayList();

    /**
     *
     * @param recordID look up by record id
     * @return the record id or null if there is no match
     */
    public static Record lookupRecord(int recordID) {

        for (Record record : allClients) {
            if(record.getId() == recordID) {
                return record;
            }
        }
        return null;
    }

    /**
     *
     * @param recordName look up by record name
     * @return a list of the names of the records
     */
    public static ObservableList<Record> lookupRecord(String recordName) {
        ObservableList<Record> found = FXCollections.observableArrayList();
        for (Record record : allClients) {
            if(record.getName().contains(recordName)) {
                found.add(record);
            }
        }
        return found;
    }

    /**
     *  First clears the list so that there isn't duplicate data. Adds both the Basic Records
     *  and Premium records separately into one list.
     *
     * @return the combined list of both records
     */
    public static ObservableList<Record> getAllRecords() {
        allClients.clear();
        allClients.addAll(BasicRecord.getAllBasicRecords());
        allClients.addAll(PremiumRecord.getAllPremiumRecords());

        return allClients;
    }
}
