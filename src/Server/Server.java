package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static final int SERVER_PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is running...");
            System.out.println("-----------------------------------------");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Chấp nhận kết nối từ client
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(out); // Thêm clientWriter vào danh sách

                // Tạo và chạy một thread để xử lý client kết nối
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                // Đọc lệnh từ client
                String command;
                while ((command = in.readLine()) != null) {
                    if (command.equals("DNHAP")) {
                        handleDNHAP();
                    } else if (command.equals("DKI")) {
                        handleDKI();
                    } 
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // Đóng kết nối và các luồng
                    in.close();
                    out.close();
                    clientSocket.close();
                    clientWriters.remove(out); // Loại bỏ clientWriter khỏi danh sách khi client ngắt kết nối
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleDNHAP() throws IOException {
            String username = in.readLine();
            String password = in.readLine();

            // Gọi phương thức xử lý từ Boss cho DNHAP
            String result = Boss.checkDataDNHAP(username, password);

            // Gửi kết quả kiểm tra cho client
            out.println(result);

            // Hiển thị thông báo tương ứng trên console của Server
            if (result.equals(Boss.LOGIN_BOSS)) {
                System.out.println("Boss đã đăng nhập: " );
            } else if (result.equals(Boss.LOGIN_USER)) {
                System.out.println("User đã đăng nhập: " );
            } else if (result.equals(Boss.LOGIN_FAILED)) {
                System.out.println("Đăng nhập thất bại: " );
            }
        }

        private void handleDKI() throws IOException {
            String username = in.readLine();
            String phoneNumber = in.readLine();
            String email = in.readLine();
            String password = in.readLine();

            // Gọi phương thức xử lý từ Boss cho DKI
            String result = Boss.checkDataDKI(username, phoneNumber, email, password);

            // Gửi kết quả kiểm tra cho client
            out.println(result);
        }
    }
}
