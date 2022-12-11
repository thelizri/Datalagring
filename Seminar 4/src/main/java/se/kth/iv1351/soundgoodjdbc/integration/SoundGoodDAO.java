package main.java.se.kth.iv1351.soundgoodjdbc.integration;

import java.sql.*;

/**
 * This data access object (DAO) encapsulates all database calls in the bank
 * application. No code outside this class shall have any knowledge about the
 * database.
 */
public class SoundGoodDAO {

    private Connection connection;

    public SoundGoodDAO() throws SoundGoodDBException {
        try {
            connectToBankDB();
            //prepareStatements();
        } catch (ClassNotFoundException | SQLException exception) {
            throw new SoundGoodDBException("Could not connect to datasource.", exception);
        }
    }


    private void connectToBankDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SoundGood","postgres","password");
        connection.setAutoCommit(false);
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

    private void handleException(String failureMsg, Exception cause) throws SoundGoodDBException {
        String completeFailureMsg = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException rollbackExc) {
            completeFailureMsg = completeFailureMsg +
                    ". Also failed to rollback transaction because of: " + rollbackExc.getMessage();
        }

        if (cause != null) {
            throw new SoundGoodDBException(failureMsg, cause);
        } else {
            throw new SoundGoodDBException(failureMsg);
        }
    }

    private void closeResultSet(String failureMsg, ResultSet result) throws SoundGoodDBException {
        try {
            result.close();
        } catch (Exception e) {
            throw new SoundGoodDBException(failureMsg + " Could not close result set.", e);
        }
    }
}
