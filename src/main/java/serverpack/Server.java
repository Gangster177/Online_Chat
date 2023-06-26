package serverpack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<ServerHandlerClient> serverList = new ArrayList<>();
//    public static History history = new History();
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8800);
        System.out.println("<<<>>> System: Server Started <<<>>>");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerHandlerClient(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}