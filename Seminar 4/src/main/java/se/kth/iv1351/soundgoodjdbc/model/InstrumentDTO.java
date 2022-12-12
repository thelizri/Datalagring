package main.java.se.kth.iv1351.soundgoodjdbc.model;

/**
 * InstrumentDTO represents an instrument in the database.
 */
public interface InstrumentDTO {

    public String getType();

    public String getBrand();

    public String getInstrumentId();

    public int getPrice();

    public String toString();
}
