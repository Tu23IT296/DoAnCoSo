package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import SQL.DatabaseConnection;
import Security.PasswordHash;

public class Boss {
    public static final String LOGIN_BOSS = "LOGIN_BOSS";
    public static final String LOGIN_USER = "LOGIN_USER";
    public static final String ADD_USER = "ADD_USER";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";

    public static String checkDataDKI(String username, String phoneNumber, String email, String password) {
        // Validate username length
        if (username.length() < 5 || username.length() > 10) {
            return "Username không hợp lệ. Username phải có ít nhất 5 kí tự.";
        }
        // Validate phone number
        if (phoneNumber.length() != 10) {
            return "Số điện thoại phải có đúng 10 chữ số.";
        }

        // Validate email
        if (!email.matches("^\\w+@vku\\.udn\\.vn$")) {
            return "Email không hợp lệ. Email phải có định dạng example@vku.udn.vn.";
        }

        // Validate password length
        if (password.length() < 6 || password.length() > 8) {
            return "Mật khẩu không hợp lệ. Mật khẩu phải có độ dài từ 6 đến 8 kí tự.";
        }

        // Mã hóa mật khẩu sau khi kiểm tra độ dài
        String hashedPassword = PasswordHash.hashPassword(password);

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if the data already exists in the Boss table
            String queryBoss = "SELECT * FROM Boss WHERE (Phone = ? OR Email = ?) AND Password = ?";
            PreparedStatement statementBoss = connection.prepareStatement(queryBoss);
            statementBoss.setString(1, phoneNumber);
            statementBoss.setString(2, email);
            statementBoss.setString(3, hashedPassword);
            ResultSet resultSetBoss = statementBoss.executeQuery();

            // Check if the data already exists in the Users table
            String queryUser = "SELECT * FROM Users WHERE (Phone = ? OR Email = ?) AND Password = ?";
            PreparedStatement statementUser = connection.prepareStatement(queryUser);
            statementUser.setString(1, phoneNumber);
            statementUser.setString(2, email);
            statementUser.setString(3, hashedPassword);
            ResultSet resultSetUser = statementUser.executeQuery();

            if (resultSetBoss.next()) {
                resultSetBoss.close();
                statementBoss.close();
                resultSetUser.close();
                statementUser.close();
                return "Tài khoản đã tồn tại trong bảng Boss";
            } else if (resultSetUser.next()) {
                resultSetBoss.close();
                statementBoss.close();
                resultSetUser.close();
                statementUser.close();
                return "Tài khoản đã tồn tại trong bảng Users";
            } else {
                // If the data doesn't exist, add a new user
                String insertQuery = "INSERT INTO Users (Username, Phone, Email, Password) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, username);
                insertStatement.setString(2, phoneNumber);
                insertStatement.setString(3, email);
                insertStatement.setString(4, hashedPassword);
                insertStatement.executeUpdate();
                insertStatement.close();
                resultSetBoss.close();
                statementBoss.close();
                resultSetUser.close();
                statementUser.close();
                return ADD_USER;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Lỗi xảy ra khi thực thi SQL.";
        }
    }
    public static String checkDataDNHAP(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String queryBoss = "SELECT * FROM Boss WHERE (Phone = ? OR Email = ?) AND Password = ?";
            PreparedStatement statementBoss = connection.prepareStatement(queryBoss);
            statementBoss.setString(1, username);
            statementBoss.setString(2, username);
            statementBoss.setString(3, password);
            ResultSet resultSetBoss = statementBoss.executeQuery();

            String queryUser = "SELECT * FROM Users WHERE (Phone = ? OR Email = ?) AND Password = ?";
            PreparedStatement statementUser = connection.prepareStatement(queryUser);
            statementUser.setString(1, username);
            statementUser.setString(2, username);
            statementUser.setString(3, password);
            ResultSet resultSetUser = statementUser.executeQuery();

            if (resultSetBoss.next()) {
                resultSetBoss.close();
                statementBoss.close();
                resultSetUser.close();
                statementUser.close();
                return LOGIN_BOSS;
            } else if (resultSetUser.next()) {
                resultSetBoss.close();
                statementBoss.close();
                resultSetUser.close();
                statementUser.close();
                return LOGIN_USER;
            } else {
                resultSetBoss.close();
               statementBoss.close();
                resultSetUser.close();
                statementUser.close();
                return LOGIN_FAILED;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}