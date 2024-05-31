package XML;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class xml {

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        FileWriter writer = null;

        try {
            // Nạp trình điều khiển MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/tv"; // Địa chỉ URL của cơ sở dữ liệu
            String userName = "root"; // Tên đăng nhập
            String password = ""; // Mật khẩu
            connection = DriverManager.getConnection(url, userName, password); // Kết nối đến cơ sở dữ liệu
            statement = connection.createStatement(); // Tạo một Statement

            // Truy vấn SQL để lấy dữ liệu từ hai bảng và thêm trạng thái mượn sách
            String sql = "SELECT UserBooks.UserId, Books.title AS bookTitle, UserBooks.BorrowDate, UserBooks.BorrowTime, 'Chưa trả' AS TinhTrang " +
                         "FROM UserBooks " +
                         "INNER JOIN Books ON UserBooks.BookId = Books.id " +
                         "UNION " +
                         "SELECT trasach.UserId, Books.title AS bookTitle, trasach.LendDate, trasach.LendTime, CONCAT('Đã trả - ', trasach.LendDate, ' ', trasach.LendTime) AS TinhTrang " +
                         "FROM trasach " +
                         "INNER JOIN Books ON trasach.BookId = Books.id";
            ResultSet resultSet = statement.executeQuery(sql); // Thực thi truy vấn và lấy kết quả

            // Tạo tên tệp để lưu tất cả các bản ghi
            String fileName = "UserBooks.xml";
            writer = new FileWriter(new File(fileName));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<userBooks>\n");

            // Duyệt qua từng bản ghi trong ResultSet
            while (resultSet.next()) {
                String userId = resultSet.getString("UserId");
                String bookTitle = resultSet.getString("bookTitle");
                java.sql.Date borrowDate = resultSet.getDate("BorrowDate");
                java.sql.Time borrowTime = resultSet.getTime("BorrowTime");
                String status = resultSet.getString("TinhTrang");

                // Ghi thông tin của mỗi bản ghi vào file XML
                writer.write("\t<userBook>\n");
                writer.write("\t\t<userId>" + userId + "</userId>\n");
                writer.write("\t\t<bookTitle>" + bookTitle + "</bookTitle>\n");
                writer.write("\t\t<borrowDate>" + borrowDate + "</borrowDate>\n");
                writer.write("\t\t<borrowTime>" + borrowTime + "</borrowTime>\n");
                writer.write("\t\t<status>" + status + "</status>\n");
                writer.write("\t</userBook>\n");
            }

            writer.write("</userBooks>");
            System.out.println("Đã lưu tất cả thông tin vào tệp " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close(); // Đóng FileWriter
                if (statement != null) statement.close(); // Đóng Statement
                if (connection != null) connection.close(); // Đóng Connection
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
