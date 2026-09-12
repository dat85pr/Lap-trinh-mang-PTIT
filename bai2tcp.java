import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class bai2tcp {
    public static void main(String[] args) {
        String serverHost = "36.50.135.242";
        int serverPort = 2206;
        
        String studentCode = "B23DCCN129"; 
        String qCode = "Hyv56gGF";
        
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
                Arrays.sort(nums);
                int mindiff = Integer.MAX_VALUE;
                int n1 = 0;
                int n2 = 0;
                for(int i = 0; i < n - 1; i++){
                    int diff = nums[i + 1] - nums[i];
                    if( diff < mindiff){
                        mindiff = diff;
                        n1 = nums[i];
                        n2 = nums[i + 1];
                    }

                }
                String result = mindiff + "," + n1 + "," + n2 + "\n";
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