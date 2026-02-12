package Hospital_Management_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {

    private Connection connection;

    private Doctor(Connection connection) {
        this.connection = connection;
    }


    public void viewDoctors(){
        String query = "SELECT * FROM doctors;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                String Specialization = resultSet.getString("Specialization");

                System.out.println("-----------------------------------------------------------");
                System.out.println("ID : " + id);
                System.out.println("Name : " + name);
                System.out.println("Specialization : " + Specialization);
                System.out.println("-----------------------------------------------------------");
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorByID(int id){
        String query = "SELECT * FROM doctors WHERE ID = ?;";

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }else{
                return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
