package ru.ifmo.prog.lab6;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Scanner;

import static java.lang.String.format;
import static ru.ifmo.prog.lab6.Constants.HOST;
import static ru.ifmo.prog.lab6.Constants.PORT;
import static ru.ifmo.prog.lab6.MessageType.*;
import static ru.ifmo.prog.lab6.OutputHelper.print;

public class Client {
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        String mainThreadName = Thread.currentThread().getName();
        while (true) {
            Socket socket = null;
            try {
                print(mainThreadName, format("try connect to %s:%d", HOST, PORT));
                socket = new Socket(HOST, PORT);
                print(mainThreadName, format("connected to %s:%d", HOST, PORT));
                try (Scanner scan = new Scanner(System.in)) {
                    showValues(socket);
                    print(mainThreadName, "removeAll | remove | clear | load | show | exit");
                    for(;;){
                        if (scan.hasNextLine()) {
                            Message message;
                            String command = scan.nextLine();
                            if (command.startsWith("removeAll")) {
                                message = new Message(REMOVE_ALL);
                                message.setPayload(mapper.readValue(command.split(" ")[1], Cart.class));
                                print(Thread.currentThread().getName(), "remove all like data...");
                                writeData(socket, message);
                                printStore(readData(socket));
                            } else if (command.startsWith("remove")) {
                                message = new Message(REMOVE);
                                message.setPayload(command.split(" ")[1]);
                                print(Thread.currentThread().getName(), "remove by id data...");
                                writeData(socket, message);
                                printStore(readData(socket));
                            } else if (command.startsWith("clear")) {
                                message = new Message(CLEAR);                                ;
                                print(Thread.currentThread().getName(), "clear data...");
                                writeData(socket, message);
                                printStore(readData(socket));
                            } else if (command.startsWith("load")) {
                                message = new Message(LOAD);
                                print(Thread.currentThread().getName(), "load data...");
                                writeData(socket, message);
                                printStore(readData(socket));
                            } else if (command.startsWith("show")) {
                                showValues(socket);
                            } else if (command.startsWith("exit")) {
                                System.out.println("Bye!");
                                message = new Message(CLOSE);
                                writeData(socket, message);
                                return;
                            } else {
                                System.out.println("Incorrect command");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                print(mainThreadName, e.getMessage());
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
        }
    }

    public static void showValues(Socket socket) throws IOException {
        print(Thread.currentThread().getName(), "show data...");
        Message message = new Message(SHOW);
        writeData(socket, message);
        Message response = readData(socket);
        LinkedHashMap<String, LinkedHashMap> store = (LinkedHashMap<String, LinkedHashMap>) response.payload;
        store.forEach((key, item) -> {
            System.out.printf("%s -> %s%n", key, item.toString());
        });
    }

    public static void writeData(Socket socket, Message message) throws IOException {
        String payload = mapper.writeValueAsString(message);
        OutputStream outputStream = socket.getOutputStream();
        print(Thread.currentThread().getName(), format("send data - %s", payload));
        Charset charset = Charset.forName("UTF-8");
        outputStream.write(charset.encode(payload).array());
    }

    public static Message readData(Socket socket) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result > 0) {
            buf.write((byte) result);
            result = bis.read();
        }
        String payload = buf.toString("UTF-8");

        print(Thread.currentThread().getName(), format("read data - %s", payload));
        return mapper.readValue(payload, Message.class);
    }

    public static void printStore(Message response) {
        LinkedHashMap<String, LinkedHashMap> store = (LinkedHashMap<String, LinkedHashMap>) response.payload;
        store.forEach((key, item) -> {
            System.out.printf("%s -> %s%n", key, item.toString());
        });
    }
}
