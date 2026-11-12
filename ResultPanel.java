package WindowsCode;

import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import MainCode.PlagiarismDetector;
import MainCode.User;


class ResultPanel {
    private String[][] data = new String[2][];
    private String result;


    public ResultPanel(String[][] data){
        this.data = data;
    }

    public void createWindows(JFrame window, String name, String file1_name, String file2_name){
        JFrame fenetre = new JFrame("Result of Plagiarism Detection");
        fenetre.setSize(1000, 1000);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setLayout(null);

        JLabel text = new JLabel("Result of Plagiarism Detection: ");
        text.setFont(new Font("Arial", Font.BOLD, 24));
        text.setBounds(50, 30, 500, 40);
        fenetre.add(text);

        PlagiarismDetector detector = new PlagiarismDetector(data);
        detector.lcs();
        result = detector.getResult();

        double size = 0;
        if(data[0].length > data[1].length){
            size = data[0].length;
        }else{
            size = data[1].length;
        }
        double percentage = ((double)result.length() / (size*5)) * 100;
        if(percentage > 100){
            percentage = 100;
        }

        JLabel percentage_result = new JLabel("Percentage of Plagiarism: " + String.format("%.2f", percentage) + "%");
        percentage_result.setFont(new Font("Arial", Font.BOLD, 24));
        percentage_result.setBounds(50, 100, 500, 30);
        fenetre.add(percentage_result);

        try (Connection conn = DriverManager.getConnection(User.DB_URL, User.DB_USER, User.DB_PASSWORD)) {
            String query = "UPDATE files SET percentage_plagia = ? WHERE username = ? AND file_name1 = ? AND file_name2 = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, percentage);
            stmt.setString(2, name);
            stmt.setString(3, file1_name);
            stmt.setString(4, file2_name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JButton button1 = new JButton("Exit");
        button1.setBounds(50, 150, 200, 50);
        button1.addActionListener( e -> {
            System.exit(0);
        });
        fenetre.add(button1);

        JButton button2 = new JButton("Back");
        button2.setBounds(50, 220, 200, 50);
        button2.addActionListener( e -> {
            fenetre.dispose();
            window.setVisible(true);
        });
        fenetre.add(button2);
        
        fenetre.setVisible(true);
    }
}
