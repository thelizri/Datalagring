package main.java.se.kth.iv1351.soundgoodjdbc.model;

/**
 * Represent an instance of an instrument from the SoundGood database
 */
public class Instrument implements InstrumentDTO {
    private int databaseId;
    private String instrumentId;
    private String type;
    private String brand;
    private int price;

    /**
     * Creates a new instance of the instrument.
     * @param databaseId the database id of the instrument
     * @param instrumentId the id of the instrument.
     * @param type the type of the instrument.
     * @param brand the brand of the instrument.
     * @param price the price of the instrument.
     */
    public Instrument(int databaseId, String instrumentId, String type, String brand, int price) {
        this.databaseId = databaseId;
        this.instrumentId = instrumentId;
        this.type = type;
        this.brand = brand;
        this.price = price;
    }

    /**
     * Gets the instrument id of the instrument.
     *
     * @return the instrument id of the instrument.
     */
    public String getInstrumentId() {
        return instrumentId;
    }

    /**
     * Gets the type of the instrument.
     *
     * @return the type of the instrument.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the brand of the instrument.
     *
     * @return the brand of the instrument.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Gets the price of the instrument.
     *
     * @return the price of the instrument.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets a string regarding this instance.
     *
     * @return The attributes of this instance in form of a string.
     */
    @Override
    public String toString() {
        return "Instrument{" +
                "brand='" + brand + '\'' +
                ", price=" + price + '\'' +
                ", instrumentID='" + instrumentId + '\'' +
                '}';
    }
}
