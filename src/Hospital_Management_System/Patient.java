package Hospital_Management_System;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    public  Patient(Connection connection , Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.print("Enter Name : ");
        String name = scanner.next();
        System.out.print("Enter Age : ");
        int age = scanner.nextInt();
        System.out.print("Enter Gender : ");
        String gender = scanner.next();


        try{
            String query = "INSERT INTO patients(Name,Age,Gender) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);

            int rowAffected = preparedStatement.executeUpdate();

            if(rowAffected > 0)
            {
                System.out.println("Insert Patient Successfully.....");
            }
            else {
                System.out.println("Failed to Insert Patient....");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }

    public void viewPatient(){
        String query = "SELECT * FROM patients;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                int age = resultSet.getInt("Age");
                String gender = resultSet.getString("Gender");

                System.out.println("-----------------------------------------------------------");
                System.out.println("ID : " + id);
                System.out.println("Name : " + name);
                System.out.println("Age : " + age);
                System.out.println("Gender : " + gender);
                System.out.println("-----------------------------------------------------------");
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientByID(int id){
        String query = "SELECT * FROM patients WHERE ID = ?;";

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
