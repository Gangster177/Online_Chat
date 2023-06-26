package serverpack;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ServerHandlerClient extends Thread {
    private static final Logger log = Logger.getLogger("History");

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String name;

    public ServerHandlerClient(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String msgInfo = " ** System ** New connection";
        log.info(msgInfo);
        System.out.println(printDate() + msgInfo);
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
                String history = name + ": " + msg;
                log.info(history);
                String historyMsg = "[ " + printDate() + "] " + name + ": " + msg;
                for (ServerHandlerClient vr : Server.serverList) {
                    vr.send(historyMsg);
                }
            }
        } catch (IOException e) {
        }
    }

    private void send(String msg) {
        try {
            //log.info(msg);
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
            log.info(msg);
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
