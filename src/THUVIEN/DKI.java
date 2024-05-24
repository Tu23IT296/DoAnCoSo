package THUVIEN;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JPasswordField;
import java.awt.Color;

public class DKI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField TDN;
    private JTextField SDT;
    private JTextField EM;
    private JPasswordField MK;
    private JPasswordField XACNHAN;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DKI frame = new DKI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public DKI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 540, 460);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel TIEUDE = new JLabel("ĐĂNG KÝ");
        TIEUDE.setFont(new Font("Times New Roman", Font.BOLD, 30));
        TIEUDE.setHorizontalAlignment(SwingConstants.CENTER);
        TIEUDE.setBounds(116, 10, 204, 50);
        contentPane.add(TIEUDE);
        
        JLabel TENTAIKHOAN = new JLabel("Tên tài khoản :");
        TENTAIKHOAN.setFont(new Font("Times New Roman", Font.BOLD, 20));
        TENTAIKHOAN.setHorizontalAlignment(SwingConstants.RIGHT);
        TENTAIKHOAN.setBounds(10, 70, 192, 50);
        contentPane.add(TENTAIKHOAN);
        
        TDN = new JTextField();
        TDN.setFont(new Font("Times New Roman", Font.BOLD, 20));
        TDN.setBounds(212, 71, 214, 50);
        contentPane.add(TDN);
        TDN.setColumns(10);
        
        SDT = new JTextField();
        SDT.setFont(new Font("Times New Roman", Font.BOLD, 20));
        SDT.setColumns(10);
        SDT.setBounds(212, 130, 214, 50);
        contentPane.add(SDT);
        
        JButton DANGKI = new JButton("Đăng kí");
        DANGKI.setBackground(new Color(144, 238, 144));
        DANGKI.setFont(new Font("Times New Roman", Font.BOLD, 25));

        DANGKI.setBounds(116, 370, 204, 50);
        contentPane.add(DANGKI);
        
        EM = new JTextField();
        EM.setFont(new Font("Times New Roman", Font.BOLD, 20));
        EM.setColumns(10);
        EM.setBounds(212, 190, 214, 50);
        contentPane.add(EM);
        
        MK = new JPasswordField();
        MK.setFont(new Font("Times New Roman", Font.BOLD, 20));
        MK.setBounds(212, 250, 214, 50);
        contentPane.add(MK);
        
        XACNHAN = new JPasswordField();
        XACNHAN.setFont(new Font("Times New Roman", Font.BOLD, 20));
        XACNHAN.setBounds(212, 310, 214, 50);
        contentPane.add(XACNHAN);
        
        JLabel SODIENTHOAI = new JLabel("Số điện thoại :");
        SODIENTHOAI.setHorizontalAlignment(SwingConstants.RIGHT);
        SODIENTHOAI.setFont(new Font("Times New Roman", Font.BOLD, 20));
        SODIENTHOAI.setBounds(10, 130, 192, 50);
        contentPane.add(SODIENTHOAI);
        
        JLabel EMAIL = new JLabel("Email :");
        EMAIL.setHorizontalAlignment(SwingConstants.RIGHT);
        EMAIL.setFont(new Font("Times New Roman", Font.BOLD, 20));
        EMAIL.setBounds(10, 190, 192, 50);
        contentPane.add(EMAIL);
        
        JLabel MATKHAU = new JLabel("Mật khẩu :");
        MATKHAU.setHorizontalAlignment(SwingConstants.RIGHT);
        MATKHAU.setFont(new Font("Times New Roman", Font.BOLD, 20));
        MATKHAU.setBounds(34, 247, 168, 50);
        contentPane.add(MATKHAU);
        
        JLabel XACNHANMATKHAU = new JLabel("Xác nhận mật khẩu :");
        XACNHANMATKHAU.setHorizontalAlignment(SwingConstants.RIGHT);
        XACNHANMATKHAU.setFont(new Font("Times New Roman", Font.BOLD, 20));
        XACNHANMATKHAU.setBounds(10, 310, 192, 50);
        contentPane.add(XACNHANMATKHAU);
        
        JButton btnShowPassword = new JButton("Show");
        btnShowPassword.setBackground(new Color(221, 160, 221));
        btnShowPassword.setFont(new Font("Times New Roman", Font.BOLD, 10));
        btnShowPassword.setBounds(420, 250, 80, 50);
        btnShowPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                char echoChar = (MK.getEchoChar() == '\u0000') ? '\u2022' : '\u0000';
                MK.setEchoChar(echoChar);
            }
        });
        contentPane.add(btnShowPassword);
        
        JButton btnShowConfirmPassword = new JButton("Show");
        btnShowConfirmPassword.setBackground(new Color(221, 160, 221));
        btnShowConfirmPassword.setFont(new Font("Times New Roman", Font.BOLD, 10));
        btnShowConfirmPassword.setBounds(420, 310, 80, 50);
        btnShowConfirmPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                char echoChar = (XACNHAN.getEchoChar() == '\u0000') ? '\u2022' : '\u0000';
                XACNHAN.setEchoChar(echoChar);
            }
        });
        contentPane.add(btnShowConfirmPassword);
        
        // Thêm sự kiện cho nút Đăng kí
        DANGKI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lấy thông tin từ các trường dữ liệu
                String username = TDN.getText();
                String phoneNumber = SDT.getText();
                String email = EM.getText();
                String password = new String(MK.getPassword());
                String confirmPassword = new String(XACNHAN.getPassword());
                // Kiểm tra mật khẩu nhập lại
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(DKI.this, "Mật khẩu xác nhận không khớp", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Gửi thông tin đăng ký tới Sever
                sendRegistrationInfoToServer(username, phoneNumber, email, password);
            }
        });
    }

    private void sendRegistrationInfoToServer(String username, String phoneNumber, String email, String password) {
        final String SERVER_ADDRESS = "localhost"; // Địa chỉ IP của máy chủ
        final int SERVER_PORT = 12345; // Port của máy chủ

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi lệnh 'DKI' trước khi gửi thông tin đăng ký
            out.println("DKI");

            // Gửi thông tin đăng ký đến server
            out.println(username); // Gửi tên người dùng
            out.println(phoneNumber); // Gửi số điện thoại
            out.println(email); // Gửi email
            out.println(password); // Gửi mật khẩu
            
//            // In ra những gì được gửi tới máy chủ để kiểm tra
//            System.out.println("Username: " + username);
//            System.out.println("Phone number: " + phoneNumber);
//            System.out.println("Email: " + email);
//            System.out.println("Password: " + password);

            // Đọc phản hồi từ server và hiển thị nó
            String response = in.readLine();
//            System.out.println("Phản hồi từ máy chủ: " + response); // In ra để kiểm tra giá trị của response

            if (response != null && response.equals("ADD_USER")) {
                JOptionPane.showMessageDialog(DKI.this, "Đăng ký thành công");

                // Nếu đăng ký thành công, ẩn cửa sổ đăng ký và hiển thị lại cửa sổ đăng nhập
                DNHAP loginFrame = new DNHAP();
                loginFrame.setVisible(true);
                dispose(); // Ẩn cửa sổ hiện tại
            } else {
                JOptionPane.showMessageDialog(DKI.this, response, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(DKI.this, "Lỗi kết nối tới máy chủ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
