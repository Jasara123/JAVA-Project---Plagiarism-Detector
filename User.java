package MainCode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/Plagiarism_detection";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";
    protected String username;
    protected String password;

    public User(String name, String pass){
        this.username = name;
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setname(String pseudo){
        this.username = pseudo;
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String passw){
        this.password = passw;
    }

    public boolean connection(){
        try(Connection connect = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            String request = "SELECT * FROM profils WHERE username = ? AND user_password = ?";
            PreparedStatement state = connect.prepareStatement(request);
            state.setString(1, this.username);
            state.setString(2, this.password);
            ResultSet rs = state.executeQuery();
            return rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean inscription(){
        try(Connection connect = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            String request = "INSERT INTO profils (username, user_password) VALUES (?, ?)";
            PreparedStatement state = connect.prepareStatement(request);
            if(!verify_profil()){
                System.out.println("Username is available. Proceeding with registration.");
                state.setString(1, this.username);
                state.setString(2, this.password);
                state.executeUpdate();
                return true;
            }else{
                System.out.println("Username already exists. Choose another Username!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean verify_profil(){
        try(Connection connect = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            String request = "SELECT * FROM profils WHERE username = ?";
            PreparedStatement state = connect.prepareStatement(request);
            state.setString(1, this.username);
            ResultSet rs = state.executeQuery();
            return rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
