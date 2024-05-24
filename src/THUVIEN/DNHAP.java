package THUVIEN;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import Security.PasswordHash;

public class DNHAP extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField TDN;
    private JPasswordField MATKHAU;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DNHAP frame = new DNHAP();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public DNHAP() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(72, 209, 204));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel TIEUDE = new JLabel("ĐĂNG NHẬP");
        TIEUDE.setFont(new Font("Times New Roman", Font.BOLD, 30));
        TIEUDE.setHorizontalAlignment(SwingConstants.CENTER);
        TIEUDE.setBounds(116, 10, 204, 50);
        contentPane.add(TIEUDE);

        JLabel TENDANGNHAP = new JLabel("Email Hoặc SDT :");
        TENDANGNHAP.setHorizontalAlignment(SwingConstants.RIGHT);
        TENDANGNHAP.setFont(new Font("Times New Roman", Font.BOLD, 19));
        TENDANGNHAP.setBounds(10, 70, 150, 50);
        contentPane.add(TENDANGNHAP);

        JLabel MATKHAU_LABEL = new JLabel("Mật khẩu :");
        MATKHAU_LABEL.setFont(new Font("Times New Roman", Font.BOLD, 19));
        MATKHAU_LABEL.setHorizontalAlignment(SwingConstants.RIGHT);
        MATKHAU_LABEL.setBounds(10, 130, 150, 50);
        contentPane.add(MATKHAU_LABEL);

        TDN = new JTextField();
        TDN.setFont(new Font("Times New Roman", Font.BOLD, 20));
        TDN.setBounds(170, 71, 256, 50);
        contentPane.add(TDN);
        TDN.setColumns(10);

        MATKHAU = new JPasswordField();
        MATKHAU.setFont(new Font("Times New Roman", Font.BOLD, 20));
        MATKHAU.setBounds(170, 130, 256, 50);
        contentPane.add(MATKHAU);

        JButton DANGNHAP = new JButton("Đăng nhập");
        DANGNHAP.setBackground(new Color(0, 250, 154));
        DANGNHAP.setFont(new Font("Times New Roman", Font.BOLD, 20));
        DANGNHAP.setBounds(170, 190, 256, 50);
        contentPane.add(DANGNHAP);

        JButton DANGKY = new JButton("Đăng ký");
        DANGKY.setBackground(new Color(0, 250, 154));
        DANGKY.setFont(new Font("Times New Roman", Font.BOLD, 20));
        DANGKY.setBounds(10, 190, 150, 50);
        contentPane.add(DANGKY);
        
        DANGKY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Tạo một cửa sổ mới cho giao diện đăng ký
                DKI dki = new DKI();
                // Hiển thị cửa sổ đăng ký
                dki.setVisible(true);
                // Ẩn cửa sổ đăng nhập
                setVisible(false);
            }
        });

        DANGNHAP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = TDN.getText();
                String password = new String(MATKHAU.getPassword());
                
                // Mã hóa mật khẩu trước khi gửi
                String hashedPassword = PasswordHash.hashPassword(password);
                sendLoginInfoToServer(username, hashedPassword);
            }
        });
    }

    private void sendLoginInfoToServer(String username, String password) {
        final String SERVER_ADDRESS = "localhost"; // Địa chỉ IP của máy chủ
        final int SERVER_PORT = 12345; // Port của máy chủ

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi lệnh DNHAP đến server
            out.println("DNHAP");
            
            // Gửi thông tin đăng nhập đến server
            out.println(username); // Gửi tên người dùng
            out.println(password); // Gửi mật khẩu

//            // In ra những gì được gửi tới máy chủ để kiểm tra
//            System.out.println("Email hoặc SDT: " + username);
//            System.out.println("Password: " + password);

         // Đọc phản hồi từ server và hiển thị nó
            String response = in.readLine();
            if (response != null) {
                if (response.equals("LOGIN_BOSS")) {
                    // Chuyển hướng sang QLI
                    QLI qli = new QLI();
                    qli.setVisible(true);
                    setVisible(false);
                } else if (response.equals("LOGIN_USER")) {
                    // Chuyển hướng sang MSACH
                    MSACH msach = new MSACH(username);
                    msach.setVisible(true);
                    setVisible(false);
                } else if (response.equals("LOGIN_FAILED")) {
                    JOptionPane.showMessageDialog(DNHAP.this, "Tài khoản không tồn tại. Vui lòng đăng nhập lại.");
                } else {
                    JOptionPane.showMessageDialog(DNHAP.this, response);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}