package main.java.se.kth.iv1351.soundgoodjdbc.startup;

import java.sql.*;


public class Main {
    public static void main(String[] args){
        Connection connection = null;

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SoundGood","postgres","password");
            if(connection != null) System.out.println("Connection ok");
        }catch (Exception e){
           e.printStackTrace();
        }
    }
}
