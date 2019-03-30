/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author c7-ong
 */
public class databaseConn {
    private static String connString = "jdbc:mysql://weeb03.cems.uwe.ac.uk:3306/fet17035315";
    private static String username = "fet17035315";
    private static String password = "y1WW3R";
    private static Connection connection;
    private static PreparedStatement statement;
    private static ResultSet result;
    private String role;
    public static boolean openConn() throws SQLException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println(ex);
        }
        try
        {
            connection = DriverManager.getConnection(connString, username, password);
            System.out.println("Database connected!");
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Error connecting to database!");
            System.out.println(e);
        }
        return false;
    }
    
     public static boolean login(String username, String password)
    {
        String loginQuery = "SELECT staffUsername, staffPassword, staffRole FROM staff WHERE staffUsername = ? AND staffPassword = ?";
        boolean flag = true;
        try
        {
            statement = connection.prepareStatement(loginQuery);
            statement.setString(1, username);
            statement.setString(2, password);
            result = statement.executeQuery();
            if (result.next())
            {
                 JOptionPane.showMessageDialog(null, "Login successful!");
                  MainMenuGUI mainmenu = new MainMenuGUI();
                  mainmenu.setRole(result.getString("staffRole"));
                  mainmenu.checkCredentials();
                  mainmenu.setVisible(true);
                  flag = true;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Login failed!");
                flag = false;
            }
            }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        if (flag == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
     
     public static void addNewPatient(String retrievedFirstName, 
             String retrievedLastName, 
             String retrievedGender, 
             String retrievedDOB, 
             String retrievedAddress, 
             String retrievedTelNo)
     {
         String addPatientQuery = "INSERT INTO patient (patientFirstName, patientLastName, patientGender, patientDOB, patientAddress, patientTelNo) VALUES " + "(?,?,?,?,?,?)";
         try
         {
             statement = connection.prepareStatement(addPatientQuery);
             statement.setString(1, retrievedFirstName);
             statement.setString(2, retrievedLastName);
             statement.setString(3, retrievedGender);
             statement.setString(4, retrievedDOB);
             statement.setString(5, retrievedAddress);
             statement.setString(6, retrievedTelNo);
             statement.executeUpdate();
             JOptionPane.showMessageDialog(null, "Record successsfully inserted!");
         }
         catch(SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
     public static void fillPatientID(JComboBox comboBox)
     {
         String patientComboBoxQuery = "SELECT idpatient FROM patient";
         try
         {
             statement = connection.prepareStatement(patientComboBoxQuery);
             result = statement.executeQuery();
             while(result.next())
             {
                 comboBox.addItem(result.getString("idpatient"));
             }
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
      public static void fillDoctorID(JComboBox comboBox)
     {
         String doctorComboBoxQuery = "SELECT idstaff FROM staff WHERE staffRole = 'Doctor'";
         try
         {
             statement = connection.prepareStatement(doctorComboBoxQuery);
             result = statement.executeQuery();
             while(result.next())
             {
                 comboBox.addItem(result.getString("idstaff"));
             }
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
      
      public static void fillAppointmentID(JComboBox comboBox)
     {
         String appointmentComboBoxQuery = "SELECT idappointment FROM appointment";
         try
         {
             statement = connection.prepareStatement(appointmentComboBoxQuery);
             result = statement.executeQuery();
             while(result.next())
             {
                 comboBox.addItem(result.getString("idappointment"));
             }
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
     public static void findPatientInfoForEdit(String patientID, JTextField id, JTextField firstname, JTextField lastname, JComboBox gender, JTextField dob, JTextField address, JTextField telno)
     {
         String patientEditQuery = "SELECT * FROM patient WHERE idpatient = ?";
         try
         {
             statement = connection.prepareStatement(patientEditQuery);
             statement.setString(1, patientID);
             result = statement.executeQuery();
             while(result.next())
             {
                 id.setText(result.getString("idpatient"));
                 firstname.setText(result.getString("patientFirstName"));
                 lastname.setText(result.getString("patientLastName"));
                 gender.setSelectedItem(result.getString("patientGender"));
                 dob.setText(result.getString("patientDOB"));
                 address.setText(result.getString("patientAddress"));
                 telno.setText(result.getString("patientTelNo"));
             }
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
     public static void updatePatients(String FirstName, String LastName, String Gender, String DOB, String Address, String TelNo, String PatientID)
     {
         String updatePatientQuery = "UPDATE patient SET patientFirstName = ?, patientLastName = ?, patientGender = ?, patientDOB = ?, patientAddress = ?, patientTelNo = ? WHERE idpatient = ?";
         try
         {
             statement =connection.prepareStatement(updatePatientQuery);
             statement.setString(1,FirstName);
             statement.setString(2, LastName);
             statement.setString(3, Gender);
             statement.setString(4, DOB);
             statement.setString(5, Address);
             statement.setString(6, TelNo);
             statement.setString(7, PatientID);
             statement.executeUpdate();
             JOptionPane.showMessageDialog(null, "Record successsfully updated!");
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
     public static void deletePatients(String id)
     {
         String deleteQuery = "DELETE FROM patient WHERE idpatient = ?";
         try
         {
             statement = connection.prepareStatement(deleteQuery);
             statement.setString(1, id);
             statement.executeUpdate();
             JOptionPane.showMessageDialog(null, "Record successsfully deleted!");
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
     public static void addNewAppointment(String retrievedDetails, 
             String retrievedDate,
             String retrievedStatus,
             String retrievedPatientID, 
             String retrievedStaffID)
     {
         String addAppointmentQuery = "INSERT INTO appointment (appointmentDetails, appointmentDate, appointmentStatus, patient_idpatient, staff_idstaff) VALUES " + "(?,?,?,?,?)";
         try
         {
             statement = connection.prepareStatement(addAppointmentQuery);
             statement.setString(1, retrievedDetails);
             statement.setString(2, retrievedDate);
             statement.setString(3, retrievedStatus);
             statement.setString(4, retrievedPatientID);
             statement.setString(5, retrievedStaffID);
             statement.executeUpdate();
             JOptionPane.showMessageDialog(null, "Appointment successsfully inserted!");
         }
         catch(SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
     public static void findAppointmentInfoForEdit(String appointmentid, JTextField id, JTextArea details, JTextField date, JComboBox status, JComboBox patientid, JComboBox doctorid)
     {
         String patientEditQuery = "SELECT * FROM appointment WHERE idappointment = ?";
         try
         {
             statement = connection.prepareStatement(patientEditQuery);
             statement.setString(1, appointmentid);
             result = statement.executeQuery();
             while(result.next())
             {
                 id.setText(result.getString("idappointment"));
                 details.setText(result.getString("appointmentDetails"));
                 date.setText(result.getString("appointmentDate"));
                 status.setSelectedItem(result.getString("appointmentStatus"));
                 patientid.setSelectedItem(result.getString("patient_idpatient"));
                 doctorid.setSelectedItem(result.getString("staff_idstaff"));
             }
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
     public static void updateAppointmentDetails(String details, String date, String status, String doctorID, String id)
     {
         String updateAppointmentsQuery = "UPDATE appointment SET appointmentDetails = ?, appointmentDate = ?, appointmentStatus = ?, staff_idStaff = ? WHERE idappointment = ?";
         try
         {
             statement = connection.prepareStatement(updateAppointmentsQuery);
             statement.setString(1, details);
             statement.setString(2, date);
             statement.setString(3, status);
             statement.setString(4, doctorID);
             statement.setString(5, id);
             statement.executeUpdate();
             JOptionPane.showMessageDialog(null, "Appointment details updated!");
         }
         catch(SQLException e)
         {
              System.out.println(e.getMessage());
         }
     }
     
     public static void deleteAppointment(String id)
     {
         String deleteQuery = "DELETE FROM appointment WHERE idappointment = ?";
         try
         {
             statement = connection.prepareStatement(deleteQuery);
             statement.setString(1, id);
             statement.executeUpdate();
             JOptionPane.showMessageDialog(null, "Record successsfully deleted!");
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
     public static void addStaff(String username, String password, String staffName, String staffRole, String staffAddress, String staffTelNo, String staffDOB)
     {
         String addStaffQuery = "INSERT INTO staff (staffUsername, staffPassword, staffName, staffRole, staffAddress, staffTelNo, staffDOB) VALUES " + "(?,?,?,?,?,?,?)";
         try
         {
             statement = connection.prepareStatement(addStaffQuery);
             statement.setString(1, username);
             statement.setString(2, password);
             statement.setString(3, staffName);
             statement.setString(4, staffRole);
             statement.setString(5, staffAddress);
             statement.setString(6, staffTelNo);
             statement.setString(7, staffDOB);
             statement.executeUpdate();
             JOptionPane.showMessageDialog(null, "Staff member added!");
         }
         catch(SQLException e)
         {
              System.out.println(e.getMessage());
         }
     }
     
     public static void fillStaffID(JComboBox staffComboBox)
     {
         String addStaffQuery = "SELECT idstaff FROM staff";
         try
         {
             statement = connection.prepareStatement(addStaffQuery);
             result = statement.executeQuery();
              while(result.next())
             {
                 staffComboBox.addItem(result.getString("idstaff"));

             }
         }
         catch(SQLException e)
         {
              System.out.println(e.getMessage());
         }
     }
     
     public static void findStaffDetailsForEdit(String staffid, 
             JTextField id, 
             JTextField username, 
             JPasswordField password, 
             JTextField staffName, 
             JTextField staffRole, 
             JTextField address, 
             JTextField telno, 
             JTextField dob)
     {
         String staffEditQuery = "SELECT * FROM staff WHERE idstaff = ?";
         try
         {
             statement = connection.prepareStatement(staffEditQuery);
             statement.setString(1, staffid);
             result = statement.executeQuery();
             while(result.next())
             {
                 id.setText(result.getString("idstaff"));
                 username.setText(result.getString("staffUsername"));
                 password.setText(result.getString("staffPassword"));
                 staffName.setText(result.getString("staffName"));
                 staffRole.setText(result.getString("staffRole"));
                 address.setText(result.getString("staffAddress"));
                 telno.setText(result.getString("staffTelNo"));
                 dob.setText(result.getString("staffDOB"));
             }
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
     
     public static void updateStaffDetails(String username, String password, String staffName, String staffRole, String staffAddress, String staffTelNo, String staffDOB, String staffid)
     {
         String updateAppointmentsQuery = "UPDATE staff SET staffUsername = ?, staffPassword = ?, staffName = ?, staffRole = ?, staffAddress = ?, staffTelNo = ?, staffDOB = ? WHERE idstaff = ?";
         try
         {
             statement = connection.prepareStatement(updateAppointmentsQuery);
             statement.setString(1, username);
             statement.setString(2, password);
             statement.setString(3, staffName);
             statement.setString(4, staffRole);
             statement.setString(5, staffAddress);
             statement.setString(6, staffTelNo);
             statement.setString(7, staffDOB);
             statement.setString(8, staffid);
             statement.executeUpdate();
             JOptionPane.showMessageDialog(null, "Staff details updated!");
         }
         catch(SQLException e)
         {
              System.out.println(e.getMessage());
         }
     }
     
      public static void deleteStaff(String id)
     {
         String deleteQuery = "DELETE FROM staff WHERE idstaff = ?";
         try
         {
             statement = connection.prepareStatement(deleteQuery);
             statement.setString(1, id);
             statement.executeUpdate();
             JOptionPane.showMessageDialog(null, "Record successsfully deleted!");
         }
         catch (SQLException e)
         {
             System.out.println(e.getMessage());
         }
     }
}
