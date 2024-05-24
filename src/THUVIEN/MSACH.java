package THUVIEN;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import SQL.DatabaseConnection;
import javax.swing.SwingConstants;
import java.awt.Color;

public class MSACH extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField textField;
    private JLabel AnhBia;
    private JMenuBar menuBar;
    private String username;
    private JLabel Name;


    public MSACH(String username) {
        this.username = username;
        initComponents();
        displayUsername();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 510);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(201, 33, 636, 410);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        
        menuBar = new JMenuBar();
        menuBar.setBounds(191, 11, 101, 22);
        contentPane.add(menuBar);

        JMenu categoryMenu = new JMenu("Thể loại");
        menuBar.add(categoryMenu);

        JMenuItem allMenuItem = new JMenuItem("Tất cả");
        JMenuItem businessMenuItem = new JMenuItem("Kinh doanh");
        JMenuItem novelMenuItem = new JMenuItem("Tiểu thuyết");
        JMenuItem comicMenuItem = new JMenuItem("Truyện tranh");

        categoryMenu.add(allMenuItem);
        categoryMenu.add(businessMenuItem);
        categoryMenu.add(novelMenuItem);
        categoryMenu.add(comicMenuItem);

        allMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayData("SELECT * FROM books");
            }
        });

        businessMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayData("SELECT * FROM books WHERE genre = 'Kinh doanh'");
            }
        });

        novelMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayData("SELECT * FROM books WHERE genre = 'Tiểu thuyết'");
            }
        });

        comicMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayData("SELECT * FROM books WHERE genre = 'Truyện tranh'");
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String bookTitle = String.valueOf(table.getValueAt(selectedRow, 1));
                        ImageIcon imageIcon = loadImageForBook(bookTitle);
                        if (imageIcon != null) {
                            AnhBia.setIcon(imageIcon);
                            adjustImageSize(imageIcon);
                        } else {
                            AnhBia.setIcon(null);
                        }
                    }
                }
            }
        });

        JButton btnNewButton = new JButton("MƯỢN SÁCH");
        btnNewButton.setBackground(new Color(144, 238, 144));
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnNewButton.setBounds(10, 192, 181, 35);
        contentPane.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int bookId = (int) table.getValueAt(selectedRow, 0);

                    // Kiểm tra sự tồn tại của sách trong cơ sở dữ liệu
                    if (isBookAvailable(bookId)) {
                        Date borrowDate = new Date();
                        Time borrowTime = new Time(System.currentTimeMillis());

                        if (addBorrowInfo(username, bookId, borrowDate, borrowTime)) {
                            JOptionPane.showMessageDialog(contentPane, "Mượn sách thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "Đã xảy ra lỗi khi mượn sách.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Sách này đã được mượn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Vui lòng chọn một cuốn sách để mượn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        JLabel lblTime = new JLabel("");
        lblTime.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblTime.setBounds(10, 11, 200, 20);
        contentPane.add(lblTime);

        Thread updateTimeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    lblTime.setText(dateFormat.format(currentDate));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        updateTimeThread.start();

        Name = new JLabel(username);
        Name.setFont(new Font("Times New Roman", Font.BOLD, 35));
        Name.setHorizontalAlignment(SwingConstants.CENTER);
        Name.setBounds(10, 56, 171, 125);
        contentPane.add(Name);
        
        textField = new JTextField();
        textField.setBounds(826, 10, 250, 22);
        contentPane.add(textField);
        textField.setColumns(10);
        
        JButton btnNewButton1 = new JButton("TÌM KIẾM");
        btnNewButton1.setBackground(new Color(144, 238, 144));
        btnNewButton1.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnNewButton1.setBounds(668, 10, 159, 23);
        contentPane.add(btnNewButton1);
        btnNewButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchTitle = textField.getText();
                if (searchTitle != null && !searchTitle.isEmpty()) {
                    displayData("SELECT * FROM books WHERE title LIKE '%" + searchTitle + "%'");
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Vui lòng nhập tên sách để tìm kiếm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        AnhBia = new JLabel("New label");
        AnhBia.setBounds(836, 33, 240, 388);
        contentPane.add(AnhBia);
        
        JButton btnLmMi = new JButton("LÀM MỚI");
        btnLmMi.setBackground(new Color(144, 238, 144));
        btnLmMi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayData("SELECT * FROM books");
            }
        });
        btnLmMi.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnLmMi.setBounds(499, 10, 159, 23);
        contentPane.add(btnLmMi);
        
        JLabel lblNewLabel = new JLabel("Sách được mượn phải được trao trả tại quầy và thanh toán tiền mượn.");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 15));
        lblNewLabel.setBounds(201, 450, 636, 23);
        contentPane.add(lblNewLabel);

        Thread movingTextThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int x = 201; // Vị trí ban đầu của dòng chữ
                while (true) {
                    lblNewLabel.setBounds(x, 450, 636, 23);
                    try {
                        Thread.sleep(10); // Điều chỉnh tốc độ di chuyển ở đây
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    x--; // Di chuyển vị trí của dòng chữ sang trái
                    if (x + lblNewLabel.getWidth() < 0) {
                        x = contentPane.getWidth(); // Reset vị trí khi dòng chữ di chuyển hết màn hình
                    }
                    contentPane.revalidate(); // Cập nhật lại contentPane để hiển thị lại dòng chữ
                    contentPane.repaint();
                }
            }
        });
        movingTextThread.start();

        displayData("SELECT * FROM books"); // Hiển thị dữ liệu ban đầu
    }
    
    private void displayData(String query) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);

            int columnCount = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rs.getMetaData().getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ImageIcon loadImageForBook(String bookTitle) {
        String imagePath = "src/image/" + bookTitle + ".jpeg";
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            return new ImageIcon(imagePath);
        } else {
            return new ImageIcon("src/image/Chưa-cập-nhập-hình-ảnh.jpeg");
        }
    }
    private void adjustImageSize(ImageIcon imageIcon) {
        AnhBia.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(AnhBia.getWidth(), AnhBia.getHeight(), java.awt.Image.SCALE_SMOOTH)));
    }
    private boolean addBorrowInfo(String username, int bookId, Date borrowDate, Time borrowTime) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String getUserIdQuery = "SELECT username FROM users WHERE email = ? OR phone = ?";
            String userId = null;

            // Tìm username dựa trên email hoặc số điện thoại
            try (PreparedStatement ps = conn.prepareStatement(getUserIdQuery)) {
                ps.setString(1, username);
                ps.setString(2, username);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    userId = rs.getString("username");
                }
                rs.close();
            }

            // Nếu userId không tìm thấy, return false
            if (userId == null) {
                return false;
            }

            // Chèn thông tin mượn sách vào bảng UserBooks
            String insertQuery = "INSERT INTO UserBooks (UserId, BookId, BorrowDate, BorrowTime) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
                ps.setString(1, userId);
                ps.setInt(2, bookId);
                ps.setDate(3, new java.sql.Date(borrowDate.getTime()));
                ps.setTime(4, borrowTime);

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
 // Phương thức hiển thị username
    private void displayUsername() {
        String query = "SELECT username FROM users WHERE email = ? OR phone = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fetchedUsername = rs.getString("username");
                Name.setText(fetchedUsername); // Hiển thị username
            } else {
                // Handle case when no username is found for the provided email or phone number
                Name.setText("Welcome, User");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            // Handle any exceptions
        }
    }
    private boolean isBookAvailable(int bookId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM UserBooks WHERE BookId = ?";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, bookId);
                ResultSet rs = ps.executeQuery();

                // Nếu có bất kỳ kết quả nào trả về, sách đã được mượn
                if (rs.next()) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true; // Sách có sẵn để mượn
    }
}