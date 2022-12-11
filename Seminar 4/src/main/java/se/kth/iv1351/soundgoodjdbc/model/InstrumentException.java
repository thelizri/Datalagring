package main.java.se.kth.iv1351.soundgoodjdbc.model;

import main.java.se.kth.iv1351.soundgoodjdbc.integration.SoundGoodDBException;

public class InstrumentException extends Exception {

    public InstrumentException(String failureMsg, Throwable e) {
        super(failureMsg, e);
    }

    public InstrumentException(String failureMsg) {
        super(failureMsg);
    }
}
