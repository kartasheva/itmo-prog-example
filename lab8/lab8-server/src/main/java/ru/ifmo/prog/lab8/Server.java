package ru.ifmo.prog.lab8;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;
import static ru.ifmo.prog.lab8.Constants.HOST;
import static ru.ifmo.prog.lab8.Constants.PORT;
import static ru.ifmo.prog.lab8.OutputHelper.print;

public class Server {
    private static ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    public static void writeData (SocketChannel socket, Message message) throws IOException {
        String payload = mapper.writeValueAsString(message);
        print(Thread.currentThread().getName(), format("send data - %s", payload));
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer buffer = ByteBuffer.wrap(charset.encode(payload).array());
        socket.write(buffer);
    }

    public static Message readData (SocketChannel socketChannel) throws IOException {
        String payload;
        ByteBuffer buffer = ByteBuffer.allocate(10);
        Charset charset = Charset.forName("UTF-8");
        StringBuilder builder = new StringBuilder();
        while (socketChannel.read(buffer) > 0) {
            buffer.rewind();
            builder.append(charset.decode(buffer));
            buffer.flip();
        }
        payload = builder.toString();
        print(Thread.currentThread().getName(), format("read data - %s", payload));
        return mapper.readValue(payload, Message.class);

    }

    private List<ResponseHandler> responseHandlers;
    private Boolean work = false;

    public Server() {
        responseHandlers = new ArrayList<>();
    }

    public void addResponseHandler(ResponseHandler responseHandler) {
        responseHandlers.add(responseHandler);
    }

    public void start() throws IOException {
        work = true;

        Selector selector = Selector.open();

        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(HOST, PORT));
        print(Thread.currentThread().getName(), format("Server started on %s:%d", HOST, PORT));

        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        while (work) {
            print(Thread.currentThread().getName(), "Wait for new connection...");
            selector.select();
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {

                SelectionKey key = selectedKeys.next();

                if (key.isAcceptable()) {
                    SocketChannel client = serverSocket.accept();
                    Thread clientHandler = new Thread(() -> {
                        print(Thread.currentThread().getName(), "started...");

                        try {
                            Selector clientSelector = Selector.open();
                            client.configureBlocking(false);
                            client.register(clientSelector, SelectionKey.OP_READ);
                            while (work) {
                                clientSelector.select();
                                Iterator<SelectionKey> clientSelectionKeys = clientSelector.selectedKeys().iterator();
                                while (clientSelectionKeys.hasNext()) {
                                    SelectionKey clientSelectionKey = clientSelectionKeys.next();

                                    if (clientSelectionKey.isReadable()) {
                                        Message message = readData(client);
                                        if (message != null) {
                                            if (responseHandlers != null) {
                                                responseHandlers.forEach(responseHandler -> responseHandler.handle(message, client));
                                            }
                                        }
                                    }

                                    clientSelectionKeys.remove();
                                }
                            }
                        } catch (IOException e) {
                            print(Thread.currentThread().getName(), e.getMessage());
                        }
                    });
                    clientHandler.start();
                }
                selectedKeys.remove();
            }
        }
    }

    public void stop() {
        work = false;
    }
}

