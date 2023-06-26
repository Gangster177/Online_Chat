package serverpack;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerHandlerClient extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String name;

    public ServerHandlerClient(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println(printDate() + " ** System ** New connection");
        newConnection();
        start();
    }

    @Override
    public void run() {
        String msg;
        try {
            while (true) {
                msg = in.readLine();
                if (msg.equals("/exit")) {
                    try {
                        socket.close();
                        in.close();
                        out.close();
                        for (ServerHandlerClient vr : Server.serverList) {
                            if (vr.equals(this)) vr.interrupt();
                            Server.serverList.remove(this);
                        }
                    } catch (IOException e) {
                    }
                    break;
                }
                String history = "[ " + printDate() + "] " + name + ": " + msg;
                System.out.println(history);
                for (ServerHandlerClient vr : Server.serverList) {
                    vr.send(history);
                }
            }
        } catch (IOException e) {
        }
    }

    private void send(String msg) {
        try {
            out.write(printDate() + msg + "\n");
            out.flush();
        } catch (IOException e) {
        }
    }

    private void newConnection() {
        String msg;
        try {
            out.write("WELCOME to the Chat! Press your nick name:\n");
            out.flush();
            name = in.readLine();
            out.write("Hello <<" + name + ">>\n");
            out.flush();
            msg = printDate() + " ** INFO ** The new user has connected => " + name;
            System.out.println(msg);
            for (ServerHandlerClient vr : Server.serverList) {
                vr.send(msg);
            }
        } catch (IOException e) {
        }
    }

    private String printDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }
}
