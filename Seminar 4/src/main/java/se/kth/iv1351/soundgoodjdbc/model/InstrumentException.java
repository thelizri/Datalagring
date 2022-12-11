package main.java.se.kth.iv1351.soundgoodjdbc.model;

import main.java.se.kth.iv1351.soundgoodjdbc.integration.SoundGoodDBException;

public class InstrumentException extends Exception {

    public InstrumentException(String failureMsg, SoundGoodDBException e) {
        super(failureMsg, e);
    }
}
