package project.javaminiproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;

public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static int a = 1;
    @FXML
    private Text eName;
    @FXML
    private Label eDetails;
    @FXML
    private Label eTime;
    @FXML
    private Label eDate;
    @FXML
    private Label eLink;
    //refresh button
    public void initial(ActionEvent event) throws IOException {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        String getename = "SELECT * FROM Event_Details where eId=" + a;
        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(getename);
            while (resultSet.next()) {
                eName.setText(resultSet.getString("EventName"));
                eDetails.setText(resultSet.getString("EventDetails"));
                eDate.setText(resultSet.getString("EventDate"));
                eTime.setText(resultSet.getString("EventTime"));
                eLink.setText(resultSet.getString("EventLink"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //next, previous actions
    public boolean check(int c) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        String check = "SELECT count(1) FROM Event_Details WHERE eId='"+c+"'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(check);
            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1){
                    return true;
                } else{
                    return false;
                }
            }
        } catch (Exception event) {
            event.printStackTrace();
        }
        return false;
    }
    public void next(ActionEvent event) throws IOException {
        if (a<getMaxId()){
        a++;
        while(!check(a)&&(a<getMaxId()))
            a++;
        if (check(a))
            initial(event);}
    }
    public void prev(ActionEvent event) throws IOException {
        if (a>1){
            a--;
            while (!check(a)&&a>1)
                a--;
        }
        if (check(a))
            initial(event);
    }
    //delete
    public void delete(ActionEvent e) throws IOException {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        String delete = "DELETE FROM Event_Details WHERE eId='"+a+"'";
        try {
            Statement statement = connectDB.createStatement();
            statement.execute(delete);
        } catch (Exception event) {
            event.printStackTrace();
        }
        prev(e);
    }
    //search
    @FXML
    private TextField search;
    public void search(ActionEvent e) throws IOException{
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        String s="Select * from Event_Details WHERE EventName='"+search.getText()+"'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet r=statement.executeQuery(s);
            while (r.next()){
                if(check(r.getInt("eId"))){
                    a=r.getInt("eId");
                    initial(e);}
            }
        } catch (Exception event) {
            event.printStackTrace();
        }
    }
    //login
    @FXML
    private Button Login;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField cancel;

    public void LoginOnAction(ActionEvent event) throws IOException {
        //Login.setText("Invalid Login!");
        if (Objects.equals(username.getText(), "admin") && Objects.equals(password.getText(), "admin")){
            switchToadmin(event);
        }
        else{
        if (!username.getText().isBlank() && !password.getText().isBlank()) {
            error.setText("Try again");
            if (validateLogin()) {
                SwitchToMainPage(event);
            }
        } else {
            error.setText("Enter your Username and Password");
        }
    }
    }


    public boolean validateLogin(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        String verifylogin = "SELECT count(1) FROM Registration_details WHERE username='" + username.getText() + "'AND password='" + password.getText() + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifylogin);

            while (queryResult.next()) {
                return queryResult.getInt(1) == 1;
            }
        } catch (Exception event) {
            event.printStackTrace();
        }
        return false;
    }
    @FXML
    private TextField Email;
    @FXML
    private Label user;
    @FXML
    private Label pass;
    public void forgor(ActionEvent e)throws IOException{
        user.setText("Username : Try again");
        pass.setText("Password : Try again");
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        String f="select username, password from Registration_details where Emailid='"+Email.getText()+"'";
        try {
            Statement s=connectDB.createStatement();
            ResultSet r=s.executeQuery(f);
            while (r.next()){
                user.setText("Username : "+r.getString(1));
                pass.setText("Password : "+r.getString(2));
            }
        } catch (Exception event) {
            event.printStackTrace();
        }
    }
    //register
    @FXML
    private TextField fName;
    @FXML
    private TextField lName;
    @FXML
    private TextField num;
    @FXML
    private TextField email;
    @FXML
    private Label error;
    public void registerdetails(ActionEvent e) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        String rdetails = "INSERT INTO Registration_details(Firstname,Lastname,Phonenumber,Emailid,Username,Password) VALUES ('" + fName.getText() + "','" + lName.getText() + "','" + num.getText() + "','" + email.getText() + "','" + username.getText() + "','" + password.getText() + "')";
        if (!fName.getText().isEmpty() && !lName.getText().isEmpty() && !num.getText().isEmpty() && !username.getText().isEmpty() && !password.getText().isEmpty()) {
            try {
                Statement statement = connectDB.createStatement();
                int a = statement.executeUpdate(rdetails);
                if (a == 1)
                    SwitchTologinpage(e);
            } catch (Exception event) {
                event.printStackTrace();
            }
        } else {
            error.setText("Error");
        }
    }
    //create events
    public int getMaxId() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        try {
            Statement s = connectDB.createStatement();
            ResultSet r = s.executeQuery("SELECT max(eId) FROM Event_Details");
            while (r.next()) {
                return r.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    @FXML
    private TextField Eventname;
    @FXML
    private TextField EventCatergory;
    @FXML
    private TextField EventLink;
    @FXML
    private TextField EventDetails;
    @FXML
    private DatePicker EventDate;
    @FXML
    private TextField EventTime;
    @FXML
    private Label Incomplete;
    public void createEvent(ActionEvent e) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        String createvent = "INSERT INTO Event_Details(EventName,EventCategory,EventLink,EventDetails,EventDate,EventTime,eId) VALUES ('" + Eventname.getText() + "','" + EventCatergory.getText() + "','" + EventLink.getText() + "','" + EventDetails.getText() + "','" + ((TextField) EventDate.getEditor()).getText() + "','" + EventTime.getText() + "','" + (1 + getMaxId()) + "')";
        if (!Eventname.getText().isEmpty() && !EventCatergory.getText().isEmpty() && !EventLink.getText().isEmpty() && !EventDetails.getText().isEmpty() && !((TextField) EventDate.getEditor()).getText().isEmpty() && !EventTime.getText().isEmpty()) {
            try {
                Statement statement = connectDB.createStatement();
                int b = statement.executeUpdate(createvent);
                if (b == 1) {
                    switchToadmin(e);
                }
            } catch (Exception event) {
                event.printStackTrace();
            }
        } else {
            Incomplete.setText("Error");
        }
    }

    //Update
    public void uRefresh(ActionEvent event){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnected();
        String getename = "SELECT * FROM Event_Details where eId=" + a;
        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(getename);
            while (resultSet.next()) {
                Eventname.setText(resultSet.getString("EventName"));
                EventCatergory.setText(resultSet.getString("EventCategory"));
                EventDetails.setText(resultSet.getString("EventDetails"));
                ((TextField) EventDate.getEditor()).setText(resultSet.getString("EventDate"));
                EventTime.setText(resultSet.getString("EventTime"));
                EventLink.setText(resultSet.getString("EventLink"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void update(ActionEvent event) throws IOException {
        if (!Eventname.getText().isBlank()&&!EventDetails.getText().isBlank()&&!((TextField) EventDate.getEditor()).getText().isBlank()&&!EventTime.getText().isBlank()&&!EventLink.getText().isBlank()&&!EventCatergory.getText().isBlank()) {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnected();
            String u = "UPDATE Event_Details SET EventName='" + Eventname.getText() + "',EventCategory='" + EventCatergory.getText() + "',EventLink='" + EventLink.getText() + "',EventDetails='" + EventDetails.getText() + "',EventDate='" + ((TextField) EventDate.getEditor()).getText() + "',EventTime='" + EventTime.getText() + "' WHERE eId=" + a;
            try {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(u);
                switchToadmin(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private Button addevent;
    @FXML
    private Button delete;
    //switch to pages
    public void SwitchToCreateEventPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("createevent.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void SwitchToMainPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainpage.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void SwitchTologinpage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void SwitchToRegister(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void switchToforgotpage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("forgotuserpass.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void switchToupdate(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("update.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void switchToadmin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainpageA.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}