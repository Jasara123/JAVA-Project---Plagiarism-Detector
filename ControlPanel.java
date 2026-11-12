package WindowsCode;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import MainCode.User;



public class ControlPanel {
    private String[][] data = new String[2][];
    private String result;
    Boolean isFileUploaded = false;
    private JFrame fenetre =  new JFrame("Plagiarism Detector");
    String name_file1;
    String name_file2;
    private Home home;

    public ControlPanel(Home home) {
        this.home = home;
    }

    public JFrame getFenetre() {
        return fenetre;
    }

    public void createWindows(JFrame frame, String name) {
        fenetre.setSize(1000, 1000);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setLayout(null);

        JLabel greeting = new JLabel("Welcome to Plagiarism Detector");
        greeting.setFont(new Font("Arial", Font.BOLD, 24));
        greeting.setBounds(320, 30, 400, 40);
        fenetre.add(greeting);

        JButton button1 = new JButton("Uplaod File");
        button1.setBounds(400, 100, 200, 50);
        fenetre.add(button1);

        JButton button2 = new JButton("Back");
        button2.setBounds(400, 170, 200, 50);
        fenetre.add(button2);

        JButton button3 = new JButton("Result");
        button3.setBounds(400, 240, 200, 50);
        fenetre.add(button3);

    
        

        button1.addActionListener(e -> {
            for(int i = 0; i < 2; i++){
                JFileChooser chooser = new JFileChooser();
                int resultat = chooser.showOpenDialog(fenetre);
                if (resultat == JFileChooser.APPROVE_OPTION) {
                    File fichierSource = chooser.getSelectedFile();
                    try{
                        File dossierCible = new File("/Users/andrrianarivonya.andryjasara/Documents/document-SRM/S-3/CSE-309-ADVANCED JAVA PROGRAMMING/Project/Output/" + fichierSource.getName());
                        Files.copy(fichierSource.toPath(), dossierCible.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        String temp = Files.readString(dossierCible.toPath());
                        temp = temp.toLowerCase();
                        data[i] = temp.split("\\W+");
                        JOptionPane.showMessageDialog(fenetre, "Fichier"+(i + 1)+" envoyé avec succès !");
                        try (Connection conn = DriverManager.getConnection(User.DB_URL, User.DB_USER, User.DB_PASSWORD)){
                            if(i == 0){
                                name_file1 = dossierCible.getName();
                                String query = "INSERT INTO files (username, file_name1) VALUES (?, ?)";
                                PreparedStatement stmt = conn.prepareStatement(query);
                                stmt.setString(1, name);
                                stmt.setString(2, dossierCible.getName());
                                stmt.executeUpdate();
                            }
                            if(i == 1){
                                name_file2 = dossierCible.getName();
                                String query1 = "UPDATE files SET file_name2 = ? WHERE username = ?";
                                PreparedStatement stmt1 = conn.prepareStatement(query1);
                                stmt1.setString(1, dossierCible.getName());
                                stmt1.setString(2, name);
                                stmt1.executeUpdate();
                            }
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                    catch (IOException ex) {
                        JOptionPane.showMessageDialog(fenetre, "Error: " + ex.getMessage());
                    }
                }
                if(i == 1){
                    isFileUploaded = true;
                }
            }
        });

        button2.addActionListener(e -> {
            fenetre.dispose();
            home.refreshTable(name);
            frame.setVisible(true);
        });

        button3.addActionListener(e ->{
            if(isFileUploaded){
                ResultPanel resultwindows = new ResultPanel(data);
                resultwindows.createWindows(getFenetre(), name, name_file1, name_file2);
            }else{
                JOptionPane.showMessageDialog(fenetre, "Please upload both files before checking the result.");
            }
        });


        fenetre.setVisible(true);
    }
}
