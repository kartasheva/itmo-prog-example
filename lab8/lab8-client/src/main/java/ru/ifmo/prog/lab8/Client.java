package ru.ifmo.prog.lab8;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import static java.lang.String.format;
import static ru.ifmo.prog.lab8.Constants.HOST;
import static ru.ifmo.prog.lab8.Constants.PORT;
import static ru.ifmo.prog.lab8.OutputHelper.print;

public class Client {
    private static ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    public static Message getMessage(Message request) throws IOException {
        return getMessage(request, null);
    }

    public static Message getMessage(Message request, Class<?> responsePayloadType) throws IOException {
        String requestThreadName = Thread.currentThread().getName();
        Message response = null;

        for (int i = 0; i < 5; i++) {
            Socket socket = null;
            try {
                print(requestThreadName, format("try connect to %s:%d", HOST, PORT));
                socket = new Socket(HOST, PORT);
                print(requestThreadName, format("connected to %s:%d", HOST, PORT));

                print(Thread.currentThread().getName(), "request - " + request.toString());
                writeData(socket, request);
                response = readData(socket, responsePayloadType);

                break;
            } catch (Exception e) {
                print(requestThreadName, e.getMessage());
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }

            if (response == null) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    print(requestThreadName, e.getMessage());
                }
            }
        }

        return response;
    }

    public static void writeData(Socket socket, Message message) throws IOException {
        String payload = mapper.writeValueAsString(message);
        OutputStream outputStream = socket.getOutputStream();
        print(Thread.currentThread().getName(), format("send data - %s", payload));
        Charset charset = Charset.forName("UTF-8");
        outputStream.write(charset.encode(payload).array());
    }

    public static Message readData(Socket socket, Class<?> deserializeType) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result > 0) {
            buf.write((byte) result);
            result = bis.read();
        }
        String payload = buf.toString("UTF-8");

        print(Thread.currentThread().getName(), format("read data - %s", payload));
        Message readMessage = new Message();
        JsonNode node = mapper.readTree(payload);

        readMessage.setMessageType(MessageType.valueOf(node.get("messageType").asText()));

        if (deserializeType != null) {
            TypeFactory typeFactory = mapper.getTypeFactory();
            MapType mapType = typeFactory.constructMapType(Hashtable.class, String.class, deserializeType);
            readMessage.setPayload(mapper.readValue(node.get("payload").toString(), mapType));
        } else {
            readMessage.setPayload(mapper.readValue(node.get("payload").toString(), LinkedHashMap.class));
        }

        return readMessage;
    }
}
