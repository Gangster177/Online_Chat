package serverpack;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerHandlerClient extends Thread {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ServerHandlerClient.class);
    private static List<String> nickname = new ArrayList<>(Arrays.asList("Бараш", "Ежик", "Нюша", "Копатыч", "Крош", "Совунья", "end"));

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

        setNickname();
        newConnection();
        start();

        nickname.add(this.name);
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
                        }
                    } catch (IOException e) {
                    }
                    break;
                }
                logMsg = "[ " + printDate() + "] " + name + ": " + msg;
                System.out.println(logMsg);
                log.info(logMsg);
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

    private void setNickname() {
        for (String value : nickname) {
            try {
                out.write(value + "\n");
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            name = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < nickname.size(); i++) {
            if (nickname.get(i).equals(name)) {
                nickname.remove(i);
            }
        }
    }

    private void newConnection() {
        String msg;
        try {
            logMsg = "Hello <<" + name + ">> \nyou have entered the chat: ";
            log.info(logMsg);
            out.write(logMsg + "\n");
            out.flush();

            msg = printDate() + " ** INFO ** The new user has connected => " + name;
            System.out.println(msg);
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
