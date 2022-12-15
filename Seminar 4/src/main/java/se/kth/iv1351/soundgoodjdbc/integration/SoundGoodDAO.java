package main.java.se.kth.iv1351.soundgoodjdbc.integration;

import main.java.se.kth.iv1351.soundgoodjdbc.model.Instrument;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This data access object (DAO) encapsulates all database calls in the bank
 * application. No code outside this class shall have any knowledge about the
 * database.
 */
public class SoundGoodDAO {

    private Connection connection;
    private PreparedStatement readAvailableInstruments;
    private PreparedStatement readRentalsByStudent;
    private PreparedStatement readDatabaseIDofStudent;
    private PreparedStatement readDatabaseIDofInstrument;
    private PreparedStatement createRentalInstrument;
    private PreparedStatement updateEndDateRental;

    /**
     * Creates new instance of the SoundGood Database Access Object
     * @throws SoundGoodDBException
     */
    public SoundGoodDAO() throws SoundGoodDBException {
        try {
            connectToDB();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException exception) {
            throw new SoundGoodDBException("Could not connect to datasource.", exception);
        }
    }


    private void prepareStatements() throws SQLException{
        readAvailableInstruments = connection.prepareStatement("SELECT PHYSICAL_INSTRUMENTS.DATABASE_ID,\n" +
                "\tPHYSICAL_INSTRUMENTS.INSTRUMENT_ID,\n" +
                "\tPHYSICAL_INSTRUMENTS.BRAND,\n" +
                "\tPHYSICAL_INSTRUMENTS.PRICE,\n" +
                "\tPHYSICAL_INSTRUMENTS.INSTRUMENT_TYPE\n" +
                "FROM PHYSICAL_INSTRUMENTS\n" +
                "LEFT JOIN\n" +
                "\t(SELECT DISTINCT *\n" +
                "\t\tFROM PHYSICAL_INSTRUMENTS\n" +
                "\t\tINNER JOIN RENTED_INSTRUMENT ON PHYSICAL_INSTRUMENTS.DATABASE_ID = RENTED_INSTRUMENT.INSTRUMENT_DB_ID\n" +
                "\t\tWHERE END_DATE IS NULL) AS FOO ON PHYSICAL_INSTRUMENTS.DATABASE_ID = FOO.DATABASE_ID\n" +
                "WHERE FOO.DATABASE_ID IS NULL AND PHYSICAL_INSTRUMENTS.INSTRUMENT_TYPE = ?\n" +
                "ORDER BY PHYSICAL_INSTRUMENTS.INSTRUMENT_TYPE, PHYSICAL_INSTRUMENTS.PRICE");

        readRentalsByStudent = connection.prepareStatement("SELECT (COUNT(*)-1) AS \"COUNT\", STUDENT_DB_ID, PERSONAL_NUMBER\n" +
                "FROM RENTED_INSTRUMENT\n" +
                "RIGHT JOIN STUDENT ON RENTED_INSTRUMENT.STUDENT_DB_ID = STUDENT.DATABASE_ID\n" +
                "WHERE PERSONAL_NUMBER = ?\n" +
                "GROUP BY STUDENT_DB_ID, PERSONAL_NUMBER\n" +
                "ORDER BY STUDENT_DB_ID");

        readDatabaseIDofStudent = connection.prepareStatement("SELECT DATABASE_ID FROM STUDENT\n" +
                "WHERE PERSONAL_NUMBER = ?");
        readDatabaseIDofInstrument = connection.prepareStatement("SELECT DATABASE_ID FROM PHYSICAL_INSTRUMENTS\n" +
                "WHERE INSTRUMENT_ID = ?");
        createRentalInstrument = connection.prepareStatement("INSERT INTO RENTED_INSTRUMENT(STUDENT_DB_ID, INSTRUMENT_DB_ID, START_DATE, RECEIPT_ID)\n" +
                "VALUES (?,?, CURRENT_DATE, ?)");

        updateEndDateRental = connection.prepareStatement("UPDATE RENTED_INSTRUMENT\n" +
                "SET END_DATE = CURRENT_DATE\n" +
                "WHERE RECEIPT_ID = ?");
    }

    /**
     * Adds the current date as end date to a rental with specific receipt ID
     * @param receiptID
     * @throws SoundGoodDBException
     */
    public void updateEndRental(String receiptID) throws SoundGoodDBException{
        try{
            updateEndDateRental.setString(1,receiptID);
            updateEndDateRental.executeUpdate();
        }catch(Exception exception) {
            handleException("Could not terminate rental", exception);
        }
    }

    private void connectToDB() throws ClassNotFoundException, SQLException {
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SoundGood","postgres","password");
            connection.setAutoCommit(false);
        }catch(ClassNotFoundException | SQLException exception){
            exception.printStackTrace();
        }
    }

    /**
     * Commits the current transaction.
     *
     * @throws SoundGoodDBException If unable to commit the current transaction.
     */
    public void commit() throws SoundGoodDBException {
        try {
            connection.commit();
        } catch (SQLException e) {
            handleException("Failed to commit", e);
        }
    }

    private void handleException(String failureMessage, Exception exception) throws SoundGoodDBException {
        String completeFailureMessage = failureMessage;
        try {
            connection.rollback();
        } catch (SQLException rollbackException) {
            completeFailureMessage +=
                    ". Also failed to rollback transaction because of: " + rollbackException.getMessage();
        }

        if (exception != null) {
            throw new SoundGoodDBException(failureMessage, exception);
        } else {
            throw new SoundGoodDBException(failureMessage);
        }
    }

    /**
     * Retrieves the amount of active rentals listed under student's account
     * @param studentPersonalNumber The student's personal number
     * @return The amount of rentals
     * @throws SoundGoodDBException throws if student doesn't exist
     */
    public int readCurrentAmountOfRentalsHeldByStudent(String studentPersonalNumber) throws SoundGoodDBException{
        try{
            readRentalsByStudent.setString(1,studentPersonalNumber);
            ResultSet rs = readRentalsByStudent.executeQuery();
            int result = -1;

            if(rs.next()){
                result= rs.getInt("COUNT");
            }else{
                handleException("Student does not exist", null);
            }
            rs.close();
            return result;
        }catch(Exception exception){
            handleException("Could not get amount of rentals by student.", exception);
        }
        return -1;
    }


    /**
     * Gets the database id of the student
     * @param studentPersonalNumber student's personal number
     * @return the database id of the student
     * @throws SoundGoodDBException if student doesn't exist
     */
    public int readStudentDatabaseId(String studentPersonalNumber) throws SoundGoodDBException{
        try{
            readDatabaseIDofStudent.setString(1,studentPersonalNumber);
            ResultSet rs = readDatabaseIDofStudent.executeQuery();
            if(rs.next()){
                return rs.getInt("DATABASE_ID");
            }else{
                handleException("Student does not exist", null);
            }
            rs.close();
        }catch(Exception exception){
            handleException("Could not retrieve database id of student", exception);
        }
        return -1;
    }

    /**
     * Gets the database id of the instrument
     * @param instrumentProductID the product if of the instrument
     * @return database id of the instrument
     * @throws SoundGoodDBException if instrument doesn't exist
     */
    public int readInstrumentDatabaseID(String instrumentProductID) throws SoundGoodDBException{
        try{
            readDatabaseIDofInstrument.setString(1,instrumentProductID);
            ResultSet rs = readDatabaseIDofInstrument.executeQuery();
            if(rs.next()){
                return rs.getInt("DATABASE_ID");
            }else{
                handleException("Instrument does not exist", null);
            }
            rs.close();
        }catch(Exception exception){
            handleException("Could not retrieve database id of student.", exception);
        }
        return -1;
    }

    /**
     * Inserts a new rental into the table with rentals
     * @param studentDbID student's database id
     * @param instrumentDbID instrument's database id
     * @param receiptID the receipt id
     * @throws SoundGoodDBException if unable to rent the instrument
     */
    public void createRentalOfInstrument(int studentDbID, int instrumentDbID, String receiptID) throws SoundGoodDBException{
        try{
            createRentalInstrument.setInt(1, studentDbID);
            createRentalInstrument.setInt(2,instrumentDbID);
            createRentalInstrument.setString(3,receiptID);
            createRentalInstrument.executeUpdate();
        }catch(Exception exception){
            handleException("Could not rent instrument.", exception);
        }
    }

    /**
     * Returns a list with instruments that are available for rental
     * @param type The type of the instrument: 'Guitar', or 'Piano'
     * @return The list with available instruments
     * @throws SoundGoodDBException if unable to retrieve the list
     */
    public List<Instrument> readAvailableInstruments(String type) throws SoundGoodDBException {

        List<Instrument> instruments = null;
        try{
            readAvailableInstruments.setString(1, type);
            ResultSet rs = readAvailableInstruments.executeQuery();
            instruments = new ArrayList<Instrument>();
            while (rs.next()) {
                instruments.add(new Instrument(
                        rs.getString("INSTRUMENT_ID"),
                        rs.getString("INSTRUMENT_TYPE"),
                        rs.getString("BRAND"),
                        rs.getInt("PRICE")
                ));
            }
            rs.close();
        }catch(Exception exception){
            handleException("Could not list instruments.", exception);
        }
        return instruments;
    }
}
