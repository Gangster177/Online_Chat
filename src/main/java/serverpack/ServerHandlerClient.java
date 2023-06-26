package serverpack;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerHandlerClient extends Thread {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ServerHandlerClient.class);

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String name;
    private String logMsg;

    public ServerHandlerClient(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        logMsg = " ** System ** New connection";
        log.info(logMsg);
        System.out.println(printDate() + logMsg);

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
                logMsg = "[ " + printDate() + "] " + name + ": " + msg;
                System.out.println(logMsg);
                log.info(logMsg);
//                Server.history.add(history);
                for (ServerHandlerClient vr : Server.serverList) {
                    vr.send(logMsg);
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
            logMsg = "WELCOME to the Chat! Press your nick name:";
            log.info(logMsg);
            out.write(logMsg + "\n");
            out.flush();

            name = in.readLine();
            logMsg = "Hello <<" + name + ">>";
            log.info(logMsg);
            out.write(logMsg + "\n");
            out.flush();

            msg = printDate() + " ** INFO ** The new user has connected => " + name;
            System.out.println(msg);
            log.info(msg);
            //Server.history.add(msg);
            for (ServerHandlerClient vr : Server.serverList) {
                vr.send(msg);
            }
        } catch (IOException e) {
        }
        //Server.history.printLast(out);
    }

    private String printDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }
}
