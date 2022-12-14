package main.java.se.kth.iv1351.soundgoodjdbc.model;

import main.java.se.kth.iv1351.soundgoodjdbc.integration.SoundGoodDBException;

public class InstrumentException extends Exception {

    /**
     * Create a new instance thrown because of the specified reason and exception.
     *
     * @param failureMsg    Why the exception was thrown.
     * @param cause The exception that caused this exception to be thrown.
     */
    public InstrumentException(String failureMsg, Throwable cause) {
        super(failureMsg, cause);
    }

    /**
     * Create a new instance thrown because of the specified reason.
     *
     * @param failureMsg Why the exception was thrown.
     */
    public InstrumentException(String failureMsg) {
        super(failureMsg);
    }
}
