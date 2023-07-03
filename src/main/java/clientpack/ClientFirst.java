package clientpack;

import serverpack.Settings;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientFirst {

    private static Socket socket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static BufferedReader input;

    private static List<String> listByNickname = new ArrayList<>();//Arrays.asList("Бараш", "Ежик", "Нюша", "Копатыч", "Крош", "Совунья");
    private static Settings settings = new Settings();

    private static String host = settings.getHost();
    private static int port = settings.getPort();

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
        setNickname();
        choosingNickname();

        readMsg().start();
        writeMsg().start();

        readMsg().join();
        writeMsg().join();
    }

    private static void setNickname() {
        String line;
        System.out.print("Welcome to the chat. ");
        try {
            while (!(line = in.readLine()).equals("end")) {
                listByNickname.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void choosingNickname() {
        System.out.println("Please select a number by nickname from the list: ");
        int nick;
        for (int i = 0; i < listByNickname.size(); i++) {
            System.out.printf("%d. %s \n", i + 1, listByNickname.get(i));
        }
        while (true) {
            try {
                System.out.print("Input number: ");
                String inputNick = input.readLine();
                nick = Integer.parseInt(inputNick);
                nick--;
                if (nick >= listByNickname.size() || nick < 0) {
                    System.out.println("** ERROR ** the number is not recognized .Repeat the input: ");
                    continue;
                }
                break;
            } catch (IOException | NumberFormatException ex) {
                System.out.println("** ERROR ** the number is not recognized .Repeat the input: ");
            }
        }
        try {
            System.out.print("Click 'ENTER' to send your nickname-> " + listByNickname.get(nick));
            String msg = listByNickname.get(nick);
            out.write(msg);
            out.flush();
        } catch (IOException e) {
        }
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
