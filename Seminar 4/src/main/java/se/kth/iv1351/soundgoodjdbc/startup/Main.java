package main.java.se.kth.iv1351.soundgoodjdbc.startup;

import main.java.se.kth.iv1351.soundgoodjdbc.controller.Controller;
import main.java.se.kth.iv1351.soundgoodjdbc.integration.SoundGoodDAO;
import main.java.se.kth.iv1351.soundgoodjdbc.integration.SoundGoodDBException;
import main.java.se.kth.iv1351.soundgoodjdbc.view.BlockingInterpreter;

import java.sql.*;


public class Main {
    /**
     * @param args There are no command line arguments.
     */
    public static void main(String[] args) {
        try {
            SoundGoodDAO sg = new SoundGoodDAO();
            sg.listInstruments("guitar");
            new BlockingInterpreter(new Controller()).handleCmds();
        } catch(Exception e) {
            System.out.println("Could not connect to SoundGood");
            e.printStackTrace();
        }
    }
}
