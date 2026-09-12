import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class bai1tcp {
    public static void main(String[] args) {
        String serverHost = "36.50.135.242";
        int serverPort = 2208;
        
        String studentCode = "B23DCCN129"; 
        String qCode = "iymrtCQj";
        
        try {
            Socket socket = new Socket(serverHost, serverPort);
            socket.setSoTimeout(5000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

            // a. Gửi chuỗi dạng "studentCode;qCode\n" (Thêm trực tiếp \n)
            String requestMsg = studentCode + ";" + qCode + "\n";
            writer.write(requestMsg);
            writer.flush();
            System.out.println("[CLIENT] Đã gửi: " + requestMsg.trim());

            // b. Nhận danh sách tên miền từ Server
            String responseMsg = reader.readLine();
            System.out.println("[SERVER] Gửi về: " + responseMsg);

            if (responseMsg != null && !responseMsg.trim().isEmpty()) {
                // c. Tách tên miền và lọc đuôi .edu
                String[] domains = responseMsg.split(",");
                List<String> eduDomains = new ArrayList<>();

                for (String domain : domains) {
                    String trimmedDomain = domain.trim();
                    if (trimmedDomain.toLowerCase().endsWith(".edu")) {
                        eduDomains.add(trimmedDomain);
                    }
                }

                // Định dạng đầu ra: nối các tên miền bằng ", " (hoặc "," tùy yêu cầu đề)
                String resultStr = String.join(", ", eduDomains) + "\n";

                // Gửi kết quả lại cho Server
                writer.write(resultStr);
                writer.flush();
                System.out.println("[CLIENT] Đã gửi kết quả: " + resultStr.trim());
            }

            // d. Đóng luồng
            reader.close();
            writer.close();
            socket.close();
            System.out.println("[CLIENT] Đã hoàn thành!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
