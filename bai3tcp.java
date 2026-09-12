import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class bai3tcp {
    public static void main(String[] args) {
        String serverHost = "36.50.135.242";
        int serverPort = 2206;
        
        String studentCode = "B23DCCN129"; 
        String qCode = "xmqDbKA9";
        
        try {
            Socket socket = new Socket(serverHost, serverPort);
            socket.setSoTimeout(5000);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // aGửi yêu cầu đến server
            String requestMsg = studentCode + ';' + qCode + '\n';
            out.write(requestMsg.getBytes());
            out.flush();
            System.out.println("[Client] sent: " + requestMsg.trim());
            // b nhan chuoi so nguyen
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            if(bytesRead != -1){
                String responseMsg = new String(buffer, 0, bytesRead).trim();
                System.out.println("[Server] sent: " + responseMsg);
                String[] strnums = responseMsg.split(",");
                int n = strnums.length;
                int[] nums = new int[n];
                for(int i = 0; i < n; i++){
                    nums[i] = Integer.parseInt(strnums[i].trim());
                }
                int stt = 0;
                int m1 = Integer.MIN_VALUE + 1;
                int m2 = Integer.MIN_VALUE;
                for(int i = 0; i < n; i++){
                    if(nums[i] > m1){
                        m1 = nums[i];
                        stt = i;
                    }
                    if(nums[i] > m2 && nums[i] < m1){
                        m2 = nums[i];
                        stt = i;
                    }

                }
                String result = m2 + "," + stt + "\n";
                out.write(result.getBytes());
                out.flush();
                System.out.println("[Client] sent: " + result.trim());

            }
            in.close();
            out.close();
            socket.close();
            System.out.println("[Client] done");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}