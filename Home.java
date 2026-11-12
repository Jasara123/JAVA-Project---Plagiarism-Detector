package WindowsCode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import MainCode.User;

public class Home {
    JTable fileTable;
    public void createHomeWindow(String username, JFrame fenetre) {
        JFrame frame = new JFrame("Home - Plagiarism Detector");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Plagiarism Detector - Home", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        Object[][] fileData = displayData(username);

        // Table to display files and plagiarism percentages
        String[] columnNames = {"File 1", "File 2", "Plagiarism Percentage"};
        DefaultTableModel tableModel = new DefaultTableModel(fileData, columnNames);
        fileTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(fileTable);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // Panel for file comparison
        JPanel comparePanel = new JPanel();
        comparePanel.setLayout(new BoxLayout(comparePanel, BoxLayout.Y_AXIS));
        comparePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton compareButton = new JButton("Compare New Files");
        compareButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        compareButton.setPreferredSize(new Dimension(200, 50));
        compareButton.setFont(new Font("Arial", Font.BOLD, 16));
        comparePanel.add(compareButton);
        comparePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton button1 = new JButton("Logout");
        button1.setAlignmentX(JButton.CENTER_ALIGNMENT);
        button1.setPreferredSize(new Dimension(200, 50));
        button1.setFont(new Font("Arial", Font.BOLD, 16));
        comparePanel.add(button1);

        frame.add(comparePanel, BorderLayout.SOUTH);

        compareButton.addActionListener(e -> {
            ControlPanel controlPanel = new ControlPanel(this);
            controlPanel.createWindows(frame, username);
        });

        button1.addActionListener(e -> {
            frame.dispose();
            fenetre.setVisible(true);
        });

        frame.setVisible(true);
    }

    protected Object[][] displayData(String username) {
        List<Object[]> data = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(User.DB_URL, User.DB_USER, User.DB_PASSWORD)) {
            String query = "SELECT file_name1, file_name2, percentage_plagia FROM files WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String file1 = rs.getString("file_name1");
                String file2 = rs.getString("file_name2");
                double percentage = rs.getDouble("percentage_plagia");
                data.add(new Object[]{file1, file2, percentage + "%"});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data.toArray(new Object[0][]);
    }

    public void refreshTable(String username) {
        Object[][] updatedData = displayData(username);
        DefaultTableModel tableModel = (DefaultTableModel) fileTable.getModel();
        tableModel.setRowCount(0);
        for (Object[] row : updatedData) {
            tableModel.addRow(row);
        }
    }

}
