package main.java.se.kth.iv1351.soundgoodjdbc.controller;

import main.java.se.kth.iv1351.soundgoodjdbc.integration.SoundGoodDAO;
import main.java.se.kth.iv1351.soundgoodjdbc.integration.SoundGoodDBException;
import main.java.se.kth.iv1351.soundgoodjdbc.model.Instrument;
import main.java.se.kth.iv1351.soundgoodjdbc.model.InstrumentDTO;
import main.java.se.kth.iv1351.soundgoodjdbc.model.InstrumentException;

import java.util.List;

/**
 * This is the application's only controller, all calls to the model pass here.
 * The controller is also responsible for calling the DAO. Typically, the
 * controller first calls the DAO to retrieve data (if needed), then operates on
 * the data, and finally tells the DAO to store the updated data (if any).
 */
public class Controller {

    private final SoundGoodDAO soundGoodDb;

    /**
     * Creates a new instance, and retrieves a connection to the database.
     *
     * @throws SoundGoodDBException If unable to connect to the database.
     */
    public Controller() throws SoundGoodDBException {
        soundGoodDb = new SoundGoodDAO();
    }

    private void commitOngoingTransaction(String failureMsg) throws InstrumentException {
        try {
            soundGoodDb.commit();
        } catch (SoundGoodDBException e) {
            throw new InstrumentException(failureMsg, e);
        }
    }

    public List<? extends InstrumentDTO> listInstrument(String instrumentType) throws InstrumentException {
        try {
            return soundGoodDb.listInstruments(instrumentType);
        } catch (Exception e) {
            throw new InstrumentException("Could not list instruments", e);
        }
    }

    public String rentInstrument(String studentPersonalNumber, String instrumentProductID) throws InstrumentException{
        try{
            int amountOfRentals = soundGoodDb.getAmountOfRentalsByStudent(studentPersonalNumber);
            if(amountOfRentals>=2){
                return "You already have 2 active rentals under your name.";
            }
            System.out.println(amountOfRentals);
            //Get database id of student
            int studentDbID = soundGoodDb.getStudentDatabaseID(studentPersonalNumber);
            System.out.println(studentDbID);

        } catch(Exception e){
            throw new InstrumentException("Could not rent an instrument", e);
        }
        return "Success";
    }

}
