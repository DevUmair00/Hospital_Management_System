package Hospital_Management_System;

import com.mysql.cj.MessageBuilder;
import com.mysql.cj.exceptions.DataReadException;

import java.lang.module.ResolutionException;
import java.sql.*;
import java.util.Scanner;

public class Hospital_Management_System {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/hospital_management_System";
    private static final String username = "root";
    private static final String password = "0000";


    public static void main(String[] args) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try{
            Connection connection = DriverManager.getConnection(url,username,password);

            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);

            while(true)
            {
                clearScreen();
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient.");
                System.out.println("2. View Patient.");
                System.out.println("3. View Doctor.");
                System.out.println("4. Book Appointment.");
                System.out.println("5. Exit.");
                System.out.print("Enter your Choice : ");
                int choice = scanner.nextInt();
                clearScreen();

                switch (choice)
                {
                    case 1:
                        patient.addPatient();
                        pressEnterToContinue(scanner);
                        break;
                    case 2:
                        patient.viewPatient();
                        pressEnterToContinue(scanner);
                        break;
                    case 3:
                        doctor.viewDoctors();
                        pressEnterToContinue(scanner);
                        break;
                    case 4:
                        bookAppointment(connection, scanner, doctor, patient );
                        pressEnterToContinue(scanner);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid Choice.");
                        pressEnterToContinue(scanner);
                        break;
                }

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Connection connection, Scanner scanner , Doctor doctor , Patient patient)
    {
        System.out.print("Enter Patient ID : ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor ID : ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD) : ");
        String appointmentDate = scanner.next();

        if(doctor.getDoctorByID(doctorId) && patient.getPatientByID(patientId))
        {
            if(checkDoctorAvailability(doctorId,appointmentDate,connection))
            {
                String appointmentQuery = "INSERT INTO appointments(Patient_ID , Doctor_ID , Appointment_Date) VALUES (? , ? , ?);";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3,appointmentDate);

                    int rowAffected = preparedStatement.executeUpdate();

                    if(rowAffected>0)
                    {
                        System.out.println("Appointment Booked...");
                    }
                    else {
                        System.out.println("Failed to Book Appointment!!");
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Doctor not Available on this Date...");
            }
        }
        else{
            System.out.println("Either Doctor and Patient with this ID found!!!!");
        }

    }

    public static boolean checkDoctorAvailability(int doctorID , String appointmentDate , Connection connection)
    {
        String query = "SELECT COUNT(*) FROM appointments WHERE Doctor_ID = ? AND Appointment_Date = ?;";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorID);
            preparedStatement.setString(2, appointmentDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                int count = resultSet.getInt(1);

                if (count == 0)
                {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch (SQLException e) {
                e.printStackTrace();
        }

        return  false;
    }


    public static void clearScreen() {

        System.out.print("\033[H\033[2J");
        System.out.flush(); // Ensures the escape codes are sent immediately to the console
    }

    public static void pressEnterToContinue(Scanner scanner) {
        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();  // consume leftover newline
        scanner.nextLine();  // wait for user
    }


}
