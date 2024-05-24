package THUVIEN;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CHATUSER extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextArea textArea;
    private String username; // Add username field

    public CHATUSER(String username) { // Modify constructor to accept username
        this.username = username; // Set the username field
        initialize(); // Call initialize method to set up the UI
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(0, 0));

        textField = new JTextField();
        panel.add(textField, BorderLayout.CENTER);
        textField.setColumns(10);

        JButton btnSend = new JButton("Gửi");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText();
                sendMessage(message);
                textField.setText("");
            }
        });
        panel.add(btnSend, BorderLayout.EAST);
    }

    private void sendMessage(String message) {
        // Thực hiện gửi tin nhắn đi
        // Code ở đây để gửi tin nhắn đi
        // Ví dụ:
        textArea.append("Bạn: " + message + "\n");
    }
}
