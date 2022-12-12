package main.java.se.kth.iv1351.soundgoodjdbc.integration;

import main.java.se.kth.iv1351.soundgoodjdbc.model.Instrument;
import main.java.se.kth.iv1351.soundgoodjdbc.model.InstrumentDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This data access object (DAO) encapsulates all database calls in the bank
 * application. No code outside this class shall have any knowledge about the
 * database.
 */
public class SoundGoodDAO {

    private static final String INSTRUMENT_TABLE_NAME = "physical_instruments";
    private static final String INSTRUMENT_PK_COLUMN_NAME = "database_id";
    private static final String INSTRUMENT_ID_COLUMN_NAME = "instrument_id";
    private static final String INSTRUMENT_TYPE_COLUMN_NAME = "instrument_type";
    private static final String INSTRUMENT_BRAND_COLUMN_NAME = "brand";
    private static final String INSTRUMENT_PRICE_COLUMN_NAME = "price";

    private static final String RENT_INSTRUMENT_TABLE_NAME = "rented_instrument";
    private static final String RENT_INSTRUMENT_RECEIPT_ID_COLUMN_NAME = "receipt_id";
    private static final String RENT_INSTRUMENT_START_DATE_COLUMN_NAME = "start_date";
    private static final String RENT_INSTRUMENT_END_DATE_COLUMN_NAME = "end_date";
    private static final String RENT_INSTRUMENT_FK_PK_STUDENT_COLUMN_NAME = "student_db_id";
    private static final String RENT_INSTRUMENT_FK_PK_INSTRUMENT_COLUMN_NAME = "instrument_db_id";

    private static final String STUDENT_TABLE_NAME = "student";
    private static final String STUDENT_TABLE_PK_COLUMN_NAME = "database_id";
    private static final String STUDENT_TABLE_PERSONALNUMBER_COLUMN_NAME = "personal_number";
    private static final String STUDENT_TABLE_FULLNAME_COLUMN_NAME = "full_name";
    private static final String STUDENT_TABLE_EMAIL_COLUMN_NAME = "email";
    private static final String STUDENT_TABLE_STUDENT_ID_COLUMN_NAME = "student_id";


    private Connection connection;
    private PreparedStatement listInstruments;
    private PreparedStatement rentInstrument;
    private PreparedStatement terminateRental;

    public SoundGoodDAO() throws SoundGoodDBException {
        try {
            connectToDB();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException exception) {
            throw new SoundGoodDBException("Could not connect to datasource.", exception);
        }
    }


    private void prepareStatements() throws SQLException{
        listInstruments = connection.prepareStatement("SELECT INSTRUMENT_ID,\n" +
                "\tBRAND,\n" +
                "\tPRICE,\n" +
                "\tINSTRUMENT_TYPE\n" +
                "FROM PHYSICAL_INSTRUMENTS\n" +
                "LEFT JOIN RENTED_INSTRUMENT ON PHYSICAL_INSTRUMENTS.DATABASE_ID = RENTED_INSTRUMENT.INSTRUMENT_DB_ID\n" +
                "WHERE RENTED_INSTRUMENT.INSTRUMENT_DB_ID IS NULL and instrument_type = ?");
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

    public void handleException(String failureMessage, Exception exception) throws SoundGoodDBException {
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

    private void closeResultSet(String failureMessage, ResultSet result) throws SoundGoodDBException {
        try {
            result.close();
        } catch (Exception exception) {
            throw new SoundGoodDBException(failureMessage + " Could not close result set.", exception);
        }
    }

    public void listInstruments(String type) throws SoundGoodDBException {

        List<Instrument> instruments = null;
        try{
            listInstruments.setString(1, type);
            ResultSet rs = listInstruments.executeQuery();
            instruments = new ArrayList<Instrument>();
            while (rs.next()) {
                System.out.println(rs.getString(INSTRUMENT_ID_COLUMN_NAME)+" "+rs.getString(INSTRUMENT_TYPE_COLUMN_NAME)+" "
                        +rs.getString(INSTRUMENT_BRAND_COLUMN_NAME)+" "+rs.getInt(INSTRUMENT_PRICE_COLUMN_NAME));
                /*
                instruments.add(new Instrument(rs.getInt(INSTRUMENT_PK_COLUMN_NAME),
                        rs.getString(INSTRUMENT_ID_COLUMN_NAME),
                        rs.getString(INSTRUMENT_TYPE_COLUMN_NAME),
                        rs.getString(INSTRUMENT_BRAND_COLUMN_NAME),
                        rs.getInt(INSTRUMENT_PRICE_COLUMN_NAME)
                ));*/
            }
            rs.close();
        }catch(Exception exception){
            exception.printStackTrace();
            handleException("Could not list instruments.", exception);
        }
        //return null;
    }
}
