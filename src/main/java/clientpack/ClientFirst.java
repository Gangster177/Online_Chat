package clientpack;

import java.io.*;
import java.net.Socket;

public class ClientFirst {
    private static String host = "localhost";
    private static int port = 8800;

    private static Socket socket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static BufferedReader input;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("** System ** connecting...");
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
        }
        try {
            input = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
        }
        readMsg().start();
        writeMsg().start();

        readMsg().join();
        writeMsg().join();
    }

    private static Thread readMsg() {
        return new Thread(() -> {
            String msg;
            try {
                while (true) {
                    msg = in.readLine();
                    System.out.println(msg);
                }
            } catch (IOException e) {
            }
        });
    }

    private static Thread writeMsg() {
        return new Thread(() -> {
            while (true) {
                String msg;
                try {
                    msg = input.readLine();
                    if (msg.equals("/exit")) {
                        out.write("!!This user leaves the chat!!\n");
                        out.flush();
                        out.write(msg + "\n");
                        out.flush();
                        try {
                            socket.close();
                            in.close();
                            out.close();
                        } catch (IOException e) {
                        }
                        break;
                    } else {
                        out.write(msg + "\n");
                        out.flush();
                    }
                } catch (IOException e) {
                }
            }
        });
    }
}
