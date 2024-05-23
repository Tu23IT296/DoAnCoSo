package THUVIEN;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import SQL.DatabaseConnection;

public class QLI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable BangSach;
    private JMenuBar menuBar;
    private JTextField textField;
    private JLabel AnhBia;
    private JTextField TENSACH;
    private JComboBox<String> THELOAI;
    private JTable BMUON;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    QLI frame = new QLI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public QLI() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1400, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Tạo JScrollPane để chứa bảng BangSach
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(490, 43, 636, 410);
        contentPane.add(scrollPane);

        BangSach = new JTable();
        BangSach.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        scrollPane.setViewportView(BangSach);
        
        // Hiển thị dữ liệu từ cơ sở dữ liệu lên bảng sách
        displayData("SELECT * FROM books");
        
        BangSach.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = BangSach.getSelectedRow();
                    if (selectedRow != -1) {
                        String bookTitle = String.valueOf(BangSach.getValueAt(selectedRow, 1));
                        String genre = String.valueOf(BangSach.getValueAt(selectedRow, 2)); // Thể loại sách

                        // Hiển thị tên sách và thể loại sách trong các JTextField tương ứng
                        TENSACH.setText(bookTitle);
                        THELOAI.setSelectedItem(genre);

                        // Tải và hiển thị ảnh bìa sách
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

        // Tạo menu bar
        menuBar = new JMenuBar();
        menuBar.setBounds(480, 11, 101, 22);
        contentPane.add(menuBar);

        // Tạo menu "Thể loại"
        JMenu categoryMenu = new JMenu("Thể loại");
        menuBar.add(categoryMenu);

        // Tạo các mục con
        JMenuItem allMenuItem = new JMenuItem("Tất cả");
        JMenuItem businessMenuItem = new JMenuItem("Kinh doanh");
        JMenuItem novelMenuItem = new JMenuItem("Tiểu thuyết");
        JMenuItem comicMenuItem = new JMenuItem("Truyện tranh");

        // Thêm các mục con vào menu "Thể loại"
        categoryMenu.add(allMenuItem);
        categoryMenu.add(businessMenuItem);
        categoryMenu.add(novelMenuItem);
        categoryMenu.add(comicMenuItem);

        // Thêm các hành động cho mục con của menu
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

     // Trong phần khởi tạo của lớp QLI():
        JButton THEM = new JButton("THÊM");
     // Trong phần ActionListener của nút "THÊM"
        THEM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bookTitle = TENSACH.getText();
                String genre = (String) THELOAI.getSelectedItem();

                try {
                    // Kết nối đến cơ sở dữ liệu
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO books (title, genre) VALUES (?, ?)");
                    pstmt.setString(1, bookTitle);
                    pstmt.setString(2, genre);
                    
                    // Thực thi truy vấn thêm sách mới
                    int rowsInserted = pstmt.executeUpdate();
                    
                    // Kiểm tra xem sách đã được thêm thành công hay không
                    if (rowsInserted > 0) {
                        // Hiển thị thông báo thêm sách thành công
                        JOptionPane.showMessageDialog(contentPane, "Sách đã được thêm vào cơ sở dữ liệu.");
                        
                        // Cập nhật lại dữ liệu trong bảng hiển thị
                        displayData("SELECT * FROM books");
                     
                        TENSACH.setText(""); // Đặt lại giá trị của ô TENSACH thành rỗng sau khi hoàn thành nhiệm vụ

                    } else {
                        // Hiển thị thông báo lỗi nếu không thêm được sách
                        JOptionPane.showMessageDialog(contentPane, "Thêm sách không thành công.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    // Đóng kết nối
                    pstmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        THEM.setFont(new Font("Times New Roman", Font.BOLD, 20));
        THEM.setBounds(10, 103, 225, 35);
        contentPane.add(THEM);


        JLabel NAME = new JLabel("BOSS");
        NAME.setFont(new Font("Times New Roman", Font.BOLD, 70));
        NAME.setHorizontalAlignment(SwingConstants.CENTER);
        NAME.setBounds(10, 11, 460, 82);
        contentPane.add(NAME);

     // Trong phần khởi tạo của lớp QLI():
        JButton btnNewButton_1 = new JButton("XÓA");
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnNewButton_1.setBounds(10, 145, 225, 35);
        contentPane.add(btnNewButton_1);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = BangSach.getSelectedRow();
                if (selectedRow != -1) {
                    int bookId = Integer.parseInt(BangSach.getValueAt(selectedRow, 0).toString()); // Giả sử cột đầu tiên là ID sách
                    
                    try {
                        // Kết nối đến cơ sở dữ liệu
                        Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM books WHERE id = ?");
                        pstmt.setInt(1, bookId);
                        
                        // Thực thi truy vấn xóa
                        int rowsDeleted = pstmt.executeUpdate();
                        
                        // Kiểm tra xem dữ liệu đã được xóa thành công hay không
                        if (rowsDeleted > 0) {
                            // Hiển thị thông báo xóa thành công
                            JOptionPane.showMessageDialog(contentPane, "Sách đã được xóa thành công.");
                            
                            // Cập nhật lại dữ liệu trong bảng hiển thị
                            displayData("SELECT * FROM books");
                            
                            TENSACH.setText(""); // Đặt lại giá trị của ô TENSACH thành rỗng sau khi hoàn thành nhiệm vụ

                        } else {
                            // Hiển thị thông báo lỗi nếu không xóa được dữ liệu
                            JOptionPane.showMessageDialog(contentPane, "Xóa sách không thành công.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                        
                        // Đóng kết nối
                        pstmt.close();
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // Hiển thị thông báo khi không có hàng nào được chọn
                    JOptionPane.showMessageDialog(contentPane, "Vui lòng chọn một sách để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


     // Trong phần khởi tạo của lớp QLI():
        JButton btnNewButton_2 = new JButton("SỬA");
        btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnNewButton_2.setBounds(10, 190, 225, 35);
        contentPane.add(btnNewButton_2);
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = BangSach.getSelectedRow();
                if (selectedRow != -1) {
                    String bookTitle = TENSACH.getText();
                    String genre = (String) THELOAI.getSelectedItem();
                    int bookId = Integer.parseInt(BangSach.getValueAt(selectedRow, 0).toString()); // Giả sử cột đầu tiên là ID sách
                    
                    try {
                        // Kết nối đến cơ sở dữ liệu
                        Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement("UPDATE books SET title = ?, genre = ? WHERE id = ?");
                        pstmt.setString(1, bookTitle);
                        pstmt.setString(2, genre);
                        pstmt.setInt(3, bookId);
                        
                        // Thực thi truy vấn cập nhật
                        int rowsUpdated = pstmt.executeUpdate();
                        
                        // Kiểm tra xem dữ liệu đã được cập nhật thành công hay không
                        if (rowsUpdated > 0) {
                            // Hiển thị thông báo cập nhật thành công
                            JOptionPane.showMessageDialog(contentPane, "Thông tin sách đã được cập nhật thành công.");
                            
                            // Cập nhật lại dữ liệu trong bảng hiển thị
                            displayData("SELECT * FROM books");
                            
                            TENSACH.setText(""); // Đặt lại giá trị của ô TENSACH thành rỗng sau khi hoàn thành nhiệm vụ

                        } else {
                            // Hiển thị thông báo lỗi nếu không cập nhật được dữ liệu
                            JOptionPane.showMessageDialog(contentPane, "Cập nhật thông tin sách không thành công.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                        
                        // Đóng kết nối
                        pstmt.close();
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // Hiển thị thông báo khi không có hàng nào được chọn
                    JOptionPane.showMessageDialog(contentPane, "Vui lòng chọn một sách để sửa đổi thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        textField = new JTextField();
        textField.setBounds(1126, 11, 250, 22);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("TÌM KIẾM");
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnNewButton.setBounds(957, 10, 159, 23);
        contentPane.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchTitle = textField.getText();
                if (searchTitle != null && !searchTitle.isEmpty()) {
                    displayData("SELECT * FROM books WHERE title LIKE '%" + searchTitle + "%'");
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Vui lòng nhập tên sách để tìm kiếm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        
        JButton btnLmMi = new JButton("LÀM MỚI");
        btnLmMi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayData("SELECT * FROM books");
            }
        });
        btnLmMi.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnLmMi.setBounds(788, 11, 159, 22);
        contentPane.add(btnLmMi);
        
        AnhBia = new JLabel("New label");
        AnhBia.setBounds(1126, 32, 250, 431);
        contentPane.add(AnhBia);
        
        TENSACH = new JTextField();
        TENSACH.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        TENSACH.setColumns(10);
        TENSACH.setBounds(239, 103, 231, 62);
        contentPane.add(TENSACH);
        
     // Trong phương thức QLI():
        THELOAI = new JComboBox<>();
        THELOAI.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        THELOAI.addItem("Tiểu thuyết");
        THELOAI.addItem("Kinh doanh");
        THELOAI.addItem("Truyện tranh");
        THELOAI.setBounds(239, 168, 231, 57);
        contentPane.add(THELOAI);
        
     // Tạo bảng BMUON
        BMUON = new JTable();
        BMUON.setBounds(10, 235, 460, 228);
        contentPane.add(BMUON);

        // Gọi phương thức displayUserBooks() để hiển thị dữ liệu từ bảng UserBooks lên bảng BMUON
        displayUserBooks();

        // Đảm bảo rằng bảng BMUON được đưa vào JScrollPane và hiển thị đúng vị trí và kích thước
        JScrollPane scrollPaneBMUON = new JScrollPane(BMUON);
        scrollPaneBMUON.setBounds(10, 235, 460, 135);
        contentPane.add(scrollPaneBMUON);
        
        JButton LMBANGMUON = new JButton("LÀM MỚI");
     // Trong phần khởi tạo của lớp QLI():
        LMBANGMUON.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Gọi phương thức displayUserBooks() để làm mới bảng BMUON
                displayUserBooks();
            }
        });
        LMBANGMUON.setFont(new Font("Times New Roman", Font.BOLD, 20));
        LMBANGMUON.setBounds(10, 380, 225, 35);
        contentPane.add(LMBANGMUON);
        
        JButton TRASACH = new JButton("ĐÃ TRẢ");
        TRASACH.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lấy chỉ mục của hàng được chọn trong bảng BMUON
                int selectedRow = BMUON.getSelectedRow();
                if (selectedRow != -1) {
                    // Lấy ID của sách từ hàng được chọn
                    String userId = String.valueOf(BMUON.getValueAt(selectedRow, 0));
                    int bookId = Integer.parseInt(BMUON.getValueAt(selectedRow, 1).toString());
                    
                    try {
                        // Kết nối đến cơ sở dữ liệu
                        Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM UserBooks WHERE UserId = ? AND BookId = ?");
                        pstmt.setString(1, userId);
                        pstmt.setInt(2, bookId);
                        
                        // Thực thi truy vấn xóa
                        int rowsDeleted = pstmt.executeUpdate();
                        
                        // Kiểm tra xem dữ liệu đã được xóa thành công hay không
                        if (rowsDeleted > 0) {
                            // Hiển thị thông báo xóa thành công
                            JOptionPane.showMessageDialog(contentPane, "Sách đã được trả thành công.");
                            
                            // Cập nhật lại dữ liệu trong bảng hiển thị
                            displayUserBooks();
                        } else {
                            // Hiển thị thông báo lỗi nếu không xóa được dữ liệu
                            JOptionPane.showMessageDialog(contentPane, "Trả sách không thành công.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                        
                        // Đóng kết nối
                        pstmt.close();
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // Hiển thị thông báo khi không có hàng nào được chọn
                    JOptionPane.showMessageDialog(contentPane, "Vui lòng chọn một sách để trả.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        TRASACH.setFont(new Font("Times New Roman", Font.BOLD, 20));
        TRASACH.setBounds(245, 380, 225, 35);
        contentPane.add(TRASACH);
        
        JButton CHAT = new JButton("CHAT");
        CHAT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the CHATUSER window
                CHATBOSS chatWindow = new CHATBOSS();
                chatWindow.setVisible(true);
            }
        });
        CHAT.setFont(new Font("Times New Roman", Font.BOLD, 20));
        CHAT.setBounds(10, 425, 225, 35);
        contentPane.add(CHAT);

        // Trong phương thức displayData(String query):
        String selectedGenre = (String) THELOAI.getSelectedItem();
        String query = "SELECT * FROM books WHERE genre = '" + selectedGenre + "'";
        
    }

    // Phương thức điều chỉnh tự động kích thước ảnh
    private void adjustImageSize(ImageIcon imageIcon) {
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();

        double scaleX = (double) AnhBia.getWidth() / width;
        double scaleY = (double) AnhBia.getHeight() / height;
        double scale = Math.min(scaleX, scaleY);

        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        ImageIcon scaledImageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH));
        AnhBia.setIcon(scaledImageIcon);
    }

    private void displayData(String query) {
        try {
            // Kết nối đến cơ sở dữ liệu bằng lớp kết nối đã định nghĩa
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            
            // Truy vấn dữ liệu từ bảng books
            ResultSet rs = stmt.executeQuery(query);

            // Tạo DefaultTableModel để lưu dữ liệu
            DefaultTableModel model = new DefaultTableModel();
            BangSach.setModel(model);

            // Lấy metadata từ ResultSet
            int columnCount = rs.getMetaData().getColumnCount();

            // Thêm các cột vào model
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rs.getMetaData().getColumnName(i));
            }

            // Thêm dữ liệu từ ResultSet vào model
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            // Đóng kết nối
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
            ImageIcon imageIcon = new ImageIcon(imagePath);
            return imageIcon;
        } else {
            // Trả về một ImageIcon cho ảnh mặc định "Chưa cập nhật hình ảnh.png"
            ImageIcon defaultImageIcon = new ImageIcon("src/image/Chưa-cập-nhập-hình-ảnh.jpeg");
            return defaultImageIcon;
        }
    }
    private void displayUserBooks() {
        try {
            // Kết nối đến cơ sở dữ liệu bằng lớp kết nối đã định nghĩa
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();

            // Truy vấn dữ liệu từ bảng UserBooks và kết hợp thông tin với bảng Books để lấy tên sách
            String query = "SELECT UserBooks.UserId, Books.title, UserBooks.BorrowDate, UserBooks.BorrowTime " +
                            "FROM UserBooks " +
                            "INNER JOIN Books ON UserBooks.BookId = Books.id";

            // Thực thi truy vấn
            ResultSet rs = stmt.executeQuery(query);

            // Tạo DefaultTableModel để lưu dữ liệu
            DefaultTableModel model = new DefaultTableModel();
            BMUON.setModel(model);

            // Lấy metadata từ ResultSet
            int columnCount = rs.getMetaData().getColumnCount();

            // Thêm các cột vào model
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rs.getMetaData().getColumnName(i));
            }

            // Thêm dữ liệu từ ResultSet vào model
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            // Đóng kết nối
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
