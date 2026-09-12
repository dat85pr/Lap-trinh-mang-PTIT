import java.io.*;
import java.net.*;

public class test {

    public static void main(String[] args) {
        String serverIP = "36.50.135.242";
        int port = 2206;

        String studentCode = "B23DCCN129";
        String qCode = "Hyv56gGFsvg";

        try (Socket socket = new Socket()) {
            // Timeout tối đa 5 giây
            socket.connect(new InetSocketAddress(serverIP, port), 5000);
            socket.setSoTimeout(5000);

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // =========================
            // a. Gửi mã sinh viên và mã câu hỏi
            // =========================
            String request = studentCode + ";" + qCode;

            os.write(request.getBytes());
            os.flush();

            System.out.println("Đã gửi: " + request);

            // =========================
            // b. Nhận chuỗi số nguyên
            // =========================
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] data = new byte[1024];
            int bytesRead;

            while ((bytesRead = is.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);

                // Nếu server gửi xong dữ liệu và đóng output
                if (bytesRead < data.length) {
                    break;
                }
            }

            String received = buffer.toString().trim();

            System.out.println("Nhận từ server: " + received);

            // =========================
            // c. Tìm khoảng cách nhỏ nhất
            // =========================
            String[] parts = received.split(",");

            int[] numbers = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                numbers[i] = Integer.parseInt(parts[i].trim());
            }

            int minDistance = Integer.MAX_VALUE;
            int firstNumber = 0;
            int secondNumber = 0;

            // So sánh tất cả các cặp số
            for (int i = 0; i < numbers.length; i++) {
                for (int j = i + 1; j < numbers.length; j++) {

                    int distance = Math.abs(numbers[i] - numbers[j]);

                    if (distance < minDistance) {
                        minDistance = distance;

                        // Đưa số nhỏ hơn lên trước
                        if (numbers[i] < numbers[j]) {
                            firstNumber = numbers[i];
                            secondNumber = numbers[j];
                        } else {
                            firstNumber = numbers[j];
                            secondNumber = numbers[i];
                        }
                    }
                }
            }

            // Chuỗi kết quả:
            // khoảng cách nhỏ nhất, số thứ nhất, số thứ hai
            String result = minDistance + "," + firstNumber + "," + secondNumber;

            // =========================
            // Gửi kết quả lên server
            // =========================
            os.write(result.getBytes());
            os.flush();

            System.out.println("Đã gửi kết quả: " + result);

        } catch (SocketTimeoutException e) {
            System.out.println("Kết nối hoặc giao tiếp quá thời gian 5 giây!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}