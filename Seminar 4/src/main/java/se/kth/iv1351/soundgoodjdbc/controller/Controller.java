package main.java.se.kth.iv1351.soundgoodjdbc.controller;

import main.java.se.kth.iv1351.soundgoodjdbc.integration.SoundGoodDAO;
import main.java.se.kth.iv1351.soundgoodjdbc.integration.SoundGoodDBException;
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

    public void listInstrument(String instrumentType) throws InstrumentException {
        try {
            soundGoodDb.listInstruments(instrumentType);
        } catch (Exception e) {
            throw new InstrumentException("Could not list instruments", e);
        }
    }

}
